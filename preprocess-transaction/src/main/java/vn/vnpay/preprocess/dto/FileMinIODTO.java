package vn.vnpay.preprocess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.preprocess.validator.NullOrBlank;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.dto
 * Author: zovivo
 * Date: 9/6/2021
 * Created with IntelliJ IDEA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileMinIODTO {

    private String bucketName;
    @NullOrBlank(message = "object name must be not null or blank")
    private String objectName;

}
