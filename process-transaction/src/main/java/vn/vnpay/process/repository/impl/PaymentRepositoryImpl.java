package vn.vnpay.process.repository.impl;

import org.springframework.stereotype.Repository;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.repository.PaymentRepository;

@Repository(value = "paymentRepository")
public class PaymentRepositoryImpl extends BaseRepositoryImpl<Payment, Long> implements PaymentRepository {

    public PaymentRepositoryImpl() {
        super(Payment.class);
    }

}
