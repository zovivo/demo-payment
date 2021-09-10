package vn.vnpay.process.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpay.process.configuration.HibernateConfigTest;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.repository.PaymentRepository;
import vn.vnpay.process.repository.impl.PaymentRepositoryImpl;
import vn.vnpay.process.repository.redis.PaymentRepositoryRedis;
import vn.vnpay.process.response.ResponseData;
import vn.vnpay.process.util.CommonUtils;
import vn.vnpay.process.util.CustomRestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;

/**
 * Project: demo-payment
 * Package: vn.vnpay.process.service.impl
 * Author: zovivo
 * Date: 9/9/2021
 * Created with IntelliJ IDEA
 */
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {HibernateConfigTest.class})
public class PaymentServiceTransactionTest {

    @PersistenceContext
    private EntityManager entityManager;

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private CustomRestTemplate customRestTemplate;
    @Mock
    private PaymentRepositoryRedis paymentRepositoryRedis;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field entityField = PaymentRepositoryImpl.class.getSuperclass().getDeclaredField("entityManager");
        entityField.setAccessible(true);
        entityField.set(paymentRepository, entityManager);
        Field repositoryField = PaymentServiceImpl.class.getDeclaredField("paymentRepository");
        repositoryField.setAccessible(true);
        repositoryField.set(paymentService, paymentRepository);
    }

    @Test
    public void whenExecutePayment_InsertPayment_Success() throws CustomException {
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
        ResponseData responseData = paymentService.executePayment(paymentModel);
        Payment payment= paymentService.find(1L);
        Assertions.assertNotNull(payment);
    }

}
