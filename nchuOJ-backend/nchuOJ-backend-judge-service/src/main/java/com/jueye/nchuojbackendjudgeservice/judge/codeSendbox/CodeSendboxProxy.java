package com.jueye.nchuojbackendjudgeservice.judge.codeSendbox;

import com.jueye.nchuojbackendjudgeservice.judge.CodeSendbox;
import com.jueye.nchuojbackendjudgeservice.judge.impl.ExampleCodeSandbox;
import com.jueye.nchuojbackendjudgeservice.judge.impl.RemoteCodeSandbox;
import com.jueye.nchuojbackendjudgeservice.judge.impl.ThirdPartyCodeSandbox;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CodeSendboxProxy {
    @Resource
    private ExampleCodeSandbox exampleCodeSandbox;

    @Resource
    private RemoteCodeSandbox remoteCodeSandbox;

    @Resource
    private ThirdPartyCodeSandbox thirdPartyCodeSandbox;

    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest,String codeSendboxType)  {
        log.info("代码沙箱请求信息:"+excuteCodeRequest.toString());
        ExcuteCodeResponse excuteCodeResponse = null;
        if ("remote".equals(codeSendboxType)){
            excuteCodeResponse = remoteCodeSandbox.excuteCode(excuteCodeRequest);
        }else if("third".equals(codeSendboxType)){
            excuteCodeResponse = thirdPartyCodeSandbox.excuteCode(excuteCodeRequest);
        }else {
            excuteCodeResponse = exampleCodeSandbox.excuteCode(excuteCodeRequest);
        }

        log.info("代码沙箱响应信息:"+excuteCodeResponse.toString());
        return excuteCodeResponse;
    }
}
