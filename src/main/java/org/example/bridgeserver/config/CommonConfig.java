package org.example.bridgeserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 공용 컴포넌트 관련 설정 클래스입니다.
 */
@Configuration
public class CommonConfig {

    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }
}
