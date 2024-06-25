package com.jueye.nchuojbackendjudgeservice.judge.impl;

import com.jueye.nchuojbackendjudgeservice.judge.CodeSendbox;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

/**
 * 第三方代码沙箱
 */
@Component
public class ThirdPartyCodeSandbox implements CodeSendbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
