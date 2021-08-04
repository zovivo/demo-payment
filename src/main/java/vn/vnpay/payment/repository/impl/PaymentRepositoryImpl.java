package vn.vnpay.payment.repository.impl;

import org.springframework.stereotype.Repository;
import vn.vnpay.payment.entity.Payment;
import vn.vnpay.payment.repository.PaymentRepository;

@Repository(value = "paymentRepository")
public class PaymentRepositoryImpl extends BaseRepositoryImpl<Payment,Long> implements PaymentRepository {

    public PaymentRepositoryImpl() {
        super(Payment.class);
    }

}
