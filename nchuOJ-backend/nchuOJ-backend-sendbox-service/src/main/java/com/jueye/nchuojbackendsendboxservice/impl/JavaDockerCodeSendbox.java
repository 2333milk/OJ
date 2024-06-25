package com.jueye.nchuojbackendsendboxservice.impl;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExecuteMessage;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Component
public class JavaDockerCodeSendbox extends JavaCodeSendboxTemplate {

    public static final Boolean FIRST_INIT = true;
    //时间限制
    private static final long TIME_OUT = 5000L;
    public static void main(String[] args) {
        JavaDockerCodeSendbox javaNativeCodeSendbox = new JavaDockerCodeSendbox();
        ExcuteCodeRequest excuteCodeRequest = new ExcuteCodeRequest();
        excuteCodeRequest.setInputlist(Arrays.asList("1\n2\n","2\n4\n"));
        String code = ResourceUtil.readStr("textcode/unhealthCode/ReadFile.java",StandardCharsets.UTF_8);
        excuteCodeRequest.setCode(code);
        excuteCodeRequest.setLanguage("java");
        ExcuteCodeResponse excuteCodeResponse = javaNativeCodeSendbox.excuteCode(excuteCodeRequest);
        System.out.println(excuteCodeResponse);

    }

    /**
     * docker执行代码返回代码执行结果
     * @param userCodeFile
     * @param inputlist
     * @return
     */
    @Override
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputlist) {
        //创建容器，把文件复制到容器内
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        //拉取镜像
        String image = "openjdk:8-alpine";
        if(FIRST_INIT){
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    super.onNext(item);
                }
            };

            try {
                pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("镜像拉取失败");
                throw new RuntimeException(e);
            }

        }


        //创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100*100*1000L);
        hostConfig.withCpuCount(1L);
        hostConfig.setBinds(new Bind(userCodeFile.getParentFile().getAbsolutePath(),new Volume("/app")));
        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        String containerId = createContainerResponse.getId();

        //查看容器状态
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for(Container container:containerList){
            System.out.println();
        }
        //启动容器
        dockerClient.startContainerCmd(containerId).exec();

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for(String inputArgs:inputlist){
            //执行程序命令  docker exec keen_blackwell java -cp /app Main 1 3
            StopWatch stopWatch = new StopWatch();
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java","-cp","/app","Main"},inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStdin(true)
                    .withAttachStderr(true)
                    .withAttachStdout(true)
                    .exec();
            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            final long[] memory = {0};
            //获取占用内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onNext(Statistics statistics) {
                    memory[0] = Math.max(statistics.getMemoryStats().getUsage(), memory[0]);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            });
            String execId = execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback(){
                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if(streamType.STDERR.equals(streamType)){
                        errorMessage[0] = new String(frame.getPayload());
                    }else {
                        message[0] = new String(frame.getPayload());
                    }
                    super.onNext(frame);
                }
            };
            try {
                //开启定时器
                stopWatch.start();
                //执行程序并限制超时
                dockerClient.execStartCmd(execId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MICROSECONDS);
                //关闭定时器
                stopWatch.stop();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
            executeMessage.setMemory(memory[0]);
            executeMessageList.add(executeMessage);
        }
        return executeMessageList;
    }

    /**
     * 整理docker运行结果
     * @param excuteCodeResponse
     * @param executeMessageList
     */
    @Override
    public void organizeOutput(ExcuteCodeResponse excuteCodeResponse, List<ExecuteMessage> executeMessageList) {
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        long maxMemory = 0;
        excuteCodeResponse.setStatus(1);
        for (ExecuteMessage executeMessage : executeMessageList) {
            if(executeMessage.getExitValue()!=0){
                excuteCodeResponse.setStatus(3);
                excuteCodeResponse.setMessage(executeMessage.getErrorMessage());
                break;
            }
            outputList.add(executeMessage.getMessage());
            maxTime = Math.max(maxTime,executeMessage.getTime());
            maxMemory = Math.max(maxMemory,executeMessage.getMemory());

        }
        excuteCodeResponse.setOutputlist(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory);
        excuteCodeResponse.setJudgeInfo(judgeInfo);
    }
}
