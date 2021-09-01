package vn.vnpay.preprocess.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Component
public class CommonUtils {

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
            logger.error("ParseException: ", e);
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

    public static String saveTempFile(MultipartFile file, String tempPath) {
        String fileName = file.getOriginalFilename();
        String tempFilePath = tempPath + fileName;
        File tempFileDir = new File(tempPath);
        if (!tempFileDir.exists()) {
            logger.info("create directory {}", tempPath);
            tempFileDir.mkdirs();
        }
        try {
            FileCopyUtils.copy(file.getBytes(), new File(tempFilePath));
        } catch (IOException e) {
            logger.error("IOException: ", e);
        }
        return tempFilePath;
    }

    public static void deleteTempFile(String tempPath) {
        File tempFile = new File(tempPath);
        if (tempFile.delete()) {
            logger.info("deleted {} ", tempPath);
        } else {
            logger.warn("failed to delete {}.", tempPath);
        }
    }

}
