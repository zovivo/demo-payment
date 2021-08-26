package vn.vnpay.preprocess.configuration;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class RabbitMQConfig {

    private static final Logger logger = LogManager.getLogger(RabbitMQConfig.class);

    @Value("${spring.rabbitmq.host}")
    private String hostName;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String userName;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    @Value("${spring.rabbitmq.reply-queue}")
    private String replyQueueName;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    @Bean(name = "pooledChannelConnectionFactory")
    PooledChannelConnectionFactory pooledChannelConnectionFactory() throws Exception {
        ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
        rabbitConnectionFactory.setHost(hostName);
        rabbitConnectionFactory.setUsername(userName);
        rabbitConnectionFactory.setPassword(password);
        PooledChannelConnectionFactory pcf = new PooledChannelConnectionFactory(rabbitConnectionFactory);
        return pcf;
    }

    @Bean(name = "rabbitTemplate")
    RabbitTemplate rabbitTemplate(PooledChannelConnectionFactory pooledChannelConnectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(pooledChannelConnectionFactory);
        template.setExchange(exchange);
        template.setRoutingKey(routingKey);
        template.setMessageConverter(messageConverter);
        template.setReplyTimeout(10000l);
        return template;
    }

    @Bean(name = "rabbitQueue")
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean(name = "replyQueue")
    Queue replyQueue() {
        return new Queue(replyQueueName, true);
    }

    @Bean(name = "rabbitExchange")
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    @Autowired
    Binding binding(@Qualifier(value = "rabbitQueue") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
