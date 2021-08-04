package vn.vnpay.payment.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class CommonUtils {

    public static Timestamp getCurrentTime() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime;
    }
}
