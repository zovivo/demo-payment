package vn.vnpay.preprocess.controller;

import io.minio.errors.*;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.preprocess.dto.FileUploadDTO;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.service.impl.FileUploadService;
import vn.vnpay.preprocess.util.CommonUtils;
import vn.vnpay.preprocess.util.ResponsePreProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.controller
 * Author: zovivo
 * Date: 8/31/2021
 * Created with IntelliJ IDEA
 */

@RestController
@RequestMapping(value = "/file/")
public class FileController {

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"upload/"})
    public ResponseEntity<ResponseData> uploadFile(@ModelAttribute FileUploadDTO fileUpload, HttpServletRequest request) throws CustomException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ThreadContext.put("tokenKey", ThreadContext.get(CommonUtils.REQUEST_ID));
        ResponseData responseData = fileUploadService.uploadFile(fileUpload);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, responseData, request);
    }

}
