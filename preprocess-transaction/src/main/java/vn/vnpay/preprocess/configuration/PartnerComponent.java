package vn.vnpay.preprocess.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import vn.vnpay.preprocess.util.Partner;

import java.nio.file.Path;
import java.util.List;


/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/12/2021
 * Time: 4:09 PM
 * Created with IntelliJ IDEA
 */

@Component
@ConfigurationProperties(prefix = "qrcode")
@PropertySource(value = "classpath:partner.yml", factory = YamlPropertySourceFactory.class)
public class PartnerComponent {

    private final List<Partner> partners;

    public PartnerComponent(List<Partner> partners) {
        this.partners = partners;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    /**
     * lấy thông tin Partner theo bankCode
     *
     * @param bankCode {@link String}
     * @return partner {@link Partner}
     */
    public Partner getPartnerByCode(String bankCode) {
        return this.partners.stream()
                .filter(partner -> partner.getBankCode().equals(bankCode))
                .findAny().orElse(null);
    }

}
