package vn.vnpay.preprocess.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/31/2021
 * Created with IntelliJ IDEA
 */

@Configuration
@RefreshScope
public class MinioConfig {

    @Autowired
    @Qualifier(value = "minioProperties")
    private Properties minioProperties;

    @Bean(name = "minioClient")
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getProperty("spring.minio.url"), Integer.valueOf(minioProperties.getProperty("spring.minio.port")), false)
                .credentials(minioProperties.getProperty("spring.minio.access-key"), minioProperties.getProperty("spring.minio.secret-key"))
                .build();
        return minioClient;
    }

}
