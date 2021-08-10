package vn.vnpay.preprocess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.model.response.ResponseData;
import vn.vnpay.preprocess.service.RabbitMQService;
import vn.vnpay.preprocess.util.CommonUtils;

@Service(value = "rabbitMQService")
public class RabbitMQServiceImpl implements RabbitMQService {

    private static final Logger logger = LogManager.getLogger(RabbitMQServiceImpl.class);

    @Autowired
    @Qualifier(value = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Override
    public void send(Payment payment) {
        Object response = rabbitTemplate.convertSendAndReceiveAsType(exchange, routingkey, payment, new ParameterizedTypeReference<ResponseData>(){});
        logger.info("send payment: {}", CommonUtils.parseObjectToString(payment));
    }

}