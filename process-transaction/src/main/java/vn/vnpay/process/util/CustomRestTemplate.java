package vn.vnpay.process.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomRestTemplate {

    private static final Logger logger = LogManager.getLogger(CustomRestTemplate.class);

    private static final String PARTNER_URL = "https://api.foodbook.vn/ipos/ws/xpartner/callback/vnpay";

    private RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Object> postForObject(Object input) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestBody = new HttpEntity<>(input, headers);
        logger.info("send data to URL: {}", PARTNER_URL);
        ResponseEntity<Object> response;
        try {
            response = restTemplate.postForEntity(PARTNER_URL, requestBody, Object.class);
        } catch (Exception e) {
            return null;
        }
        return response;
    }

}
