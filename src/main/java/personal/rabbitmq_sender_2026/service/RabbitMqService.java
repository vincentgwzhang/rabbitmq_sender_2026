package personal.rabbitmq_sender_2026.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import personal.rabbitmq_sender_2026.dto.OrderDto;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public void sendMessages(int num) {
        OrderDto orderDto = Instancio.of(OrderDto.class).create();

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                orderDto,
                message -> {
                    message.getMessageProperties()
                            .setMessageId(UUID.randomUUID().toString());
                    return message;
                },
                new CorrelationData(UUID.randomUUID().toString())
        );

    }
}
