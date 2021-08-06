package vn.vnpay.payment.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpay.payment.entity.Payment;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.PaymentDTO;
import vn.vnpay.payment.model.enu.ErrorCode;
import vn.vnpay.payment.model.response.ResponseData;
import vn.vnpay.payment.repository.PaymentRepository;
import vn.vnpay.payment.service.PaymentService;
import vn.vnpay.payment.service.RabbitMQService;
import vn.vnpay.payment.util.CommonUtils;
import vn.vnpay.payment.util.CustomRestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service(value = "paymentService")
public class PaymentServiceImpl extends BaseServiceImpl<PaymentRepository, Payment, Long> implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    @Autowired
    @Qualifier(value = "paymentRepository")
    private PaymentRepository paymentRepository;

    @Autowired
    @Qualifier(value = "paymentRepositoryRedis")
    private PaymentRepository paymentRepositoryRedis;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private CustomRestTemplate customRestTemplate;

    protected void checkDuplicateToken(PaymentDTO paymentDTO) throws CustomException {
        Payment payment = paymentRepositoryRedis.getByTokenKey(paymentDTO.getTokenKey());
        if (payment != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date lastPayDate = CommonUtils.parseStringToDate(payment.getPayDate().substring(0, 8), dateFormat);
            Date newPayDate = CommonUtils.parseStringToDate(paymentDTO.getPayDate().substring(0, 8), dateFormat);
            if (lastPayDate.getTime() == newPayDate.getTime())
                throw new CustomException(ErrorCode.DUPLICATE_TOKEN_KEY);
        }
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

    protected void sendRabbitMQ(PaymentDTO payment) {
        rabbitMQService.send(payment);
    }

    protected void saveRedis(Payment payment) {
        paymentRepositoryRedis.insert(payment);
    }

    protected ResponseData sendToPartner() throws CustomException {
        String inputStr = "{\n" +
                "\t\"tokenKey\": \"1601353776839FT19310RH6P1\",\n" +
                "\t\"apiID\": \"restPayment\",\n" +
                "\t\"mobile\": \"0145225630\",\n" +
                "\t\"bankCode\": \"970445\",\n" +
                "\t\"accountNo\": \"0001100014211002\",\n" +
                "\t\"payDate\": \"20200929112923\",\n" +
                "\t\"addtionalData\": \"\",\n" +
                "\t\"debitAmount\": 11200,\n" +
                "\t\"respCode\": \"00\",\n" +
                "\t\"respDesc\": \"SUCCESS\",\n" +
                "\t\"traceTransfer\": \"FT19310RH6P1\",\n" +
                "\t\"messageType\": \"1\",\n" +
                "\t\"checkSum\": \"40e670720b754324af3d3a0ff49b52fb\",\n" +
                "\t\"orderCode\": \"FT19310RH6P1\",\n" +
                "\t\"userName\": \"cntest001\",\n" +
                "\t\"realAmount\": \"11200\",\n" +
                "\t\"promotionCode\": \"\",\n" +
                "\t\"queueNameResponse\": \"queue.payment.qrcodeV2.restPayment\",\n" +
                "\t\"addValue\": \"{\\\"payMethod\\\":\\\"01\\\",\\\"payMethodMMS\\\":1}\"\n" +
                "}\n";
        Object input = CommonUtils.parseStringToObject(inputStr, Object.class);
        ResponseEntity<Object> response = customRestTemplate.postForObject(input);
        logger.info("response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful())
            throw new CustomException(ErrorCode.SEND_PARTNER_FAIL);
        ResponseData responseData = new ResponseData();
        responseData.setCode(response.getStatusCode().value() + "");
        responseData.setMessage("Success");
        return responseData;
    }

    @Override
    @Transactional
    public ResponseData executePayment(PaymentDTO paymentDTO) throws CustomException {
//        checkValidPayment(paymentDTO);
//        sendRabbitMQ(paymentDTO);
//        Payment payment = PaymentDTO.convertToEntity(paymentDTO);
//        payment = create(payment);
//        saveRedis(payment);
        ResponseData responseData = sendToPartner();
        return responseData;
    }

    @Override
    public PaymentRepository getRepository() {
        return paymentRepository;
    }
}
