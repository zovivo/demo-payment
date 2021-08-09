package vn.vnpay.process.service;


import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.model.response.ResponseData;

public interface PaymentService extends BaseService<Payment, Long> {

    public ResponseData executePayment(PaymentModel paymentModel) throws CustomException;

}
