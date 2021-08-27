package vn.vnpay.preprocess.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomCode {

    SUCCESS("success","200"),
    FAIL("fail","500"),
    UNKNOWN_ERROR("unknown error","500"),
    VALIDATION_FAILED("validation failed","400"),
    DUPLICATE_TOKEN_KEY("same day token key","400"),
    EXCEEDS_DEBIT("amount after promotion exceeds debit","400"),
    PROMOTION_CODE_EMPTY("promotion code is null or blank","400"),
    SEND_PARTNER_FAIL("send to partner failed","400"),
    REQUEST_TIME_OUT("request timeout to execute in queue","400"),
    CHECKSUM_NOT_MATCH("checkSum not match","400"),
    BANK_CODE_INVALID("bank code not exist","400"),
    ;

    private final String description;
    private final String statusCode;

}
