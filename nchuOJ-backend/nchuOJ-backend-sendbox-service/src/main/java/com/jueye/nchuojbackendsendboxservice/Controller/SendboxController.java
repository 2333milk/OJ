package com.jueye.nchuojbackendsendboxservice.Controller;

import com.jueye.nchuojbackendcommon.common.BaseResponse;
import com.jueye.nchuojbackendcommon.common.ResultUtils;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendsendboxservice.factory.CodeboxTemplateFactory;
import com.jueye.nchuojbackendsendboxservice.service.CodeSendbox;
import com.jueye.nchuojbackendsendboxservice.service.impl.JavaNativeCodeSendbox;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/")
public class SendboxController {

    @Resource
    private CodeboxTemplateFactory codeboxTemplateFactory;

    @PostMapping("/executeCode")
    public BaseResponse<ExcuteCodeResponse>  executeCode(@RequestBody ExcuteCodeRequest excuteCodeRequest) {
        if(excuteCodeRequest==null){
            throw new RuntimeException("请求参数为空");
        }
        CodeSendbox codeSendbox = codeboxTemplateFactory.getCodeSendbox(excuteCodeRequest.getLanguage());
        //执行代码
        return ResultUtils.success(codeSendbox.excuteCode(excuteCodeRequest));
    }
}
