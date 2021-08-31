package vn.vnpay.preprocess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.dto
 * Author: zovivo
 * Date: 8/31/2021
 * Created with IntelliJ IDEA
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDTO {

    private String name;
    private String description;
    private MultipartFile content;

}
