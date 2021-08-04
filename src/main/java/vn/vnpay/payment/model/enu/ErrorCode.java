package vn.vnpay.payment.model.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS("success"),
    FAIL("fail"),
    UNKNOWN_ERROR("unknown error"),
    VALIDATION_FAILED("validation failed"),
    ;

    private final String description;

}
