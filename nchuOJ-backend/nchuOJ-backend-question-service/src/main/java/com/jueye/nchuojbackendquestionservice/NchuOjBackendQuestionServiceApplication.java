package com.jueye.nchuojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {})
@MapperScan("com.jueye.nchuojbackendquestionservice.mapper")
@EnableScheduling
//开启服务发现客户端
@EnableDiscoveryClient
//开启FeignClients远程调用的客户端
@EnableFeignClients(basePackages = {"com.jueye.nchuojbackendserviceclient.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.jueye")
public class NchuOjBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NchuOjBackendQuestionServiceApplication.class, args);
    }

}
