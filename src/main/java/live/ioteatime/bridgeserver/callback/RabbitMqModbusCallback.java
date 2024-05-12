package live.ioteatime.bridgeserver.callback;

import java.util.function.BiConsumer;
import live.ioteatime.bridgeserver.domain.Data;
import live.ioteatime.bridgeserver.domain.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ에 데이터를 전송하는 모드버스 콜백 클래스입니다.
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "bridge.server.protocol", havingValue = "modbus")
public class RabbitMqModbusCallback implements BiConsumer<PlcReadResponse, Throwable> {

    private final String rabbitMqRoutingKey;
    private final String rabbitMqExchange;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqModbusCallback(
        @Value("${spring.rabbitmq.template.routing-key}") String rabbitMqRoutingKey,
        @Value("${spring.rabbitmq.template.exchange}") String rabbitMqExchange,
        RabbitTemplate rabbitTemplate) {

        this.rabbitMqRoutingKey = rabbitMqRoutingKey;
        this.rabbitMqExchange = rabbitMqExchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void accept(PlcReadResponse response, Throwable throwable) {
        response.getTagNames()
                .stream()
                .map(name -> new Data(Protocol.MODBUS, name, System.currentTimeMillis(), response.getFloat(name)))
                .forEach(this::sendToRabbitMq);
    }

    private void sendToRabbitMq(Data data) {
        rabbitTemplate.convertAndSend(rabbitMqExchange, rabbitMqRoutingKey, data);
        log.debug("Message arrived : {}", data);
    }
}
