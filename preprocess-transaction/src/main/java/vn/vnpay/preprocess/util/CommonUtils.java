package vn.vnpay.preprocess.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Component
public class CommonUtils {

    public static final String REQUEST_ID = "request_id";
    private static final Logger logger = LogManager.getLogger(CommonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    public static String parseObjectToString(Object object) {
        return gson.toJson(object);
    }

    public static Date parseStringToDate(String dateStr, DateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.warn("ParseException: ", e);
            return null;
        }
        return date;
    }

    public static <T> T parseStringToObject(String json, Class<T> classObject) {
        try {
            return gson.fromJson(json, classObject);
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

    public static <T> T convertData(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

}
