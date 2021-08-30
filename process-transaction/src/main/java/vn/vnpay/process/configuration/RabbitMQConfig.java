package vn.vnpay.process.configuration;

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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@RefreshScope
public class RabbitMQConfig {

    private static final Logger logger = LogManager.getLogger(RabbitMQConfig.class);

    @Autowired
    @Qualifier(value = "rabbitmqProperties")
    private Properties rabbitmqProperties;

    @Bean(name = "pooledChannelConnectionFactory")
    @Primary
    PooledChannelConnectionFactory pooledChannelConnectionFactory() throws Exception {
        ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
        rabbitConnectionFactory.setHost(rabbitmqProperties.getProperty("spring.rabbitmq.host"));
        rabbitConnectionFactory.setUsername(rabbitmqProperties.getProperty("spring.rabbitmq.username"));
        rabbitConnectionFactory.setPassword(rabbitmqProperties.getProperty("spring.rabbitmq.password"));
        rabbitConnectionFactory.setChannelRpcTimeout(Integer.valueOf(rabbitmqProperties.getProperty("spring.rabbitmq.channel-rpc-timeout")));
        rabbitConnectionFactory.setConnectionTimeout(Integer.valueOf(rabbitmqProperties.getProperty("spring.rabbitmq.connection-timeout")));
        rabbitConnectionFactory.setRequestedChannelMax(Integer.valueOf(rabbitmqProperties.getProperty("spring.rabbitmq.requested-channel-max")));
        PooledChannelConnectionFactory pooledChannelConnectionFactory = new PooledChannelConnectionFactory(rabbitConnectionFactory);
        return pooledChannelConnectionFactory;
    }

    @Bean(name = "rabbitTemplate")
    @Primary
    RabbitTemplate rabbitTemplate(@Autowired @Qualifier("pooledChannelConnectionFactory") PooledChannelConnectionFactory pooledChannelConnectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(pooledChannelConnectionFactory);
        template.setExchange(rabbitmqProperties.getProperty("spring.rabbitmq.exchange"));
        template.setRoutingKey(rabbitmqProperties.getProperty("spring.rabbitmq.routingkey"));
        template.setMessageConverter(messageConverter);
        template.setReplyTimeout(Long.valueOf(rabbitmqProperties.getProperty("spring.rabbitmq.template.reply-timeout")));
        return template;
    }

    @Bean(name = "rabbitQueue")
    Queue queue() {
        return new Queue(rabbitmqProperties.getProperty("spring.rabbitmq.queue"), false);
    }

    @Bean(name = "rabbitExchange")
    DirectExchange exchange() {
        return new DirectExchange(rabbitmqProperties.getProperty("spring.rabbitmq.exchange"));
    }

    @Bean
    @Autowired
    Binding binding(@Qualifier(value = "rabbitQueue") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitmqProperties.getProperty("spring.rabbitmq.routingkey"));
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
