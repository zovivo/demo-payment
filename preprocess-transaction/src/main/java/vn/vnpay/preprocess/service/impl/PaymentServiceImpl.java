package vn.vnpay.preprocess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.configuration.PartnerComponent;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.dto.PaymentDTO;
import vn.vnpay.preprocess.constant.CustomCode;
import vn.vnpay.preprocess.response.ResponseData;
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

    @Autowired
    private PartnerComponent partnerComponent;

    @Override
    public ResponseData executePayment(PaymentDTO paymentDTO) throws CustomException {
        logger.info("begin executePayment");
        checkValidPayment(paymentDTO);
        Payment payment = PaymentDTO.convertToEntity(paymentDTO);
        ResponseData responseData = sendRabbitMQ(payment);
        logger.info("end executePayment");
        return responseData;
    }

    /**
     * hàm thực thi kiểm tra dữ liệu thanh toán hợp lệ
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkValidPayment(PaymentDTO paymentDTO) throws CustomException {
        logger.info("begin checkValidPayment");
        checkBankCode(paymentDTO);
        checkDuplicateToken(paymentDTO);
        checkRealAmount(paymentDTO);
        checkPromotionCode(paymentDTO);
        checkMatchCheckSum(paymentDTO);
        logger.info("end checkValidPayment");
    }

    /**
     * hàm thực thi kiểm tra tokenKey trùng sau 0h
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkDuplicateToken(PaymentDTO paymentDTO) throws CustomException {
        logger.info("begin checkDuplicateToken");
        if (paymentRedisService.isExistedTokenKey(paymentDTO.getTokenKey())) {
            throw new CustomException(CustomCode.DUPLICATE_TOKEN_KEY);
        }
        logger.info("end checkDuplicateToken");
    }

    /**
     * hàm thực thi gửi dữ liệu thanh toán lên queue
     *
     * @param payment - {@link Payment}
     * @return responseData - {@link ResponseData}
     */
    protected ResponseData sendRabbitMQ(Payment payment) throws CustomException {
        logger.info("begin sendRabbitMQ");
        ResponseData responseData = rabbitMQService.send(payment);
        if (null == responseData)
            throw new CustomException(CustomCode.REQUEST_TIME_OUT);
        else if (CustomCode.SEND_PARTNER_FAIL.getStatusCode().equals(responseData.getCode()))
            throw new CustomException(CustomCode.SEND_PARTNER_FAIL);
        else if (CustomCode.UNKNOWN_ERROR.getStatusCode().equals(responseData.getCode()))
            throw new CustomException(CustomCode.UNKNOWN_ERROR);
        logger.info("end sendRabbitMQ");
        return responseData;
    }

    /**
     * hàm thực thi kiểm tra realAmount và debitAmount
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkRealAmount(PaymentDTO paymentDTO) throws CustomException {
        logger.info("begin checkRealAmount");
        if (Long.parseLong(paymentDTO.getRealAmount()) > paymentDTO.getDebitAmount())
            throw new CustomException(CustomCode.EXCEEDS_DEBIT);
        logger.info("end checkRealAmount");
    }

    /**
     * hàm thực thi kiểm tra promotionCode
     *
     * @param paymentDTO
     * @return void
     * @throws CustomException
     */
    protected void checkPromotionCode(PaymentDTO paymentDTO) throws CustomException {
        logger.info("begin checkPromotionCode");
        if (Long.parseLong(paymentDTO.getRealAmount()) != paymentDTO.getDebitAmount()) {
            if (null == paymentDTO.getPromotionCode() || paymentDTO.getPromotionCode().trim().isEmpty())
                throw new CustomException(CustomCode.PROMOTION_CODE_EMPTY);
        }
        logger.info("end checkPromotionCode");
    }

    /**
     * hash Payment để kiểm tra checkSum
     *
     * @param payment - {@link PaymentDTO}
     * @return checkSum - {@link ResponseData}
     */
    protected String hashPayment(PaymentDTO payment) {
        String accessKey = partnerComponent.getPartnerByCode(payment.getBankCode()).getPrivateKey();
        StringBuilder sumDataPayment = new StringBuilder().append(payment.getMobile())
                .append(payment.getBankCode())
                .append(payment.getAccountNo())
                .append(payment.getPayDate())
                .append(payment.getDebitAmount())
                .append(payment.getRespCode())
                .append(payment.getTraceTransfer())
                .append(payment.getMessageType())
                .append(accessKey);
        String checkSum = CommonUtils.hashData(sumDataPayment.toString());
        return checkSum;
    }

    /**
     * kiểm tra trùng khớp checkSum
     *
     * @param payment - {@link PaymentDTO}
     * @return void
     * @throws CustomException
     */
    protected void checkMatchCheckSum(PaymentDTO payment) throws CustomException {
        logger.info("begin checkMatchCheckSum");
        String checkSum = hashPayment(payment);
        if (!checkSum.equals(payment.getCheckSum()))
            throw new CustomException(CustomCode.CHECKSUM_NOT_MATCH);
        logger.info("end checkMatchCheckSum");
    }

    /**
     * kiểm tra bankCode hợp lệ
     *
     * @param payment - {@link PaymentDTO}
     * @return void
     * @throws CustomException
     */
    protected void checkBankCode(PaymentDTO payment) throws CustomException {
        logger.info("begin checkBankCode");
        if (null == partnerComponent.getPartnerByCode(payment.getBankCode()))
            throw new CustomException(CustomCode.BANK_CODE_INVALID);
        ThreadContext.put("bankCode", payment.getBankCode());
        logger.info("end checkBankCode");
    }

}
