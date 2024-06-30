package com.jueye.nchuojbackendsendboxservice.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExecuteMessage;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendsendboxservice.service.CodeSendbox;
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
public class CodeSendboxTemplate implements CodeSendbox {
    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";


    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String globalCodePathName = USER_DIR+ File.separator+GLOBAL_CODE_DIR_NAME;

    public static final List<String> blackList = Arrays.asList("exec","File");

    public static final WordTree WORD_TREE ;


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
        return  null;
    }

    /**
     *
     * @return
     */
    public void compileCode(ExcuteCodeResponse excuteCodeResponse,File userCodeFile){

    }
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputlist){
        return null;
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
