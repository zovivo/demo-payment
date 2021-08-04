package vn.vnpay.payment.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.response.ResponseData;
import vn.vnpay.payment.util.ResponsePreProcessor;


@RestControllerAdvice
public class CustomRestExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseData> handleCustomException(CustomException ex, WebRequest request) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(HttpStatus.BAD_REQUEST.toString());
        responseData.setMessage(ex.getErrorCode().getDescription());
        return responsePreProcessor.buildResponseEntity(HttpStatus.BAD_REQUEST, responseData);
    }

}
