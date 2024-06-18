package com.jueye.nchuojbackendquestionservice.Mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class InitRabbitMQMain {
    public static void doInit(){
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "code_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            //创建队列，随机分配一个队列名称
            String queueName = "code_queue";
            channel.queueDeclare(queueName,true,false,false,null);
            channel.queueBind(queueName,EXCHANGE_NAME,"my_routingKey");
        }catch (Exception e){

        }
    }
    public static void main(String[] args) {
        doInit();
    }
}
