package vn.vnpay.payment.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import vn.vnpay.payment.util.GsonParserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LoggingService {

    private final Logger logger = LogManager.getLogger(LoggingService.class);

    private static final String REQUEST_ID = "request_id";

    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        StringBuilder data = new StringBuilder();
        data.append("\nLOGGING REQUEST BODY-----------------------------------\n")
                .append("[REQUEST-ID]: ").append(requestId).append("\n")
                .append("[BODY REQUEST]: ").append("\n\n")
                .append(GsonParserUtils.parseObjectToString(body))
                .append("\n\n")
                .append("LOGGING REQUEST BODY-----------------------------------\n");

        logger.info(data.toString());
    }

    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        StringBuilder data = new StringBuilder();
        data.append("\nLOGGING RESPONSE-----------------------------------\n")
                .append("[REQUEST-ID]: ").append(requestId).append("\n")
                .append("[BODY RESPONSE]: ").append("\n\n")
                .append(GsonParserUtils.parseObjectToString(body))
                .append("\n\n")
                .append("LOGGING RESPONSE-----------------------------------\n");

        logger.info(data.toString());
    }

}
