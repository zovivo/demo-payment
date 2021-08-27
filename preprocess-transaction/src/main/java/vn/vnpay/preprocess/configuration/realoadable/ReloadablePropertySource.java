package vn.vnpay.preprocess.configuration.realoadable;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;
import vn.vnpay.preprocess.exception.PropertiesException;

import java.util.Properties;


/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/18/2021
 * Created with IntelliJ IDEA
 */
public class ReloadablePropertySource extends PropertySource {

    PropertiesConfiguration propertiesConfiguration;

    public ReloadablePropertySource(String name, PropertiesConfiguration propertiesConfiguration) {
        super(name);
        this.propertiesConfiguration = propertiesConfiguration;
    }

    public ReloadablePropertySource(String name, String path) {
        super(!StringUtils.hasText(name) ? path : name);
        try {
            this.propertiesConfiguration = new PropertiesConfiguration(path);
            FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
            strategy.setRefreshDelay(5000);
            this.propertiesConfiguration.setReloadingStrategy(strategy);
        } catch (Exception e) {
            throw new PropertiesException(e);
        }
    }

    public ReloadablePropertySource(String name, String path, Properties properties) {
        super(!StringUtils.hasText(name) ? path : name);
        try {
            this.propertiesConfiguration = new PropertiesConfiguration(path);
//            propertiesConfiguration = (PropertiesConfiguration) ConfigurationConverter.getConfiguration(properties);
            FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
            strategy.setRefreshDelay(5000);
            this.propertiesConfiguration.setReloadingStrategy(strategy);
        } catch (Exception e) {
            throw new PropertiesException(e);
        }
    }

    @Override
    public Object getProperty(String s) {
        return propertiesConfiguration.getProperty(s);
    }
}
