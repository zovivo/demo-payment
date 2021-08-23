package vn.vnpay.process.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
public class CommonUtils {

    private static final Logger logger = LogManager.getLogger(CommonUtils.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String REQUEST_ID = "request_id";

    public static Timestamp getCurrentTime() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime;
    }

    public static Date getCurrentDateTime() {
        Date currentTime = new Date(System.currentTimeMillis());
        return currentTime;
    }

    public static Date getEndDateTime() {
        LocalDateTime localDate = LocalTime.MAX.atDate(LocalDate.now());
        Date endDayTime = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
        return endDayTime;
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

    public static <T> T convertData(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

}
