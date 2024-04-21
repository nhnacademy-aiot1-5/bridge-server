package org.example.queueingserver.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.example.queueingserver.domain.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MqttCalllback implements MqttCallback {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationContext context;

    @Value("${spring.rabbitmq.template.exchange}")
    private String rabbitMqExchange;
    @Value("${spring.rabbitmq.template.routing-key}")
    private String rabbitMqRoutingKey;

    @Override
    public void connectionLost(Throwable throwable) {
        try {
            MqttClient mqttClient = context.getBean(MqttClient.class);
            if (!mqttClient.isConnected()) {
            mqttClient.reconnect();
            log.info("Reconnect successful");
            }
        } catch (MqttException e) {
            log.error("Connection lost : {}", e.getMessage());
        }
    }
 
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Data data = objectMapper.readValue(mqttMessage.getPayload(), Data.class);
        data.setTopic(s);
        rabbitTemplate.convertAndSend(rabbitMqExchange,rabbitMqRoutingKey,data);
        log.info("Message arrived : {}", data);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.debug("deliveryComplete : {}", iMqttDeliveryToken.getResponse());
    }
}
