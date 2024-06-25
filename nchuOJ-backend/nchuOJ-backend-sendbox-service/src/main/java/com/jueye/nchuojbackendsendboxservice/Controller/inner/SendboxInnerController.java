package com.jueye.nchuojbackendsendboxservice.Controller.inner;

import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendsendboxservice.impl.JavaNativeCodeSendbox;
import com.jueye.nchuojbackendserviceclient.service.SendboxFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 用户接口
 *
 
 */

@RestController
@RequestMapping("/inner")
@Slf4j
public class SendboxInnerController implements SendboxFeignClient {
    @Resource
    private JavaNativeCodeSendbox javaNativeCodeSendbox;
    @Override
    @PostMapping("/executeCode")
    public ExcuteCodeResponse executeCode(@RequestBody ExcuteCodeRequest excuteCodeRequest){
        if(excuteCodeRequest==null){
            throw new RuntimeException("请求参数为空");
        }
        log.info("开始判题");
        return javaNativeCodeSendbox.excuteCode(excuteCodeRequest);
    }
}
