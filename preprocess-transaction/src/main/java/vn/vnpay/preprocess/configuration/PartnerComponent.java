package vn.vnpay.preprocess.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.vnpay.preprocess.util.CommonUtils;
import vn.vnpay.preprocess.util.Partner;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;


/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.configuration
 * Author: zovivo
 * Date: 8/12/2021
 * Time: 4:09 PM
 * Created with IntelliJ IDEA
 */

@Component
public class PartnerComponent {

    private static final Logger logger = LogManager.getLogger(PartnerComponent.class);
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    @Value("${spring.partner-config.location}")
    private String path;
    private List<Partner> partners;

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

    @Scheduled(fixedRate = 10000)
    private void reloadPartnerProperties() throws IOException {
        Map<String, List<Partner>> partnerMap = mapper.readValue(new URL(path), new TypeReference<Map<String, List<Partner>>>() {
        });
        this.partners = partnerMap.get("partners");
        logger.info("partners: {}", CommonUtils.parseObjectToString(partners));
    }

}
