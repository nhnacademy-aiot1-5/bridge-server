package org.example.queueingserver.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Slf4j
public class MqttConfig {
    @Bean
    public MqttClient mqttClient(
            @Value("${mqtt.server.uri}") String uri,
            @Value("${mqtt.client.id}") String clientId,
            @Value("${mqtt.subscribe.topic}") String topic,
            MqttCallback mqttCallback) throws MqttException {

        try {
            MqttClient mqttClient = new MqttClient(uri, clientId);
            mqttClient.setCallback(mqttCallback);
            mqttClient.connect();
            log.info("MqttClient has connected to uri '{}'", uri);
            mqttClient.subscribe(topic);
            log.info("MqttClient has subscribed to topic '{}'", topic);

            return mqttClient;
        }
        catch (MqttException e) {
            log.error("MqttException caught while subscribing to topic '{}', {}", topic, e.getMessage());
        }
        return null;
    }
}
