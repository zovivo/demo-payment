package vn.vnpay.payment.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpay.payment.entity.Payment;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.PaymentDTO;
import vn.vnpay.payment.model.enu.ErrorCode;
import vn.vnpay.payment.repository.PaymentRepository;
import vn.vnpay.payment.service.PaymentService;

@Service(value = "paymentService")
public class PaymentServiceImpl extends BaseServiceImpl<PaymentRepository, Payment, Long> implements PaymentService {

    private final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    protected void checkDuplicateToken(PaymentDTO paymentDTO) throws CustomException {
        Payment payment = paymentRepository.getByTokenKey(paymentDTO.getTokenKey());
        if (payment != null)
            throw new CustomException(ErrorCode.DUPLICATE_TOKEN_KEY);
        return;
    }

    protected void checkRealAmount(PaymentDTO paymentDTO) throws CustomException {
        if (Long.parseLong(paymentDTO.getRealAmount()) > paymentDTO.getDebitAmount())
            throw new CustomException(ErrorCode.EXCEEDS_DEBIT);
        return;
    }

    protected void checkPromotionCode(PaymentDTO paymentDTO) throws CustomException {
        if (Long.parseLong(paymentDTO.getRealAmount()) != paymentDTO.getDebitAmount()) {
            if (paymentDTO.getPromotionCode() == null || paymentDTO.getPromotionCode().trim().isEmpty())
                throw new CustomException(ErrorCode.PROMOTION_CODE_EMPTY);
        }
        return;
    }

    protected void checkValidPayment(PaymentDTO paymentDTO) throws CustomException {
        checkDuplicateToken(paymentDTO);
        checkRealAmount(paymentDTO);
        checkPromotionCode(paymentDTO);
    }

    @Override
    @Transactional
    public void executePayment(PaymentDTO paymentDTO) throws CustomException {
        checkValidPayment(paymentDTO);
        Payment payment = PaymentDTO.convertToEntity(paymentDTO);
        create(payment);
        return;
    }

    @Override
    public PaymentRepository getRepository() {
        return paymentRepository;
    }
}
