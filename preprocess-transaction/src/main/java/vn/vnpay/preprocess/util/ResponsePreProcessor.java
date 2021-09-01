package vn.vnpay.preprocess.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.vnpay.preprocess.constant.AppConstant;
import vn.vnpay.preprocess.response.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class ResponsePreProcessor {

    public ResponseEntity<ResponseData> buildResponseEntity(HttpStatus status, ResponseData responseData, HttpServletRequest request) {
        responseData.setResponseId(request.getAttribute(AppConstant.REQUEST_ID).toString());
        ResponseEntity<ResponseData> responseEntity = new ResponseEntity<ResponseData>(responseData, status);
        return responseEntity;
    }

    public ResponseEntity<ByteArrayResource> buildDownloadResponseEntity(String fileName, byte[] data) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=" + fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return new ResponseEntity<ByteArrayResource>(resource, responseHeader, HttpStatus.OK);
    }

}
