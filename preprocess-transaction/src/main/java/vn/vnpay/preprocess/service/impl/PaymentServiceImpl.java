package vn.vnpay.preprocess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.model.dto.PaymentDTO;
import vn.vnpay.preprocess.model.enu.ErrorCode;
import vn.vnpay.preprocess.model.response.ResponseData;
import vn.vnpay.preprocess.service.PaymentRedisService;
import vn.vnpay.preprocess.service.PaymentService;
import vn.vnpay.preprocess.service.RabbitMQService;
import vn.vnpay.preprocess.util.CommonUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service(value = "paymentService")
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private PaymentRedisService paymentRedisService;

    /**
     * hàm thực thi kiểm tra tokenKey trùng sau 0h
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkDuplicateToken(PaymentDTO paymentDTO) throws CustomException {
        logger.info("===== begin checkDuplicateToken =====");
        Payment payment = paymentRedisService.getByTokenKey(paymentDTO.getTokenKey());
        if (payment != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date lastPayDate = CommonUtils.parseStringToDate(payment.getPayDate().substring(0, 8), dateFormat);
            Date newPayDate = CommonUtils.parseStringToDate(paymentDTO.getPayDate().substring(0, 8), dateFormat);
            if (lastPayDate.getTime() == newPayDate.getTime())
                throw new CustomException(ErrorCode.DUPLICATE_TOKEN_KEY);
        }
        logger.info("===== end checkDuplicateToken =====");
        return;
    }

    /**
     * hàm thực thi kiểm tra realAmount và debitAmount
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkRealAmount(PaymentDTO paymentDTO) throws CustomException {
        logger.info("===== begin checkRealAmount =====");
        if (Long.parseLong(paymentDTO.getRealAmount()) > paymentDTO.getDebitAmount())
            throw new CustomException(ErrorCode.EXCEEDS_DEBIT);
        logger.info("===== end checkRealAmount =====");
        return;
    }

    /**
     * hàm thực thi kiểm tra promotionCode
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkPromotionCode(PaymentDTO paymentDTO) throws CustomException {
        logger.info("===== begin checkPromotionCode =====");
        if (Long.parseLong(paymentDTO.getRealAmount()) != paymentDTO.getDebitAmount()) {
            if (paymentDTO.getPromotionCode() == null || paymentDTO.getPromotionCode().trim().isEmpty())
                throw new CustomException(ErrorCode.PROMOTION_CODE_EMPTY);
        }
        logger.info("===== end checkPromotionCode =====");
        return;
    }

    /**
     * hàm thực thi kiểm tra dữ liệu thanh toán hợp lệ
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkValidPayment(PaymentDTO paymentDTO) throws CustomException {
        logger.info("===== begin checkValidPayment =====");
        checkDuplicateToken(paymentDTO);
        checkRealAmount(paymentDTO);
        checkPromotionCode(paymentDTO);
        logger.info("===== end checkValidPayment =====");
    }

    /**
     * hàm thực thi gửi dữ liệu thanh toán lên queue
     *
     * @param payment - {@link Payment}
     * @return responseData - {@link ResponseData}
     */
    protected ResponseData sendRabbitMQ(Payment payment) {
        logger.info("===== begin sendRabbitMQ =====");
        ResponseData responseData = rabbitMQService.send(payment);
        logger.info("===== end sendRabbitMQ =====");
        return responseData;
    }

    @Override
    public ResponseData executePayment(PaymentDTO paymentDTO) throws CustomException {
        logger.info("===== begin executePayment =====");
        checkValidPayment(paymentDTO);
        Payment payment = PaymentDTO.convertToEntity(paymentDTO);
        ResponseData responseData = sendRabbitMQ(payment);
        logger.info("===== end executePayment =====");
        return responseData;
    }

}
