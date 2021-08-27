package vn.vnpay.preprocess.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Properties;

@Configuration
@RefreshScope
@EnableAutoConfiguration(
        exclude = {RedisAutoConfiguration.class,
        })
public class RedisConfig {

    @Autowired
    @Qualifier(value = "redisProperties")
    private Properties redisProperties;

    @Bean
    public LettucePoolingClientConfiguration lettucePoolingClientConfiguration() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(Integer.valueOf(redisProperties.getProperty("spring.redis.max-idle")));
        poolConfig.setMinIdle(Integer.valueOf(redisProperties.getProperty("spring.redis.min-idle")));
        poolConfig.setMaxWaitMillis(Integer.valueOf(redisProperties.getProperty("spring.redis.max-wait")));
        poolConfig.setMaxTotal(Integer.valueOf(redisProperties.getProperty("spring.redis.max-active")));
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(10))
                .shutdownTimeout(Duration.ZERO)
                .poolConfig(poolConfig)
                .build();
        return lettucePoolingClientConfiguration;
    }

    @Bean
    @Autowired
    public LettuceConnectionFactory redisConnectionFactory(LettucePoolingClientConfiguration lettucePoolingClientConfiguration) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisProperties.getProperty("spring.redis.host"));
        redisConfig.setPort(Integer.valueOf(redisProperties.getProperty("spring.redis.port")));
        redisConfig.setPassword(redisProperties.getProperty("spring.redis.password"));
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
        lettuceConnectionFactory.setShareNativeConnection(false);
        return lettuceConnectionFactory;
    }

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

}