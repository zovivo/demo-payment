package vn.vnpay.process;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vn.vnpay.process.util.CommonUtils;

import java.util.Arrays;

@SpringBootApplication
@EnableAsync
public class ProcessTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessTransactionApplication.class, args);
    }


}
