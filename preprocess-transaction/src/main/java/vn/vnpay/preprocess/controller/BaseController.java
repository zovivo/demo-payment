package vn.vnpay.preprocess.controller;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.vnpay.preprocess.constant.AppConstant;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.util.ResponsePreProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.controller
 * Author: zovivo
 * Date: 9/1/2021
 * Created with IntelliJ IDEA
 */
@Component
@NoArgsConstructor
public class BaseController {

    @Autowired
    protected ResponsePreProcessor responsePreProcessor;

    protected ResponseEntity<ResponseData> buildResponseEntity(HttpStatus status, ResponseData responseData, HttpServletRequest request) {
        return responsePreProcessor.buildResponseEntity(status, responseData, request);
    }

    protected void putKeyToThread(String keyValue) {
        ThreadContext.put(AppConstant.TOKEN_KEY_THREAD, keyValue);
    }

    protected ResponseEntity<ByteArrayResource> downloadFile(String fileName, byte[] data) throws IOException {
        return responsePreProcessor.buildDownloadResponseEntity(fileName, data);
    }

}
