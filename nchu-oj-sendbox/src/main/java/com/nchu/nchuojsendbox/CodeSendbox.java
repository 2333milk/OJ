package com.nchu.nchuojsendbox;


import com.nchu.nchuojsendbox.model.ExcuteCodeRequest;
import com.nchu.nchuojsendbox.model.ExcuteCodeResponse;

public interface CodeSendbox {

    /**
     * 执行代码
     * @param excuteCodeRequest
     * @return
     */
    ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) throws InterruptedException;
}
