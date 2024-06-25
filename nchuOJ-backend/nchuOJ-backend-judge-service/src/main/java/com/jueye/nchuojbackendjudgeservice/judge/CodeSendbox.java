package com.jueye.nchuojbackendjudgeservice.judge;


import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import org.springframework.stereotype.Component;

@Component
public interface CodeSendbox {

    /**
     * 执行代码
     * @param excuteCodeRequest
     * @return
     */
    ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest);
}
