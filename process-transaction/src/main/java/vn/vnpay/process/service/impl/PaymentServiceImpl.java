package vn.vnpay.process.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpay.process.constant.CustomCode;
import vn.vnpay.process.constant.SendPartnerStatus;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.repository.PaymentRepository;
import vn.vnpay.process.repository.redis.PaymentRepositoryRedis;
import vn.vnpay.process.response.ResponseData;
import vn.vnpay.process.service.PaymentService;
import vn.vnpay.process.util.CommonUtils;
import vn.vnpay.process.util.CustomRestTemplate;

@Service(value = "paymentService")
public class PaymentServiceImpl extends BaseServiceImpl<PaymentRepository, Payment, Long> implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    @Autowired
    private CustomRestTemplate customRestTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentRepositoryRedis paymentRepositoryRedis;

    @Override
    @Transactional(noRollbackFor = {RuntimeException.class, CustomException.class})
    public ResponseData executePayment(PaymentModel paymentModel) throws RuntimeException, CustomException {
        logger.info("begin executePayment ");
        Payment payment = PaymentModel.convertToEntity(paymentModel);
        saveDB(payment);
        saveRedis(payment);
        ResponseData responseData = sendToPartner(paymentModel);
        payment = updatePayment(responseData, payment);
        if (!responseData.getCode().equals(CustomCode.SUCCESS.getStatusCode()))
            throw new CustomException(CustomCode.SEND_PARTNER_FAIL);
        responseData.setData(payment);
        logger.info("end executePayment ");
        return responseData;
    }

    protected void saveDB(Payment payment) throws RuntimeException {
        logger.info("begin saveDB ");
        try {
            payment = paymentRepository.insert(payment);
        } catch (RuntimeException e) {
            logger.error("insert payment to DB failed exception: ", e);
            throw new RuntimeException("insert payment to DB failed exception");
        }
        logger.info("save DB successfully: {}", CommonUtils.parseObjectToString(payment));
        logger.info("end saveDB ");
    }

    protected void saveRedis(Payment payment) throws RuntimeException {
        logger.info("begin saveRedis ");
        try {
            paymentRepositoryRedis.insert(payment);
        } catch (RuntimeException e) {
            logger.error("save Redis failed", e);
            throw new RuntimeException("save Redis failed");
        }
        logger.info("save Redis successfully");
        logger.info("end saveRedis ");
    }

    protected ResponseData sendToPartner(PaymentModel payment) {
        logger.info("begin sendToPartner ");
        ResponseEntity<Object> response = customRestTemplate.postForObject(payment);
        logger.info("response from partner: {}", response);
        ResponseData responseData;
        if (null != response && response.getStatusCode().is2xxSuccessful())
            responseData = new ResponseData(CustomCode.SUCCESS);
        else
            responseData = new ResponseData(CustomCode.FAIL);
        logger.info("end sendToPartner ");
        return responseData;
    }

    protected Payment updatePayment(ResponseData responseData, Payment payment) throws RuntimeException {
        logger.info("begin updatePayment ");
        if (responseData.getCode().equals(CustomCode.SUCCESS.getStatusCode()))
            payment.setSendPartnerStatus(SendPartnerStatus.SUCCESS);
        else
            payment.setSendPartnerStatus(SendPartnerStatus.FAIL);
        try {
            payment = paymentRepository.update(payment);
        } catch (RuntimeException e) {
            logger.error("update payment to DB failed exception: ", e);
            throw new RuntimeException("update payment to DB failed exception");
        }
        logger.info("save DB successfully: {}", CommonUtils.parseObjectToString(payment));
        logger.info("end updatePayment ");
        return payment;
    }

    @Override
    public PaymentRepository getRepository() {
        return paymentRepository;
    }
}
