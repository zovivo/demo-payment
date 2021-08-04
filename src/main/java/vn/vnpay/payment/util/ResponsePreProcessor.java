package vn.vnpay.payment.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.vnpay.payment.model.response.ResponseData;


@Component
public class ResponsePreProcessor {


    public ResponseEntity<ResponseData> buildResponseEntity(HttpStatus status, ResponseData responseData) {
        ResponseEntity<ResponseData> responseEntity = new ResponseEntity<ResponseData>(responseData, status);
        return responseEntity;
    }

}
