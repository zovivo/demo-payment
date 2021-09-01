package vn.vnpay.preprocess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.constant.RabbitConstant;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.service.RabbitMQService;
import vn.vnpay.preprocess.util.CommonUtils;

import java.util.Properties;

@Service(value = "rabbitMQService")
public class RabbitMQServiceImpl implements RabbitMQService {

    private static final Logger logger = LogManager.getLogger(RabbitMQServiceImpl.class);

    @Autowired
    @Qualifier(value = RabbitConstant.RABBIT_TEMPLATE_BEAN)
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier(value = RabbitConstant.RABBITMQ_PROPERTIES_BEAN)
    private Properties rabbitmqProperties;

    @Override
    public ResponseData send(Payment payment) {
        logger.info("send to exchange: {} routingKey: {} payment: {}",
                rabbitmqProperties.getProperty(RabbitConstant.EXCHANGE),
                rabbitmqProperties.getProperty(RabbitConstant.ROUTING_KEY),
                CommonUtils.parseObjectToString(payment));
        ResponseData response = rabbitTemplate.convertSendAndReceiveAsType(
                rabbitmqProperties.getProperty(RabbitConstant.EXCHANGE),
                rabbitmqProperties.getProperty(RabbitConstant.ROUTING_KEY),
                payment, message -> {
                    message.getMessageProperties().setReplyTo(rabbitmqProperties.getProperty(RabbitConstant.REPLY_QUEUE));
                    return message;
                }, new ParameterizedTypeReference<ResponseData>() {
                }
        );
        logger.info("response: {}", CommonUtils.parseObjectToString(response));
        return response;
    }

}
