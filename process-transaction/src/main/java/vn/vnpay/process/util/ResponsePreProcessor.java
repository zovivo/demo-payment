package vn.vnpay.process.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.vnpay.process.constant.CustomCode;
import vn.vnpay.process.response.ResponseData;

import javax.servlet.http.HttpServletRequest;


@Component
public class ResponsePreProcessor {

    public ResponseEntity<ResponseData> buildResponseEntity(HttpStatus status, ResponseData responseData, HttpServletRequest request) {
        responseData.setResponseId(request.getAttribute(CommonUtils.REQUEST_ID).toString());
        ResponseEntity<ResponseData> responseEntity = new ResponseEntity<ResponseData>(responseData, status);
        return responseEntity;
    }

    public ResponseData buildResponseData(Exception e, CustomCode customCode) {
        return new ResponseData(customCode);
    }

}
