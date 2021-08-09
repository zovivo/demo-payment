package vn.vnpay.process.model.enu;

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
    SEND_PARTNER_FAIL("send to partner failed"),
    ;

    private final String description;

}
