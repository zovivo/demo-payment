package vn.vnpay.payment.service;

import vn.vnpay.payment.entity.Payment;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.PaymentDTO;

public interface PaymentService extends BaseService<Payment, Long>{

    public void executePayment(PaymentDTO paymentDTO) throws CustomException;

}
