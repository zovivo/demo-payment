package vn.vnpay.process.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Project: demo-payment
 * Package: vn.vnpay.process.constant
 * Author: zovivo
 * Date: 8/26/2021
 * Created with IntelliJ IDEA
 */

@Getter
@AllArgsConstructor
public enum SendPartnerStatus {

    SUCCESS("success"),
    FAIL("fail"),
    ;

    private final String description;
}
