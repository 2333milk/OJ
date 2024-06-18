package com.jueye.nchuojbackendjudgeservice.judge.impl;


import com.jueye.nchuojbackendjudgeservice.judge.codeSendbox.CodeSendbox;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.jueye.nchuojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 实例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSendbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        List<String> inputlist = excuteCodeRequest.getInputlist();
        ExcuteCodeResponse excuteCodeResponse = new ExcuteCodeResponse();
        excuteCodeResponse.setOutputlist(inputlist);
        excuteCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        excuteCodeResponse.setJudgeInfo(judgeInfo);
        excuteCodeResponse.setMessage("测试成功");
        return excuteCodeResponse;
    }
}
