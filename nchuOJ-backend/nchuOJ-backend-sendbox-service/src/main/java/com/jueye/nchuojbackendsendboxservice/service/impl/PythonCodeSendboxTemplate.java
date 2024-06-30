package com.jueye.nchuojbackendsendboxservice.service.impl;

import cn.hutool.core.io.FileUtil;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExecuteMessage;
import com.jueye.nchuojbackendsendboxservice.utils.ProcessUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PythonCodeSendboxTemplate extends CodeSendboxTemplate{
    private static PythonCodeSendboxTemplate instance;

    private PythonCodeSendboxTemplate(){}

    public static PythonCodeSendboxTemplate getInstance(){
        if(instance==null){
            synchronized (PythonCodeSendboxTemplate.class){
                if(instance==null){
                    instance = new PythonCodeSendboxTemplate();
                }
            }
        }
        return instance;
    }


    @Override
    public File saveCodeToFile(String code, String userCodeParentPath) {
        //判断全局代码目录是否存在,若不存在则创建
        if(FileUtil.exist(globalCodePathName)){
            FileUtil.mkdir(globalCodePathName);
        }
        //将客户端的代码隔离存放
        String userCodePath = userCodeParentPath + File.separator +"Main.py";
        File userCodeFile = FileUtil.writeString(code,userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }


    @Override
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputlist) {
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for(String inputArgs: inputlist){
            String runCmd = String.format("python %s %s",userCodeFile.getParentFile().getAbsoluteFile(),inputArgs);
            //命令行传参
            ExecuteMessage runExecuteMessage = ProcessUtils.runProcess(runCmd);
            //交互传参
            //ExecuteMessage runExecuteMessage = ProcessUtils.runInteractProcess(runCmd,inputArgs);
            executeMessageList.add(runExecuteMessage);
        }
        return executeMessageList;
    }

    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        List<String> inputlist = excuteCodeRequest.getInputlist();
        String code = excuteCodeRequest.getCode();
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();

        //黑白名单检验代码
        ExcuteCodeResponse excuteCodeResponse = blackWhiteFilter(code);

        //把用户代码保存为文件
        File userCodeFile = saveCodeToFile(code,userCodeParentPath);
        //执行代码
        List<ExecuteMessage> executeMessageList = runCode(userCodeFile,inputlist);

        //整理输出
        organizeOutput(excuteCodeResponse,executeMessageList);

        //文件清理
        boolean b = cleanFile(userCodeFile);
        if(!b){
            System.out.println("delete error");
        }
        return excuteCodeResponse;
    }
}
