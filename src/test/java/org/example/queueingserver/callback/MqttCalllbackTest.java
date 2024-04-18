package org.example.queueingserver.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.example.queueingserver.domain.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MqttCalllbackTest {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private MqttClient mqttClient;

    @Mock
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange}")
    private String rabbitMqExchange;

    @Value("${rabbitmq.routing-key}")
    private String rabbitMqRoutingKey;

    @InjectMocks
    private MqttCalllback mqttCalllback;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void connectionLost() throws MqttException {
        Throwable throwable = new Exception("Connection lost");
        mqttCalllback.connectionLost(throwable);
        verify(mqttClient).reconnect();
    }

    @Test
    void messageArrived() throws Exception {
        String topic = "asdasda";
        byte[] payload = "asdasd".getBytes();
        MqttMessage mqttMessage = new MqttMessage(payload);
        Data data = new Data();
        data.setTopic(topic);

        when(objectMapper.readValue(payload, Data.class)).thenReturn(data);

        mqttCalllback.messageArrived(topic, mqttMessage);

        verify(objectMapper).readValue(payload, Data.class);
        verify(rabbitTemplate).convertAndSend(rabbitMqExchange, rabbitMqRoutingKey, data);
    }

    @Test
    void deliveryComplete() throws MqttException {
        IMqttDeliveryToken iMqttDeliveryToken = new MqttDeliveryToken("asdas");

        mqttCalllback.deliveryComplete(iMqttDeliveryToken);
        verify(mqttClient).disconnect();
        verify(mqttClient).connect();
    }
}