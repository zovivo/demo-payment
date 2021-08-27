package vn.vnpay.preprocess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.util.CommonUtils;
import vn.vnpay.preprocess.validator.DateFormat;
import vn.vnpay.preprocess.validator.NullOrBlank;
import vn.vnpay.preprocess.validator.PhoneNumber;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private String tokenKey;
    @NullOrBlank(message = "apiID cannot be blank or null")
    private String apiID;
    @PhoneNumber
    private String mobile;
    private String bankCode = "970445";
    private String accountNo;
    @DateFormat(message = "payDate wrong format")
    @DateTimeFormat
    private String payDate;
    private String additionalData;
    private Long debitAmount;
    private String respCode;
    private String respDesc;
    private String traceTransfer;
    private String messageType = "1";
    private String checkSum;
    @NullOrBlank(message = "orderCode cannot be blank or null")
    private String orderCode;
    private String userName;
    @NullOrBlank(message = "realAmount cannot be blank or null")
    private String realAmount;
    private String promotionCode;
    private String addValue;

    public static Payment convertToEntity(PaymentDTO paymentDTO) {
        return CommonUtils.convertData(paymentDTO, Payment.class);
    }

}
