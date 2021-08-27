package vn.vnpay.preprocess.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess.util
 * Author: zovivo
 * Date: 8/12/2021
 * Time: 4:13 PM
 * Created with IntelliJ IDEA
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Partner {

    private String bankCode;
    private String privateKey;

}
