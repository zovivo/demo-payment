package vn.vnpay.preprocess.configuration.realoadable;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.vnpay.preprocess.constant.MinioConstant;
import vn.vnpay.preprocess.constant.RabbitConstant;

import java.io.File;
import java.util.Properties;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/18/2021
 * Created with IntelliJ IDEA
 */

@Configuration
@EnableScheduling
public class ReloadablePropertySourceConfig {

    @Bean("rabbitmqPropertiesConfiguration")
    @ConditionalOnProperty(name = "spring.rabbitmq-config.location", matchIfMissing = false)
    public PropertiesConfiguration rabbitmqPropertiesConfiguration(
            @Value("${spring.rabbitmq-config.location}") String path,
            @Value("${spring.properties.refreshDelay}") long refreshDelay) throws Exception {
        PropertiesConfiguration configuration = new PropertiesConfiguration(new File(path).getCanonicalPath());
        FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
        fileChangedReloadingStrategy.setRefreshDelay(refreshDelay);
        configuration.setReloadingStrategy(fileChangedReloadingStrategy);
        return configuration;
    }

    @Bean(RabbitConstant.RABBITMQ_PROPERTIES_BEAN)
    @ConditionalOnBean(name = "rabbitmqPropertiesConfiguration")
    @RefreshScope
    public Properties rabbitmqProperties(@Autowired @Qualifier(value = "rabbitmqPropertiesConfiguration") PropertiesConfiguration propertiesConfiguration) throws Exception {
        ReloadableProperties properties = new ReloadableProperties(propertiesConfiguration);
        return properties;
    }

    @Bean("redisPropertiesConfiguration")
    @ConditionalOnProperty(name = "spring.redis-config.location", matchIfMissing = false)
    public PropertiesConfiguration redisPropertiesConfiguration(
            @Value("${spring.redis-config.location}") String path,
            @Value("${spring.properties.refreshDelay}") long refreshDelay) throws Exception {
        PropertiesConfiguration configuration = new PropertiesConfiguration(new File(path).getCanonicalPath());
        FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
        fileChangedReloadingStrategy.setRefreshDelay(refreshDelay);
        configuration.setReloadingStrategy(fileChangedReloadingStrategy);
        return configuration;
    }

    @Bean("redisProperties")
    @ConditionalOnBean(name = "redisPropertiesConfiguration")
    @RefreshScope
    public Properties redisProperties(@Autowired @Qualifier(value = "redisPropertiesConfiguration") PropertiesConfiguration propertiesConfiguration) throws Exception {
        ReloadableProperties properties = new ReloadableProperties(propertiesConfiguration);
        return properties;
    }

    @Bean("minioPropertiesConfiguration")
    @ConditionalOnProperty(name = "spring.minio-config.location", matchIfMissing = false)
    public PropertiesConfiguration minioPropertiesConfiguration(
            @Value("${spring.minio-config.location}") String path,
            @Value("${spring.properties.refreshDelay}") long refreshDelay) throws Exception {
        PropertiesConfiguration configuration = new PropertiesConfiguration(new File(path).getCanonicalPath());
        FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
        fileChangedReloadingStrategy.setRefreshDelay(refreshDelay);
        configuration.setReloadingStrategy(fileChangedReloadingStrategy);
        return configuration;
    }

    @Bean(MinioConstant.MINIO_PROPERTIES_BEAN)
    @ConditionalOnBean(name = "minioPropertiesConfiguration")
    @RefreshScope
    public Properties minioProperties(@Autowired @Qualifier(value = "minioPropertiesConfiguration") PropertiesConfiguration propertiesConfiguration) throws Exception {
        ReloadableProperties properties = new ReloadableProperties(propertiesConfiguration);
        return properties;
    }
}
