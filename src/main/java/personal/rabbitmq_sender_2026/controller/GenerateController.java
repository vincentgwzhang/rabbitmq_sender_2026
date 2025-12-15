package personal.rabbitmq_sender_2026.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import personal.rabbitmq_sender_2026.service.RabbitMqService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/send")
public class GenerateController {

    private final RabbitMqService rabbitMqService;

    @PostMapping
    public void send(@RequestParam int num) throws Exception {
        rabbitMqService.sendMessages(num);
    }

}
