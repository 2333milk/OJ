package com.jueye.nchuojbackendsendboxservice.service.impl;

import cn.hutool.core.io.FileUtil;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExecuteMessage;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendsendboxservice.utils.ProcessUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CCodeSendboxTemplate extends CodeSendboxTemplate{
    private static CCodeSendboxTemplate instance;

    private CCodeSendboxTemplate(){}

    public static CCodeSendboxTemplate getInstance(){
        if(instance==null){
            synchronized (CCodeSendboxTemplate.class){
                if(instance==null){
                    instance = new CCodeSendboxTemplate();
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
        String userCodePath = userCodeParentPath + File.separator +"Main.cpp";
        File userCodeFile = FileUtil.writeString(code,userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    @Override
    public void compileCode(ExcuteCodeResponse excuteCodeResponse, File userCodeFile) {
        String compileCmd = String.format("gcc -o %s %s", userCodeFile.getAbsolutePath().replace(".c", ""), userCodeFile.getAbsolutePath());
        ExecuteMessage compileExecuteMessage = ProcessUtils.runProcess(compileCmd);
        if (compileExecuteMessage.getExitValue() != 0) {
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
            String runCmd = String.format("./%s %s", userCodeFile.getParentFile().getAbsoluteFile(), inputArgs); // 假设可执行文件在当前目录下
            ExecuteMessage runExecuteMessage = ProcessUtils.runProcess(runCmd); // 这里可能需要修改ProcessUtils以支持输入
            executeMessageList.add(runExecuteMessage);
        }
        return executeMessageList;
    }
}
