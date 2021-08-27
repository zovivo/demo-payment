package vn.vnpay.process.configuration.realoadable;

import org.apache.commons.configuration.PropertiesConfiguration;
import vn.vnpay.process.exception.PropertiesException;

import javax.naming.OperationNotSupportedException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/18/2021
 * Created with IntelliJ IDEA
 */

public class ReloadableProperties extends Properties {

    private PropertiesConfiguration propertiesConfiguration;

    public ReloadableProperties(PropertiesConfiguration propertiesConfiguration) throws IOException {
        super.load(new FileReader(propertiesConfiguration.getFile()));
        this.propertiesConfiguration = propertiesConfiguration;
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        propertiesConfiguration.setProperty(key, value);
        return super.setProperty(key, value);
    }

    @Override
    public String getProperty(String key) {
        String val = propertiesConfiguration.getString(key);
        super.setProperty(key, val);
        return val;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String val = propertiesConfiguration.getString(key, defaultValue);
        super.setProperty(key, val);
        return val;
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        throw new PropertiesException(new OperationNotSupportedException());
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        throw new PropertiesException(new OperationNotSupportedException());
    }

}