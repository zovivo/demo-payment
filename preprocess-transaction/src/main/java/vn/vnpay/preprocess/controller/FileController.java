package vn.vnpay.preprocess.controller;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.vnpay.preprocess.constant.AppConstant;
import vn.vnpay.preprocess.dto.FileUploadDTO;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.service.FileService;

import javax.servlet.http.HttpServletRequest;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.controller
 * Author: zovivo
 * Date: 8/31/2021
 * Created with IntelliJ IDEA
 */

@RestController
@RequestMapping(value = "/file/")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = {"upload/"})
    public ResponseEntity<ResponseData> uploadFile(@ModelAttribute FileUploadDTO fileUpload, HttpServletRequest request) throws Exception {
        putKeyToThread(ThreadContext.get(AppConstant.REQUEST_ID));
        ResponseData responseData = fileService.uploadFile(fileUpload);
        return buildResponseEntity(HttpStatus.OK, responseData, request);
    }

    @RequestMapping(value = "download/", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value = "file-name") String fileName) throws Exception {
        putKeyToThread(ThreadContext.get(AppConstant.REQUEST_ID));
        return downloadFile(fileName, fileService.getFileData(fileName));
    }

}


