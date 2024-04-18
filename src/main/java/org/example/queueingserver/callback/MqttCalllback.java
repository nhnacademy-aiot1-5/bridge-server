package org.example.queueingserver.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;
import org.example.queueingserver.domain.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MqttCalllback implements MqttCallback {
    private static final Logger log = LoggerFactory.getLogger(MqttCalllback.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final MqttClient mqttClient;

    @Value("${rabbitmq.exchange}")
    private String rabbitMqExchange;
    @Value("${rabbitmq.routing-key}")
    String rabbitMqRoutingKey;

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("Connection lost : {}", throwable.getMessage());
        try {
            mqttClient.reconnect();
            log.info("Reconnect successful");
        } catch (MqttException e) {
            log.error("Connection lost : {}", e.getMessage());
        }
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Data data = objectMapper.readValue(mqttMessage.getPayload(), Data.class);
        data.setTopic(s);
        rabbitTemplate.convertAndSend(rabbitMqExchange,rabbitMqRoutingKey,data);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.debug("deliveryComplete : {}", iMqttDeliveryToken);
        try {
            mqttClient.disconnect();
            mqttClient.connect();
            log.info("MQTT Client disconnected");
        } catch (MqttException e) {
            log.error("MQTT Client disconnect failed : {}", e.getMessage());
        }

    }
}
