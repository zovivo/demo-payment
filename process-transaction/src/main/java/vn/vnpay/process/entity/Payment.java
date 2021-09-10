package vn.vnpay.process.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.process.constant.SendPartnerStatus;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Column(name = "token_key")
    private String tokenKey;
    @Column(name = "app_id")
    private String apiID;
    private String mobile;
    @Column(name = "bank_code")
    private String bankCode;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "pay_date")
    private String payDate;
    @Column(name = "additional_data")
    private String additionalData;
    @Column(name = "debit_amount")
    private Long debitAmount;
    @Column(name = "resp_code")
    private String respCode;
    @Column(name = "resp_desc")
    private String respDesc;
    @Column(name = "trace_transfer")
    private String traceTransfer;
    @Column(name = "message_type")
    private String messageType;
    @Column(name = "check_sum")
    private String checkSum;
    @Column(name = "order_code")
    private String orderCode;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "real_amount")
    private String realAmount;
    @Column(name = "promotion_code")
    private String promotionCode;
    @Column(name = "add_value")
    private String addValue;
    @Column(name = "send_partner_status")
    @Enumerated(EnumType.STRING)
    private SendPartnerStatus sendPartnerStatus;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "payment")
    private List<Order> orders;

}
