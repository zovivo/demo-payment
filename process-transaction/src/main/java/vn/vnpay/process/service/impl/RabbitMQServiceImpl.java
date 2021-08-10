package vn.vnpay.process.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.util.CommonUtils;

@Service(value = "rabbitMQService")
public class RabbitMQServiceImpl {

    private static final Logger logger = LogManager.getLogger(RabbitMQServiceImpl.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    public void send(PaymentModel payment) {
        rabbitTemplate.convertAndSend(exchange, routingkey, payment);
        logger.info("send payment: {}", CommonUtils.parseObjectToString(payment));
    }

}
