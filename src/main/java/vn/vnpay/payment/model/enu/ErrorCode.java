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
    DUPLICATE_TOKEN_KEY("same day token key"),
    EXCEEDS_DEBIT("amount after promotion exceeds debit"),
    PROMOTION_CODE_EMPTY("promotion code is null or blank"),
    ;

    private final String description;

}