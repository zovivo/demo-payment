package vn.vnpay.payment.service;

import vn.vnpay.payment.entity.Payment;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.PaymentDTO;
import vn.vnpay.payment.model.response.ResponseData;

public interface PaymentService extends BaseService<Payment, Long>{

    public ResponseData executePayment(PaymentDTO paymentDTO) throws CustomException;

}
