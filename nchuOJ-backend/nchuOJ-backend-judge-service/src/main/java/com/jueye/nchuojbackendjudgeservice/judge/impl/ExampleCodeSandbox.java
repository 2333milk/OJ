package com.jueye.nchuojbackendjudgeservice.judge.impl;


import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendjudgeservice.judge.CodeSendbox;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.jueye.nchuojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.jueye.nchuojbackendserviceclient.service.SendboxFeignClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实例代码沙箱
 */
@Component
public class ExampleCodeSandbox implements CodeSendbox {
    @Resource
    private SendboxFeignClient sendboxFeignClient;
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        ExcuteCodeResponse response = sendboxFeignClient.executeCode(excuteCodeRequest);
        if(response == null){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR);
        }
        return response;
    }
}
