package live.ioteatime.bridgeserver.callback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import live.ioteatime.bridgeserver.domain.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

class RabbitMqProducerCallbackTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private MqttClient mqttClient;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ApplicationContext applicationContextMock;
    @InjectMocks
    private RabbitMqProducerCallback rabbitMqProducerCallback;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void connectionLost() throws MqttException {
        Throwable throwable = mock(Throwable.class);
        String errorMessage = "Connection lost error message";
        when(throwable.getMessage()).thenReturn(errorMessage);
        when(applicationContextMock.getBean(MqttClient.class)).thenReturn(mqttClient);

        rabbitMqProducerCallback.connectionLost(throwable);

        verify(mqttClient).reconnect();
    }

    @Test
    void messageArrived() throws Exception {
        String topic = "asdasda";
        byte[] payload = "asdasd".getBytes();
        MqttMessage mqttMessage = new MqttMessage(payload);
        Data data = createTestData();
        data.setId(topic);
        when(objectMapper.readValue(payload, Data.class)).thenReturn(data);

        rabbitMqProducerCallback.messageArrived(topic, mqttMessage);

        verify(objectMapper).readValue(payload, Data.class);
        verify(rabbitTemplate).convertAndSend(any(), any(), eq(data));
    }

    private Data createTestData() throws Exception {
        Constructor<Data> constructor = Data.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        return constructor.newInstance();
    }

    @Test
    void deliveryComplete() {
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        Logger logger = (Logger) LoggerFactory.getLogger(RabbitMqProducerCallback.class);
        logger.addAppender(appender);
        IMqttDeliveryToken iMqttDeliveryToken = new MqttDeliveryToken("asdas");

        rabbitMqProducerCallback.deliveryComplete(iMqttDeliveryToken);

        String message = appender.list.get(0).getMessage();
        assertThat(message).startsWith("deliveryComplete");
    }
}
