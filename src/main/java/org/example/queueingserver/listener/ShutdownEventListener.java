package org.example.queueingserver.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {
    private final MqttClient mqttClient;

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        closeMqttClient();

    }

    private void closeMqttClient() {
        try {
            mqttClient.disconnect();
            mqttClient.close();
            log.info("mqtt client disconnected");
        } catch (MqttException e) {
            //ignore
        }
    }
}
