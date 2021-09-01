package vn.vnpay.preprocess.service;

import vn.vnpay.preprocess.dto.FileUploadDTO;
import vn.vnpay.preprocess.response.ResponseData;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.service
 * Author: zovivo
 * Date: 9/1/2021
 * Created with IntelliJ IDEA
 */
public interface FileService {

    /**
     * hàm upload file chung
     *
     * @param fileUpload {@link FileUploadDTO}
     * @return ResponseData
     * @throws Exception
     */
    public ResponseData uploadFile(FileUploadDTO fileUpload) throws Exception;

    /**
     * hàm check bucket trên MinIO Server,
     * nếu chưa có bucket, tạo bucket mới với tên bucketName truyền vào
     *
     * @param bucketName {@link String}
     * @return ResponseData
     * @throws Exception
     */
    public void checkBucket(String bucketName) throws Exception;

    /**
     * hàm upload file trực tiếp lên MinIO Server,
     *
     * @param bucketName {@link String} - tên bucket
     * @param objectName {@link String} - tên file lưu trên MinIO server
     * @param filePath   {@link String} - đường dẫn file upload
     * @return ResponseData
     * @throws Exception
     */
    public ResponseData uploadToMinio(String bucketName, String objectName, String filePath) throws Exception;

    /**
     * hàm get file data trực tiếp từ MinIO Server,
     *
     * @param fileName {@link String} - tên file lưu trên MinIO server
     * @return byte []
     * @throws Exception
     */
    public byte[] getFileData(String fileName) throws Exception;

}
