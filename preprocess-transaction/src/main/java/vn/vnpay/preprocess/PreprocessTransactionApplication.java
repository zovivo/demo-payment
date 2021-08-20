package vn.vnpay.preprocess;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PreprocessTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreprocessTransactionApplication.class, args);
    }

}
