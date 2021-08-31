package vn.vnpay.preprocess.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.constant.CustomCode;
import vn.vnpay.preprocess.dto.FileUploadDTO;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.util.CommonUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.service.impl
 * Author: zovivo
 * Date: 8/31/2021
 * Created with IntelliJ IDEA
 */

@Service
public class FileUploadService {

    @Value("${server.temp-folder.path}")
    private String tempFolder;
    @Value("${server.bucket.name}")
    private String bucketName;

    private static final Logger logger = LogManager.getLogger(FileUploadService.class);

    private MinioClient minioClient;

    public ResponseData uploadFile(FileUploadDTO fileUpload) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        logger.info("begin uploadFile: {}", fileUpload.getName());
        String tempFilePath = saveTempFile(fileUpload);
        ResponseData responseData = uploadFile(bucketName, fileUpload.getName(), tempFilePath);
        deleteTempFile(tempFilePath);
        logger.info("end uploadFile: {}", fileUpload.getName());
        return responseData;
    }

    public void checkBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        logger.info("begin checkBucket {}", bucketName);
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (isExist) {
            logger.warn("bucket {} already exists.", bucketName);
        } else {
            logger.warn("bucket {} not exist.", bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            logger.warn("create bucket {} successfully", bucketName);
        }
        logger.info("end checkBucket {}", bucketName);
    }

    public ResponseData uploadFile(String bucketName, String objectName, String filePath) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        checkBucket(bucketName);
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filePath)
                        .build());
        logger.warn("create object file {} successfully", objectName);
        return new ResponseData(CustomCode.SUCCESS);
    }

    protected String saveTempFile(FileUploadDTO fileUpload) {
        logger.info("begin save temp file of {} ", fileUpload.getName());
        String tempFilePath = CommonUtils.saveTempFile(fileUpload.getContent(), tempFolder);
        logger.info("end save temp file of {} ", fileUpload.getName());
        return tempFilePath;
    }

    protected void deleteTempFile(String tempFilePath) {
        logger.info("begin delete temp file : {}", tempFilePath);
        CommonUtils.deleteTempFile(tempFilePath);
        logger.info("end delete temp file : {}", tempFilePath);
    }

    public FileUploadService(@Autowired MinioClient minioClient) {
        this.minioClient = minioClient;
    }
}
