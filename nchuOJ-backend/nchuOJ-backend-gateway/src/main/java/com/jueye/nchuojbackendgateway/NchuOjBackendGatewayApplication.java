package com.jueye.nchuojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class NchuOjBackendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NchuOjBackendGatewayApplication.class, args);
    }

}
