package live.ioteatime.bridgeserver;

import live.ioteatime.bridgeserver.client.ModbusClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class BridgeServerApplicationTests {

    @MockBean
    MqttClient mqttClient;

    @MockBean
    ModbusClient modbusClient;

    @Test
    void contextLoads() {
    }
}
