package live.ioteatime.bridgeserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 관련 설정 클래스입니다.
 */
@Configuration
@Slf4j
public class AmqpConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        log.info("RabbitTemplate: {}", connectionFactory.getHost());
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }
}
