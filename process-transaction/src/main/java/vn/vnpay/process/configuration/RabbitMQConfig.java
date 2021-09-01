package vn.vnpay.process.configuration;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vn.vnpay.process.constant.RabbitConstant;

import java.util.Properties;

@Configuration
@RefreshScope
public class RabbitMQConfig {

    private static final Logger logger = LogManager.getLogger(RabbitMQConfig.class);

    @Autowired
    @Qualifier(value = RabbitConstant.RABBITMQ_PROPERTIES_BEAN)
    private Properties rabbitmqProperties;

    @Bean(name = "pooledChannelConnectionFactory")
    @Primary
    PooledChannelConnectionFactory pooledChannelConnectionFactory() throws Exception {
        ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
        rabbitConnectionFactory.setHost(rabbitmqProperties.getProperty(RabbitConstant.HOST));
        rabbitConnectionFactory.setUsername(rabbitmqProperties.getProperty(RabbitConstant.USERNAME));
        rabbitConnectionFactory.setPassword(rabbitmqProperties.getProperty(RabbitConstant.PASSWORD));
        rabbitConnectionFactory.setChannelRpcTimeout(Integer.valueOf(rabbitmqProperties.getProperty(RabbitConstant.CHANNEL_RPC_TIMEOUT)));
        rabbitConnectionFactory.setConnectionTimeout(Integer.valueOf(rabbitmqProperties.getProperty(RabbitConstant.CONNECTION_TIMEOUT)));
        rabbitConnectionFactory.setRequestedChannelMax(Integer.valueOf(rabbitmqProperties.getProperty(RabbitConstant.REQUESTED_CHANNEL_MAX)));
        PooledChannelConnectionFactory pooledChannelConnectionFactory = new PooledChannelConnectionFactory(rabbitConnectionFactory);
        return pooledChannelConnectionFactory;
    }

    @Bean(name = "rabbitTemplate")
    @Primary
    RabbitTemplate rabbitTemplate(@Autowired @Qualifier("pooledChannelConnectionFactory") PooledChannelConnectionFactory pooledChannelConnectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(pooledChannelConnectionFactory);
        template.setExchange(rabbitmqProperties.getProperty(RabbitConstant.EXCHANGE));
        template.setRoutingKey(rabbitmqProperties.getProperty(RabbitConstant.ROUTING_KEY));
        template.setMessageConverter(messageConverter);
        template.setReplyTimeout(Long.valueOf(rabbitmqProperties.getProperty(RabbitConstant.REPLY_TIMEOUT)));
        return template;
    }

    @Bean(name = "rabbitQueue")
    Queue queue() {
        return new Queue(rabbitmqProperties.getProperty(RabbitConstant.QUEUE), false);
    }

    @Bean(name = "rabbitExchange")
    DirectExchange exchange() {
        return new DirectExchange(rabbitmqProperties.getProperty(RabbitConstant.QUEUE));
    }

    @Bean
    @Autowired
    Binding binding(@Qualifier(value = "rabbitQueue") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitmqProperties.getProperty(RabbitConstant.ROUTING_KEY));
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
