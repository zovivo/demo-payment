package vn.vnpay.process.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.util.CommonUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel extends BaseModel {

    private String tokenKey;
    private String apiID;
    private String mobile;
    private String bankCode;
    private String accountNo;
    private String payDate;
    private String additionalData;
    private Long debitAmount;
    private String respCode;
    private String respDesc;
    private String traceTransfer;
    private String messageType;
    private String checkSum;
    private String orderCode;
    private String userName;
    private String realAmount;
    private String promotionCode;
    private String addValue;

    public static Payment convertToEntity(PaymentModel paymentModel) {
        return CommonUtils.convertData(paymentModel, Payment.class);
    }

}
