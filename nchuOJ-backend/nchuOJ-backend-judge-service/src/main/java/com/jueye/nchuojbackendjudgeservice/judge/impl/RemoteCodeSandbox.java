package com.jueye.nchuojbackendjudgeservice.judge.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendjudgeservice.judge.CodeSendbox;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 远程代码沙箱
 */
@Component
public class RemoteCodeSandbox implements CodeSendbox {
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";


    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExcuteCodeResponse.class);

    }
}
