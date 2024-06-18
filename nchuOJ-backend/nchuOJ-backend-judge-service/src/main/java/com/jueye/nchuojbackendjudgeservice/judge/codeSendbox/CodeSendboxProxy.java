package com.jueye.nchuojbackendjudgeservice.judge.codeSendbox;

import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CodeSendboxProxy implements CodeSendbox{

    private CodeSendbox codeSendbox;
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        log.info("代码沙箱请求信息:"+excuteCodeRequest.toString());
        ExcuteCodeResponse excuteCodeResponse = codeSendbox.excuteCode(excuteCodeRequest);
        log.info("代码沙箱响应信息:"+excuteCodeResponse.toString());
        return excuteCodeResponse;
    }
}
