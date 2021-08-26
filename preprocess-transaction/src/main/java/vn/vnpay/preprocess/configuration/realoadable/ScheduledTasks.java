package vn.vnpay.preprocess.configuration.realoadable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/18/2021
 * Created with IntelliJ IDEA
 */

@Component
public class ScheduledTasks {

    private static final Logger logger = LogManager.getLogger(ScheduledTasks.class);

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("rabbitmqProperties")
    private Properties rabbitmqProperties;

//    @Scheduled(fixedRate = 1000)
    public void scheduleTaskWithFixedRate() {
        logger.info("Environment: application.theme.color: " + environment.getProperty("application.theme.color"));
        logger.info("rabbitmqProperties: spring.rabbitmq.host: " + rabbitmqProperties.getProperty("spring.rabbitmq.host"));

    }

}
