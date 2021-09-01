package vn.vnpay.preprocess.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.vnpay.preprocess.constant.MinioConstant;

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
    @Qualifier(value = MinioConstant.MINIO_PROPERTIES_BEAN)
    private Properties minioProperties;

    @Bean(name = MinioConstant.MINIO_CLIENT_BEAN)
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getProperty(MinioConstant.URL), Integer.valueOf(minioProperties.getProperty(MinioConstant.PORT)), false)
                .credentials(minioProperties.getProperty(MinioConstant.ACCESS_KEY), minioProperties.getProperty(MinioConstant.SECRET_KEY))
                .build();
        return minioClient;
    }

}
