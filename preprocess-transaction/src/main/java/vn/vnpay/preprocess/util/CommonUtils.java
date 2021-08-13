package vn.vnpay.preprocess.util;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
public class CommonUtils {

    private static final Logger logger = LogManager.getLogger(CommonUtils.class);
    public static final String REQUEST_ID = "request_id";

    public static Timestamp getCurrentTime() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime;
    }

    public static String parseObjectToString(Object object) {
        return new Gson().toJson(object);
    }

    public static Date parseStringToDate(String dateStr, DateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.info("ParseException: ", e);
            return null;
        }
        return date;
    }

    public static Calendar parseStringToCalendar(String dateStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.info("ParseException: ", e);
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static <T> T parseStringToObject(String json, Class<T> classObject) {
        try {
            return new Gson().fromJson(json, classObject);
        } catch (Exception e) {
            return null;
        }
    }

    public static String hashData(String dataInput) {
        String sha256hex = Hashing.sha256()
                .hashString(dataInput, StandardCharsets.UTF_8)
                .toString();
        return sha256hex;
    }
}
