package vn.vnpay.payment.repository;

import vn.vnpay.payment.entity.Payment;

public interface PaymentRepository extends BaseRepository<Payment, Long>{

    public Payment getByTokenKey(String tokenKey);

}
