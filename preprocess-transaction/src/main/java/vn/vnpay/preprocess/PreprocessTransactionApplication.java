package vn.vnpay.preprocess;


import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class PreprocessTransactionApplication {

    private static ApplicationContext applicationContext;

    public static void displayAllBeans() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println(beanName);
        }
    }

    public static void main(String[] args) {

        SpringApplication.run(PreprocessTransactionApplication.class, args);
        displayAllBeans();
    }

}
