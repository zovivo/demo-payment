package vn.vnpay.process.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import vn.vnpay.process.configuration.realoadable.ReloadablePropertySourceFactory;
import vn.vnpay.process.constant.DataSourceConstant;

import java.util.Properties;

@Configuration
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        })
@PropertySource(value = "${spring.datasource-config.location}", factory = ReloadablePropertySourceFactory.class)
public class HibernateConfig {

    private static final String PACKAGES_TO_SCAN = "vn.vnpay.process.entity";
    private static Logger logger = LogManager.getLogger(HibernateConfig.class);
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${hibernate.ddl-auto}")
    private String hibernateDDLAuto;
    @Value("${hibernate.show-sql}")
    private String hibernateShowSQL;
    @Value("${hibernate.format-sql}")
    private String hibernateFormatSQL;

    @Bean(name = "hikariDataSource")
    @Primary
    @RefreshScope
    @ConfigurationProperties(DataSourceConstant.HIKARI_PREFIX_CONFIG)
    public HikariDataSource dataSource() {
        HikariDataSource hikariDataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        return hikariDataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(@Autowired @Qualifier("hikariDataSource") HikariDataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(DataSourceConstant.PACKAGES_TO_SCAN);
        Properties properties = getHibernateProperties();
        entityManagerFactoryBean.setJpaProperties(properties);
        logger.info("entityManagerFactory: {}", entityManagerFactoryBean);
        return entityManagerFactoryBean;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, hibernateDialect);
        properties.put(Environment.SHOW_SQL, hibernateShowSQL);
        properties.put(Environment.FORMAT_SQL, hibernateFormatSQL);
        properties.put(Environment.HBM2DDL_AUTO, hibernateDDLAuto);
        return properties;
    }


}
