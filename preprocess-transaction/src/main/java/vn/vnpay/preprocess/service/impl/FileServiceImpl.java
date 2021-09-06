package vn.vnpay.preprocess.service.impl;

import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.constant.AppConstant;
import vn.vnpay.preprocess.constant.CustomCode;
import vn.vnpay.preprocess.constant.MinioConstant;
import vn.vnpay.preprocess.dto.FileMinIODTO;
import vn.vnpay.preprocess.dto.FileUploadDTO;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.service.FileService;
import vn.vnpay.preprocess.util.CommonUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.service.impl
 * Author: zovivo
 * Date: 8/31/2021
 * Created with IntelliJ IDEA
 */

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);
    @Autowired
    @Qualifier(value = MinioConstant.MINIO_PROPERTIES_BEAN)
    private Properties minioProperties;
    private MinioClient minioClient;

    public FileServiceImpl(@Autowired MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public ResponseData uploadFile(FileUploadDTO fileUpload) throws Exception {
        logger.info("begin uploadFile: {}", fileUpload.getName());
        String tempFilePath = saveTempFile(fileUpload);
        ResponseData responseData = uploadToMinio(fileUpload.getBucketName(), fileUpload.getName(), tempFilePath);
        deleteTempFile(tempFilePath);
        logger.info("end uploadFile: {}", fileUpload.getName());
        return responseData;
    }

    @Override
    public byte[] getFileData(FileMinIODTO fileMinIODTO) throws Exception {
        String bucketName = fileMinIODTO.getBucketName() == null ? minioProperties.getProperty(MinioConstant.DEFAULT_BUCKET_NAME) : fileMinIODTO.getBucketName();
        logger.info("begin getFileInputStream: {} from bucket {}", fileMinIODTO.getObjectName(), bucketName);
        byte[] data;
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileMinIODTO.getObjectName())
                        .build())) {
            data = IOUtils.toByteArray(stream);
        }
        logger.info("end getFileInputStream: {} from bucket {}", fileMinIODTO.getObjectName(), bucketName);
        return data;
    }

    @Override
    public void checkBucket(String bucketName) throws Exception {
        bucketName = bucketName == null ? minioProperties.getProperty(MinioConstant.DEFAULT_BUCKET_NAME) : bucketName;
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

    @Override
    public ResponseData uploadToMinio(String bucketName, String objectName, String filePath) throws Exception {
        logger.info("begin uploadToMinio");
        checkBucket(bucketName);
        ObjectWriteResponse response = minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filePath)
                        .build());
        logger.info("create object file {} successfully", objectName);
        logger.info("end uploadToMinio");
        ResponseData responseData = new ResponseData(CustomCode.SUCCESS);
        FileMinIODTO fileMinIO = new FileMinIODTO(response.bucket(), response.object());
        responseData.setData(fileMinIO);
        return responseData;
    }

    /**
     * hàm lưu file tạm thời trên server
     *
     * @param fileUpload - {@link FileUploadDTO}
     * @return tempFilePath - {@link String} - đường dẫn file tạm thời
     */
    protected String saveTempFile(FileUploadDTO fileUpload) {
        logger.info("begin save temp file of {} ", fileUpload.getName());
        String tempFilePath = CommonUtils.saveTempFile(fileUpload.getContent(), minioProperties.getProperty(AppConstant.TEMP_DIR));
        logger.info("end save temp file of {} ", fileUpload.getName());
        return tempFilePath;
    }

    /**
     * hàm xóa file tạm thời trên server
     *
     * @param tempFilePath - {@link String} - đường dẫn file tạm thời
     */
    protected void deleteTempFile(String tempFilePath) {
        logger.info("begin delete temp file : {}", tempFilePath);
        CommonUtils.deleteTempFile(tempFilePath);
        logger.info("end delete temp file : {}", tempFilePath);
    }
}
