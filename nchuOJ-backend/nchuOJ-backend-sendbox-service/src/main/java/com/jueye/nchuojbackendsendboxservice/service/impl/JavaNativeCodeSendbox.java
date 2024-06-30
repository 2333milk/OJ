package com.jueye.nchuojbackendsendboxservice.service.impl;

import cn.hutool.core.io.FileUtil;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExecuteMessage;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendsendboxservice.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * java原生实现
 */
@Component
public class JavaNativeCodeSendbox extends CodeSendboxTemplate {
    private static JavaNativeCodeSendbox instance;

    private JavaNativeCodeSendbox(){}

    public static JavaNativeCodeSendbox getInstance(){
        if(instance==null){
            synchronized (JavaNativeCodeSendbox.class){
                if(instance==null){
                    instance = new JavaNativeCodeSendbox();
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
        String userCodePath = userCodeParentPath + File.separator +"Main.java";
        File userCodeFile = FileUtil.writeString(code,userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    @Override
    public void compileCode(ExcuteCodeResponse excuteCodeResponse, File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s",userCodeFile.getAbsoluteFile());
        ExecuteMessage compileExecuteMessage = ProcessUtils.runProcess(compileCmd);
        if(compileExecuteMessage.getExitValue()!=0){
            excuteCodeResponse.setMessage(compileExecuteMessage.getErrorMessage());
            excuteCodeResponse.setOutputlist(null);
            excuteCodeResponse.setStatus(3);
            excuteCodeResponse.setJudgeInfo(new JudgeInfo());
            throw new RuntimeException("编译错误");
        }
    }

    @Override
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputlist) {
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for(String inputArgs: inputlist){
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s",userCodeFile.getParentFile().getAbsoluteFile(),inputArgs);
            //命令行传参
            ExecuteMessage runExecuteMessage = ProcessUtils.runProcess(runCmd);
            //交互传参
            //ExecuteMessage runExecuteMessage = ProcessUtils.runInteractProcess(runCmd,inputArgs);
            executeMessageList.add(runExecuteMessage);
        }
        return executeMessageList;
    }
}
