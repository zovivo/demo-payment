package vn.vnpay.preprocess.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@Component
public class CustomURLFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(CustomURLFilter.class);
    private static final String REQUEST_ID = "request_id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestId = UUID.randomUUID().toString();
        servletRequest.setAttribute(REQUEST_ID, requestId);
        ThreadContext.put("requestId", requestId);
        logRequest((HttpServletRequest) servletRequest, requestId);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private void logRequest(HttpServletRequest request, String requestId) {
        if (request != null) {
            StringBuilder data = new StringBuilder();
            data.append("\nLOGGING REQUEST-----------------------------------\n")
                    .append("[REQUEST-ID]: ").append(requestId).append("\n")
                    .append("[PATH]: ").append(request.getRequestURI()).append("\n")
                    .append("[QUERIES]: ").append(request.getQueryString()).append("\n")
                    .append("[HEADERS]: ").append("\n");

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                data.append("---").append(key).append(" : ").append(value).append("\n");
            }
            data.append("LOGGING REQUEST-----------------------------------\n");

            logger.info(data.toString());
        }
    }

}
