package personal.rabbitmq_sender_2026.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMqConfig {

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter jacksonJsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonJsonMessageConverter);

        // broker 确认收到，通常是到达 exchange
        template.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (!ack) {
                // broker 没确认，发送失败或被 broker 拒绝，原因在 cause 里
                log.error("Message NOT acknowledged: {}", cause);
            } else {
                // RabbitMQ broker 已确认收到消息，通常表示消息成功到达 exchange
                log.info("Message acknowledged: {}", correlationData);
            }
        });

        // 没有被退回，通常说明成功路由到队列
        template.setReturnsCallback(returned -> log.error("Message returned: {}", returned.getReplyText()));
        return template;
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange", true, false);
    }

}
