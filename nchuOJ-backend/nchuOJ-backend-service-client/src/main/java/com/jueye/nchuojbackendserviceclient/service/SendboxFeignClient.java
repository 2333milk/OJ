package com.jueye.nchuojbackendserviceclient.service;

import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "nchuOJ-backend-sendbox-service",path = "/api/sendbox/inner")
public interface SendboxFeignClient {

    @PostMapping("/executeCode")
    ExcuteCodeResponse executeCode(@RequestBody ExcuteCodeRequest excuteCodeRequest) ;
}
