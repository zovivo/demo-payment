package vn.vnpay.payment.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.response.ResponseData;
import vn.vnpay.payment.util.ResponsePreProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class CustomRestExceptionHandler {

    private final Logger logger = LogManager.getLogger(CustomRestExceptionHandler.class);

    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseData> handleCustomException(CustomException ex, HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(HttpStatus.BAD_REQUEST.value() + "");
        responseData.setMessage(ex.getErrorCode().getDescription());
        logger.info("Handler for CustomException: {}", ex);
        return responsePreProcessor.buildResponseEntity(HttpStatus.BAD_REQUEST, responseData, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        List<String> validationMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            validationMessages.add(error.getDefaultMessage());
        }
        responseData.setCode(HttpStatus.BAD_REQUEST.value() + "");
        responseData.setMessage(String.join(", ", validationMessages));
        logger.info("Handler for ArgumentNotValidException: {}", ex);
        return responsePreProcessor.buildResponseEntity(HttpStatus.BAD_REQUEST, responseData, request);
    }
}
