package vn.vnpay.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpay.payment.entity.Payment;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.PaymentDTO;
import vn.vnpay.payment.repository.PaymentRepository;
import vn.vnpay.payment.service.PaymentService;

@Service(value = "paymentService")
public class PaymentServiceImpl extends BaseServiceImpl<PaymentRepository, Payment, Long> implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void executePayment(PaymentDTO paymentDTO) throws CustomException {
        Payment payment = PaymentDTO.convertToEntity(paymentDTO);
        create(payment);
        return;
    }

    @Override
    public PaymentRepository getRepository() {
        return paymentRepository;
    }
}
