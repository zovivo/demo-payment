package vn.vnpay.process.consumer;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import vn.vnpay.process.configuration.realoadable.ReloadablePropertySourceFactory;
import vn.vnpay.process.constant.CustomCode;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.response.ResponseData;
import vn.vnpay.process.service.PaymentService;
import vn.vnpay.process.util.CommonUtils;
import vn.vnpay.process.util.ResponsePreProcessor;

import java.util.Properties;

/**
 * Project: demo-payment
 * Package: vn.vnpay.process.configuration
 * Author: zovivo
 * Date: 8/10/2021
 * Time: 4:11 PM
 * Created with IntelliJ IDEA
 */
@Component
@RefreshScope
@PropertySource(value = "file:configs/process/rabbitmq.properties", factory = ReloadablePropertySourceFactory.class)
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    @Qualifier(value = "rabbitmqProperties")
    private Properties rabbitmqProperties;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public ResponseData receivedMessageAndReply(PaymentModel paymentModel, Message message) {
        ThreadContext.put("tokenKey", paymentModel.getTokenKey());
        logger.info("received from queue: {} Message: {}", queue, message);
        ResponseData responseData;
        try {
            responseData = paymentService.executePayment(paymentModel);
        } catch (CustomException e) {
            logger.warn("custom exception: ", e);
            responseData = responsePreProcessor.buildResponseData(e, e.getCustomCode());
        } catch (RuntimeException e) {
            logger.error("runtime exception: ", e);
            responseData = responsePreProcessor.buildResponseData(e, CustomCode.UNKNOWN_ERROR);
        }
        logger.info("return response: {}", CommonUtils.parseObjectToString(responseData));
        ThreadContext.remove("tokenKey");
        return responseData;
    }

}
