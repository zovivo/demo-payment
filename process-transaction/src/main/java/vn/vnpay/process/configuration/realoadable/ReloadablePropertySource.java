package vn.vnpay.process.configuration.realoadable;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;
import vn.vnpay.process.exception.PropertiesException;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/18/2021
 * Created with IntelliJ IDEA
 */
public class ReloadablePropertySource extends PropertySource {

    PropertiesConfiguration propertiesConfiguration;
    @Value("${spring.properties.refreshDelay}")
    private long refreshDelay;

    public ReloadablePropertySource(String name, PropertiesConfiguration propertiesConfiguration) {
        super(name);
        this.propertiesConfiguration = propertiesConfiguration;
    }

    public ReloadablePropertySource(String name, String path) {
        super(!StringUtils.hasText(name) ? path : name);
        try {
            this.propertiesConfiguration = new PropertiesConfiguration(path);
            FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
            strategy.setRefreshDelay(refreshDelay);
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
