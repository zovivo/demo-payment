package vn.vnpay.process.repository.impl;

import org.springframework.stereotype.Repository;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.repository.PaymentRepository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository(value = "paymentRepository")
public class PaymentRepositoryImpl extends BaseRepositoryImpl<Payment, Long> implements PaymentRepository {

    public PaymentRepositoryImpl() {
        super(Payment.class);
    }

    @Override
    public Payment getByTokenKey(String tokenKey) {
        String queryStr = "From Payment p where p.tokenKey = :tokenKey Order By p.createdAt Desc";
        Query query = getEntityManager().createQuery(queryStr);
        query.setParameter("tokenKey", tokenKey);
        Payment payment = null;
        try {
            payment = (Payment) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return payment;
    }

}
