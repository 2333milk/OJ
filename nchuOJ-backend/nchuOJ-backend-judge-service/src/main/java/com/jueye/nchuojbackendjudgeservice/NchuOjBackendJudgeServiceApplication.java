package com.jueye.nchuojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {})
@EnableScheduling
//开启服务发现客户端
@EnableDiscoveryClient
//开启FeignClients远程调用的客户端
@EnableFeignClients(basePackages = {"com.jueye.nchuojbackendserviceclient.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.jueye")
public class NchuOjBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NchuOjBackendJudgeServiceApplication.class, args);
    }

}
