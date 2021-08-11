package vn.vnpay.process.consumer;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.model.response.ResponseData;
import vn.vnpay.process.service.PaymentService;
import vn.vnpay.process.util.CommonUtils;

/**
 * Project: demo-payment
 * Package: vn.vnpay.process.configuration
 * Author: zovivo
 * Date: 8/10/2021
 * Time: 4:11 PM
 * Created with IntelliJ IDEA
 */
@Component
@PropertySource(value = "classpath:application.properties")
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private PaymentService paymentService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    @SendTo(value = "")
    public ResponseData receivedMessageAndReply(PaymentModel paymentModel, Message message) throws CustomException {
        logger.info("Received Message From RabbitMQ: " + message);
        ThreadContext.put("tokenKey", paymentModel.getTokenKey());
        ResponseData responseData = paymentService.executePayment(paymentModel);
        logger.info("return response: " + CommonUtils.parseObjectToString(responseData));
        return responseData;
    }

}
