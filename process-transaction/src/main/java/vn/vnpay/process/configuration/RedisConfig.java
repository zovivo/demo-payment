package vn.vnpay.process.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import vn.vnpay.process.constant.RedisConstant;

import java.time.Duration;
import java.util.Properties;

@Configuration
@RefreshScope
@EnableAutoConfiguration(
        exclude = {RedisAutoConfiguration.class,
        })
public class RedisConfig {

    @Autowired
    @Qualifier(value = RedisConstant.REDIS_PROPERTIES_BEAN)
    private Properties redisProperties;

    @Bean
    public LettucePoolingClientConfiguration lettucePoolingClientConfiguration() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(Integer.valueOf(redisProperties.getProperty(RedisConstant.MAX_IDLE)));
        poolConfig.setMinIdle(Integer.valueOf(redisProperties.getProperty(RedisConstant.MIN_IDLE)));
        poolConfig.setMaxWaitMillis(Integer.valueOf(redisProperties.getProperty(RedisConstant.MAX_WAIT)));
        poolConfig.setMaxTotal(Integer.valueOf(redisProperties.getProperty(RedisConstant.MAX_ACTIVE)));
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
        redisConfig.setHostName(redisProperties.getProperty(RedisConstant.HOST));
        redisConfig.setPort(Integer.valueOf(redisProperties.getProperty(RedisConstant.PORT)));
        redisConfig.setPassword(redisProperties.getProperty(RedisConstant.PASSWORD));
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
        lettuceConnectionFactory.setShareNativeConnection(false);
        return lettuceConnectionFactory;
    }

    @Bean(RedisConstant.REDIS_TEMPLATE_BEAN)
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

}
