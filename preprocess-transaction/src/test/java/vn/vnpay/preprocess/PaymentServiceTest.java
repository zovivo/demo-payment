package vn.vnpay.preprocess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.vnpay.preprocess.configuration.PartnerComponent;
import vn.vnpay.preprocess.dto.PaymentDTO;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.service.PaymentRedisService;
import vn.vnpay.preprocess.service.RabbitMQService;
import vn.vnpay.preprocess.service.impl.PaymentServiceImpl;
import vn.vnpay.preprocess.util.CommonUtils;
import vn.vnpay.preprocess.util.Partner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess
 * Author: zovivo
 * Date: 8/20/2021
 * Created with IntelliJ IDEA
 */

@ExtendWith(SpringExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PartnerComponent partnerComponent;
    @Mock
    private PaymentRedisService paymentRedisService;
    @Mock
    private RabbitMQService rabbitMQService;

    @Test
    public void whenExecutePayment() throws CustomException {
        Mockito.when(partnerComponent.getPartnerByCode("VNPAY")).thenReturn(new Partner("VNPAY", "908405"));
        Mockito.when(rabbitMQService.send(Mockito.any(Payment.class))).thenReturn(new ResponseData("200","Success"));
        Mockito.when(paymentRedisService.isExistedTokenKey(Mockito.any(String.class))).thenReturn(false);
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
        PaymentDTO paymentDTO = CommonUtils.parseStringToObject(inputPaymentData, PaymentDTO.class);
        ResponseData responseData = paymentService.executePayment(paymentDTO);
        Assertions.assertNotNull(responseData);
        assertThat(responseData.getCode()).isEqualTo("200");

    }
}
