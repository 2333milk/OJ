package com.jueye.nchuojbackendjudgeservice.mq;

import com.jueye.nchuojbackendserviceclient.service.JudgeFeignClient;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class JudgeMessageConsumer {
    // 指定程序监听的消息队列和确认机制
    @Resource
    private JudgeFeignClient judgeFeignClient;

    @SneakyThrows
    @RabbitListener(queues = {"code_queue"},ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag){
        log.info("receiveMessage message = {}",message);
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeFeignClient.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e){
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
