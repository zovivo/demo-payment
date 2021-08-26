package vn.vnpay.preprocess.configuration.realoadable;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

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

    private ConfigurableEnvironment env;

    public ReloadablePropertySourceConfig(@Autowired ConfigurableEnvironment env) {
        this.env = env;
    }

    @Bean(name = "propertiesConfiguration")
    @ConditionalOnProperty(name = "spring.config.location", matchIfMissing = false)
    public PropertiesConfiguration redisPropertiesConfiguration(
            @Value("${spring.config.location}") String path,
            @Value("${spring.properties.refreshDelay}") long refreshDelay) throws Exception {
        PropertiesConfiguration configuration = new PropertiesConfiguration(new File(path).getCanonicalPath());
        FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
        fileChangedReloadingStrategy.setRefreshDelay(refreshDelay);
        configuration.setReloadingStrategy(fileChangedReloadingStrategy);
        return configuration;
    }

    @Bean(value = "rabbitmqPropertiesConfiguration")
    @ConditionalOnProperty(name = "spring.config.location", matchIfMissing = false)
    public PropertiesConfiguration rabbitMQPropertiesConfiguration(
            @Value("${spring.rabbitmq-config.location}") String path,
            @Value("${spring.properties.refreshDelay}") long refreshDelay) throws Exception {
        PropertiesConfiguration configuration = new PropertiesConfiguration(new File(path).getCanonicalPath());
        FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
        fileChangedReloadingStrategy.setRefreshDelay(refreshDelay);
        configuration.setReloadingStrategy(fileChangedReloadingStrategy);
        return configuration;
    }

    @Bean
    @ConditionalOnBean(PropertiesConfiguration.class)
    @Primary
    public Properties properties(PropertiesConfiguration propertiesConfiguration) throws Exception {
        ReloadableProperties properties = new ReloadableProperties(propertiesConfiguration);
        return properties;
    }

    @Bean(name = "rabbitmqProperties")
    @ConditionalOnBean(name = "rabbitmqPropertiesConfiguration")
    public Properties rabbitmqProperties(@Autowired @Qualifier(value = "rabbitmqPropertiesConfiguration") PropertiesConfiguration propertiesConfiguration) throws Exception {
        ReloadableProperties properties = new ReloadableProperties(propertiesConfiguration);
        return properties;
    }

}
