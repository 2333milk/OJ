package com.jueye.nchuojbackendsendboxservice.Controller;

import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendsendboxservice.impl.JavaNativeCodeSendbox;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
public class SendboxController {
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";
    @Resource
    private JavaNativeCodeSendbox javaNativeCodeSendbox;


    @PostMapping("/executeCode")
    public ExcuteCodeResponse executeCode(@RequestBody ExcuteCodeRequest excuteCodeRequest, HttpServletRequest request,
                                          HttpServletResponse response){
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if(!AUTH_REQUEST_SECRET.equals(authHeader)){
            response.setStatus(403);
            return null;
        }
        if(excuteCodeRequest==null){
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSendbox.excuteCode(excuteCodeRequest);
    }
}
