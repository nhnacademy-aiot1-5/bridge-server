package live.ioteatime.bridgeserver.config;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 클라이언트 설정 클래스입니다.
 */
@Configuration
@Slf4j
public class ClientConfig {

    @Bean
    @ConditionalOnProperty(name = "bridge.server.protocol", havingValue = "mqtt")
    public MqttClient mqttClient(
        @Value("${mqtt.server.uri}") String uri,
        @Value("${mqtt.client.id}") String clientId,
        @Value("${mqtt.subscribe.topics}") String[] topics,
        MqttCallback mqttCallback) throws MqttException {

        MqttClient mqttClient = new MqttClient(uri, clientId);
        mqttClient.setCallback(mqttCallback);
        mqttClient.connect();
        log.info("MqttClient has connected to uri '{}'", uri);
        mqttClient.subscribe(topics);
        log.info("MqttClient has subscribed to topics '{}'", Arrays.toString(topics));

        return mqttClient;
    }
}
