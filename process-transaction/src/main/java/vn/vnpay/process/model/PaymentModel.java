package vn.vnpay.process.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import vn.vnpay.process.entity.Payment;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel extends BaseModel{

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
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PaymentModel, Payment>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        Payment payment = modelMapper.map(paymentModel, Payment.class);
        return payment;
    }

}
