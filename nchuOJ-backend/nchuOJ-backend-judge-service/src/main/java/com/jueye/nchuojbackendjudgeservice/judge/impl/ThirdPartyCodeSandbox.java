package com.jueye.nchuojbackendjudgeservice.judge.impl;

import com.jueye.nchuojbackendjudgeservice.judge.codeSendbox.CodeSendbox;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;

/**
 * 第三方代码沙箱
 */
public class ThirdPartyCodeSandbox implements CodeSendbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
