package vn.vnpay.process;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.repository.PaymentRepository;
import vn.vnpay.process.repository.redis.PaymentRepositoryRedis;
import vn.vnpay.process.response.ResponseData;
import vn.vnpay.process.service.impl.PaymentServiceImpl;
import vn.vnpay.process.util.CommonUtils;
import vn.vnpay.process.util.CustomRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Project: demo-payment
 * Package: vn.vnpay.process
 * Author: zovivo
 * Date: 8/20/2021
 * Created with IntelliJ IDEA
 */

@ExtendWith(SpringExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private CustomRestTemplate customRestTemplate;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentRepositoryRedis paymentRepositoryRedis;

    @Test
    public void whenExecutePayment() throws CustomException {
        Mockito.when(customRestTemplate.postForObject(Mockito.any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        String inputPaymentData = "{\n" +
                "\"tokenKey\": \"1601353776839FT19310RH6P2\",\n" +
                "\t\"apiID\": \"restPayment\",\n" +
                "\t\"mobile\": \"0345225630\",\n" +
                "\t\"bankCode\": \"VNPAY\",\n" +
                "\t\"accountNo\": \"0001100014211002\",\n" +
                "\t\"payDate\": \"20200911112923\",\n" +
                "\t\"additionalData\": \"\",\n" +
                "\t\"debitAmount\": 11200,\n" +
                "\t\"respCode\": \"00\",\n" +
                "\t\"respDesc\": \"SUCCESS\",\n" +
                "\t\"traceTransfer\": \"FT19310RH6P1\",\n" +
                "\t\"messageType\": \"1\",\n" +
                "\t\"checkSum\": \"18b42dc90b1a9a3121ba71474c7002280b0a596941a6df06d18ad53ca147c93d\",\n" +
                "\t\"orderCode\": \"FT19310RH6P1\",\n" +
                "\t\"userName\": \"cntest001\",\n" +
                "\t\"realAmount\": \"11100\",\n" +
                "\t\"promotionCode\": \"   123 \",\n" +
                "\t\"addValue\": \"{\\\"payMethod\\\":\\\"01\\\",\\\"payMethodMMS\\\":1}\"" +
                "}\n";
        PaymentModel paymentModel = CommonUtils.parseStringToObject(inputPaymentData, PaymentModel.class);
        Mockito.when(paymentRepository.update(Mockito.any(Payment.class))).thenReturn(PaymentModel.convertToEntity(paymentModel));
        ResponseData responseData = paymentService.executePayment(paymentModel);
        Assertions.assertNotNull(responseData);
        assertThat(responseData.getCode()).isEqualTo("200");
        Payment paymentResult = (Payment) responseData.getData();
        Assertions.assertNotNull(paymentResult);
        Assertions.assertEquals("1601353776839FT19310RH6P2", paymentResult.getTokenKey());
        Assertions.assertEquals("VNPAY", paymentResult.getBankCode());
        Assertions.assertEquals("restPayment", paymentResult.getApiID());
        Assertions.assertEquals("18b42dc90b1a9a3121ba71474c", paymentResult.getCheckSum());
    }
}
