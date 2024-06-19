package com.nchu.nchuojsendbox.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.nchu.nchuojsendbox.CodeSendbox;
import com.nchu.nchuojsendbox.model.ExcuteCodeRequest;
import com.nchu.nchuojsendbox.model.ExcuteCodeResponse;
import com.nchu.nchuojsendbox.model.ExecuteMessage;
import com.nchu.nchuojsendbox.model.JudgeInfo;
import com.nchu.nchuojsendbox.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * java代码沙箱模板
 */
@Component
public class JavaCodeSendboxTemplate implements CodeSendbox {
    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final String USER_DIR = System.getProperty("user.dir");

    private static final String globalCodePathName = USER_DIR+ File.separator+GLOBAL_CODE_DIR_NAME;

    private static final List<String> blackList = Arrays.asList("exec","File");

    private static final WordTree WORD_TREE ;


    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blackList);
    }


//    public static void main(String[] args) {
//        JavaNativeCodeSendbox javaNativeCodeSendbox = new JavaNativeCodeSendbox();
//        ExcuteCodeRequest  excuteCodeRequest = new ExcuteCodeRequest();
//        excuteCodeRequest.setInputlist(Arrays.asList("1\n2\n","2\n4\n"));
//        String code = ResourceUtil.readStr("textcode/unhealthCode/ReadFile.java", StandardCharsets.UTF_8);
//        excuteCodeRequest.setCode(code);
//        excuteCodeRequest.setLanguage("java");
//        ExcuteCodeResponse excuteCodeResponse = javaNativeCodeSendbox.excuteCode(excuteCodeRequest);
//        System.out.println(excuteCodeResponse);
//
//    }

    /**
     * 校验代码
     * @param code
     * @return
     */
    public ExcuteCodeResponse blackWhiteFilter(String code){
        ExcuteCodeResponse excuteCodeResponse = new ExcuteCodeResponse();

        //黑白名单检验代码
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if(foundWord!=null){
            excuteCodeResponse.setMessage("检测到恶意代码");
            excuteCodeResponse.setOutputlist(null);
            excuteCodeResponse.setStatus(3);
            excuteCodeResponse.setJudgeInfo(new JudgeInfo());
        }
        return excuteCodeResponse;
    }

    /**
     * 把用户代码保存为文件
     * @return
     */
    public File saveCodeToFile(String code,String userCodeParentPath){
        //判断全局代码目录是否存在,若不存在则创建
        if(FileUtil.exist(globalCodePathName)){
            FileUtil.mkdir(globalCodePathName);
        }
        //将客户端的代码隔离存放
        String userCodePath = userCodeParentPath + File.separator +GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code,userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    /**
     *
     * @return
     */
    public void compileCode(ExcuteCodeResponse excuteCodeResponse,File userCodeFile){

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
    public List<ExecuteMessage> runCode(File userCodeFile,List<String> inputlist){
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

    /**
     *
     * @param excuteCodeResponse
     * @param executeMessageList
     */
    public void organizeOutput(ExcuteCodeResponse excuteCodeResponse ,List<ExecuteMessage> executeMessageList){
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        excuteCodeResponse.setStatus(1);
        for (ExecuteMessage executeMessage : executeMessageList) {
            if(executeMessage.getExitValue()!=0){
                excuteCodeResponse.setStatus(3);
                excuteCodeResponse.setMessage(executeMessage.getErrorMessage());
                break;
            }
            outputList.add(executeMessage.getMessage());
            maxTime = Math.max(maxTime,executeMessage.getTime());
        }
        excuteCodeResponse.setOutputlist(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(0L);
        excuteCodeResponse.setJudgeInfo(judgeInfo);
    }

    /**
     * 删除文件
     * @param userCodeFile
     * @return
     */
    public boolean cleanFile(File userCodeFile){
        if(userCodeFile.exists()){
            boolean del = FileUtil.del(userCodeFile.getParentFile().getAbsoluteFile());
            return del;
        }
        return true;
    }

    /**
     *
     * @param excuteCodeRequest
     * @return
     */
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        List<String> inputlist = excuteCodeRequest.getInputlist();
        String code = excuteCodeRequest.getCode();
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();

        //黑白名单检验代码
        ExcuteCodeResponse excuteCodeResponse = blackWhiteFilter(code);

        //把用户代码保存为文件
        File userCodeFile = saveCodeToFile(code,userCodeParentPath);

        //编译代码，生成class文件
        compileCode(excuteCodeResponse,userCodeFile);

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
