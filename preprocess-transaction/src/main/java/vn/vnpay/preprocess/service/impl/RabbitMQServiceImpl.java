package vn.vnpay.preprocess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.response.ResponseData;
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

    @Value("${spring.rabbitmq.reply-queue}")
    private String replyQueue;

    @Override
    public ResponseData send(Payment payment) {
        logger.info("send to exchange: {} routingKey: {} payment: {}", exchange, routingkey, CommonUtils.parseObjectToString(payment));
        ResponseData response = rabbitTemplate.convertSendAndReceiveAsType(exchange, routingkey, payment, message -> {
                    message.getMessageProperties().setReplyTo(replyQueue);
                    return message;
                }, new ParameterizedTypeReference<ResponseData>() {
                }
        );
        logger.info("response: {} from replyQueue: {}", CommonUtils.parseObjectToString(response), replyQueue);
        return response;
    }

}
