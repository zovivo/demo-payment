package vn.vnpay.preprocess.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.vnpay.preprocess.configuration.PartnerComponent;
import vn.vnpay.preprocess.constant.CustomCode;
import vn.vnpay.preprocess.dto.PaymentDTO;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.service.PaymentRedisService;
import vn.vnpay.preprocess.service.RabbitMQService;
import vn.vnpay.preprocess.util.CommonUtils;
import vn.vnpay.preprocess.util.Partner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private List<Partner> partners;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        partners = new ArrayList<>();
        Partner partner1 = new Partner("VNPAY", "908405");
        Partner partner2 = new Partner("BIDV", "970466");
        Partner partner3 = new Partner("VIETCOMBANK", "970436");
        partners.add(partner1);
        partners.add(partner2);
        partners.add(partner3);
    }

    @AfterEach
    public void tearDown() {
        partners.clear();
    }

    @Test
    public void whenExecutePayment_ResponseNotNull() throws CustomException {
        Mockito.when(partnerComponent.getPartnerByCode(Mockito.anyString())).thenAnswer((Answer<Partner>) invocation -> {
            String bankCode = invocation.getArgument(0, String.class);
            return partners.stream()
                    .filter(partner -> partner.getBankCode().equals(bankCode))
                    .findAny().orElse(null);
        });
        Mockito.when(rabbitMQService.send(Mockito.any(Payment.class))).thenReturn(new ResponseData("200", "Success"));
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
    }

    @Test
    public void whenExecutePayment_ResponseSuccess() throws CustomException {
        Mockito.when(partnerComponent.getPartnerByCode("VNPAY")).thenReturn(new Partner("VNPAY", "908405"));
        Mockito.when(rabbitMQService.send(Mockito.any(Payment.class))).thenReturn(new ResponseData("200", "Success"));
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
        assertThat(responseData.getCode()).isEqualTo("200");
    }

    @Test
    public void whenExecutePayment_ThrowTimeoutException() {
        Mockito.when(partnerComponent.getPartnerByCode("VNPAY")).thenReturn(new Partner("VNPAY", "908405"));
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
        CustomException exception = assertThrows(CustomException.class, () -> {
            paymentService.executePayment(paymentDTO);
        });
        assertThat(exception.getCustomCode()).isEqualTo(CustomCode.REQUEST_TIME_OUT);
    }

    @Test
    public void whenCheckRealAmount_RealMoreThanDebit_ShouldThrowExceedsDebitException() {
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
                "\t\"realAmount\": \"11201\",\n" +
                "\t\"promotionCode\": \"   123 \",\n" +
                "\t\"addValue\": \"{\\\"payMethod\\\":\\\"01\\\",\\\"payMethodMMS\\\":1}\"" +
                "}\n";
        PaymentDTO paymentDTO = CommonUtils.parseStringToObject(inputPaymentData, PaymentDTO.class);
        CustomException exception = assertThrows(CustomException.class, () -> {
            paymentService.checkRealAmount(paymentDTO);
        });
        assertThat(exception.getCustomCode()).isEqualTo(CustomCode.EXCEEDS_DEBIT);
    }

    @Test
    public void whenCheckPromotionCode_RealNotEqualDebit_ShouldThrowPromotionException() {
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
                "\t\"realAmount\": \"11119\",\n" +
                "\t\"promotionCode\": \"       \",\n" +
                "\t\"addValue\": \"{\\\"payMethod\\\":\\\"01\\\",\\\"payMethodMMS\\\":1}\"" +
                "}\n";
        PaymentDTO paymentDTO = CommonUtils.parseStringToObject(inputPaymentData, PaymentDTO.class);
        CustomException exception = assertThrows(CustomException.class, () -> {
            paymentService.checkPromotionCode(paymentDTO);
        });
        assertThat(exception.getCustomCode()).isEqualTo(CustomCode.PROMOTION_CODE_EMPTY);
    }

    @Test
    public void whenCheckMatchCheckSum_CheckSumNotMatch_ShouldThrowCheckSumException() {
        Mockito.when(partnerComponent.getPartnerByCode("VNPAY")).thenReturn(new Partner("VNPAY", "908405"));
        Mockito.when(paymentRedisService.isExistedTokenKey(Mockito.any(String.class))).thenReturn(false);
        String inputPaymentData = "{\n" +
                "\"tokenKey\": \"1601353776839FT19310RH6P2\",\n" +
                "\t\"apiID\": \"restPayment\",\n" +
                "\t\"mobile\": \"0345225630\",\n" +
                "\t\"bankCode\": \"VNPAY\",\n" +
                "\t\"accountNo\": \"0001100014211002999999\",\n" +
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
                "\t\"realAmount\": \"11200\",\n" +
                "\t\"promotionCode\": \"       \",\n" +
                "\t\"addValue\": \"{\\\"payMethod\\\":\\\"01\\\",\\\"payMethodMMS\\\":1}\"" +
                "}\n";
        PaymentDTO paymentDTO = CommonUtils.parseStringToObject(inputPaymentData, PaymentDTO.class);
        CustomException exception = assertThrows(CustomException.class, () -> {
            paymentService.checkMatchCheckSum(paymentDTO);
        });
        assertThat(exception.getCustomCode()).isEqualTo(CustomCode.CHECKSUM_NOT_MATCH);
    }

    @Test
    public void whenHashPayment_CheckIsMatch() {
        Mockito.when(partnerComponent.getPartnerByCode("VNPAY")).thenReturn(new Partner("VNPAY", "908405"));
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
                "\t\"realAmount\": \"11200\",\n" +
                "\t\"promotionCode\": \"       \",\n" +
                "\t\"addValue\": \"{\\\"payMethod\\\":\\\"01\\\",\\\"payMethodMMS\\\":1}\"" +
                "}\n";
        PaymentDTO paymentDTO = CommonUtils.parseStringToObject(inputPaymentData, PaymentDTO.class);
        String checkSum = paymentService.hashPayment(paymentDTO);
        assertEquals(paymentDTO.getCheckSum(), checkSum);
    }

}
