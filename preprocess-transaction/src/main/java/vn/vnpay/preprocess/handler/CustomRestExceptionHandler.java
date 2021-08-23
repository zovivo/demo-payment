package vn.vnpay.preprocess.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.util.ResponsePreProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class CustomRestExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CustomRestExceptionHandler.class);

    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleAllOtherException(Exception ex, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", ex.getMessage());
        logger.warn("Exception: " + ex.getMessage(), ex);
        return responsePreProcessor.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, responseData, request);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseData> handleCustomException(CustomException ex, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(HttpStatus.BAD_REQUEST.value() + "", ex.getCustomCode().getDescription());
        logger.warn("Handler for CustomException: ", ex);
        return responsePreProcessor.buildResponseEntity(HttpStatus.BAD_REQUEST, responseData, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> validationMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            validationMessages.add(error.getDefaultMessage());
        }
        ResponseData responseData = new ResponseData(HttpStatus.BAD_REQUEST.value() + "", String.join(", ", validationMessages));
        logger.warn("Handler for ArgumentNotValidException: {}", ex);
        return responsePreProcessor.buildResponseEntity(HttpStatus.BAD_REQUEST, responseData, request);
    }
}
