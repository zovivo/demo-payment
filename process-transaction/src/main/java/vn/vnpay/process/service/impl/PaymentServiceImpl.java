package vn.vnpay.process.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.model.enu.ErrorCode;
import vn.vnpay.process.model.response.ResponseData;
import vn.vnpay.process.repository.PaymentRepository;
import vn.vnpay.process.service.PaymentService;
import vn.vnpay.process.util.CommonUtils;
import vn.vnpay.process.util.CustomRestTemplate;


@Service(value = "paymentService")
public class PaymentServiceImpl extends BaseServiceImpl<PaymentRepository, Payment, Long> implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    @Autowired
    private CustomRestTemplate customRestTemplate;

    @Autowired
    @Qualifier(value = "paymentRepository")
    private PaymentRepository paymentRepository;

    @Autowired
    @Qualifier(value = "paymentRepositoryRedis")
    private PaymentRepository paymentRepositoryRedis;


    protected void saveRedis(Payment payment) {
        logger.info("===== begin saveRedis =====");
        paymentRepositoryRedis.insert(payment);
        logger.info("===== end saveRedis =====");
    }

    public void saveDB(Payment payment) {
        logger.info("===== begin saveDB =====");
        paymentRepository.insert(payment);
        logger.info("===== end saveDB =====");
    }

    protected ResponseData sendToPartner() throws CustomException {
        logger.info("===== begin sendToPartner =====");
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
        logger.info("===== end sendToPartner =====");
        return responseData;
    }

    @Override
    @Transactional
    public ResponseData executePayment(PaymentModel paymentModel) throws CustomException {
        logger.info("===== begin executePayment =====");
        Payment payment = PaymentModel.convertToEntity(paymentModel);
        saveDB(payment);
        saveRedis(payment);
        ResponseData responseData = sendToPartner();
        responseData.setData(payment);
        logger.info("===== end executePayment =====");
        return responseData;
    }

    @Override
    public PaymentRepository getRepository() {
        return paymentRepository;
    }
}
