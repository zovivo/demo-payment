package vn.vnpay.preprocess.constant;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.constant
 * Author: zovivo
 * Date: 9/1/2021
 * Created with IntelliJ IDEA
 */
public class RabbitConstant {

    public static final String HOST = "spring.rabbitmq.host";
    public static final String PORT = "spring.rabbitmq.port";
    public static final String USERNAME = "spring.rabbitmq.username";
    public static final String PASSWORD = "spring.rabbitmq.password";
    public static final String EXCHANGE = "spring.rabbitmq.exchange";
    public static final String QUEUE = "spring.rabbitmq.queue";
    public static final String ROUTING_KEY = "spring.rabbitmq.routingkey";
    public static final String REPLY_QUEUE = "spring.rabbitmq.reply-queue";
    public static final String CHANNEL_RPC_TIMEOUT = "spring.rabbitmq.channel-rpc-timeout";
    public static final String CONNECTION_TIMEOUT = "spring.rabbitmq.connection-timeout";
    public static final String REQUESTED_CHANNEL_MAX = "spring.rabbitmq.requested-channel-max";
    public static final String REPLY_TIMEOUT = "spring.rabbitmq.template.reply-timeout";
    public static final String RABBITMQ_PROPERTIES_BEAN = "rabbitmqProperties";
    public static final String RABBIT_TEMPLATE_BEAN = "rabbitTemplate";

}
