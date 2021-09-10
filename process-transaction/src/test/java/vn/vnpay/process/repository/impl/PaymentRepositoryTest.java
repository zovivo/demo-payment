package vn.vnpay.process.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpay.process.configuration.HibernateConfigTest;
import vn.vnpay.process.entity.Order;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.util.CommonUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project: demo-payment
 * Package: vn.vnpay.process.repository.impl
 * Author: zovivo
 * Date: 9/8/2021
 * Created with IntelliJ IDEA
 */

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {HibernateConfigTest.class})
//@Transactional
public class PaymentRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field entityField = PaymentRepositoryImpl.class.getSuperclass().getDeclaredField("entityManager");
        entityField.setAccessible(true);
        entityField.set(paymentRepository, entityManager);
    }

    @Test
    @Transactional
    public void whenInsertPayment_Success() {
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
        Payment paymentEntity = CommonUtils.parseStringToObject(inputPaymentData, Payment.class);
        paymentRepository.insert(paymentEntity);
        Assertions.assertNotNull(paymentEntity.getId());
    }

    @Test
    public void whenInsertPayment_NoTransactional_ShouldThrowTransactionException() {
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
        Payment paymentEntity = CommonUtils.parseStringToObject(inputPaymentData, Payment.class);
        Exception exception = assertThrows(Exception.class, () -> {
            paymentRepository.insert(paymentEntity);
        });
        assertTrue(exception instanceof TransactionRequiredException);
    }

    @Test
    @Transactional
    public void whenInsertPayment_NullPayment_ShouldThrowException() {
        Payment paymentEntity = null;
        Exception exception = assertThrows(Exception.class, () -> {
            paymentRepository.insert(paymentEntity);
        });
        assertTrue(exception instanceof IllegalArgumentException);
    }

    @Test
    @Transactional
    public void whenInsertPayment_ProgrammaticTransaction_Success() {
        Assertions.assertTrue(TestTransaction.isActive());
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
        Payment paymentEntity = CommonUtils.parseStringToObject(inputPaymentData, Payment.class);
        paymentRepository.insert(paymentEntity);
        long id = paymentEntity.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
        Payment payment = paymentRepository.find(id);
        TestTransaction.end();
        Assertions.assertNotNull(payment);
    }

    @Test
    @Transactional
    public void whenInsertPayment_ProgrammaticTransaction_NotCommit_NotInsertDB() {
        Assertions.assertTrue(TestTransaction.isActive());
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
        Payment paymentEntity = CommonUtils.parseStringToObject(inputPaymentData, Payment.class);
        paymentRepository.insert(paymentEntity);
        Long id = paymentEntity.getId();
        TestTransaction.end();
        TestTransaction.start();
        Payment payment = paymentRepository.find(id);
        TestTransaction.end();
        Assertions.assertNull(payment);
    }

    @Test
    @Transactional
    public void whenInsertPayment_Rollback() {
        Assertions.assertTrue(TestTransaction.isActive());
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
        Payment paymentEntity = CommonUtils.parseStringToObject(inputPaymentData, Payment.class);
        paymentRepository.insert(paymentEntity);
        long id = paymentEntity.getId();
        TestTransaction.flagForRollback();
        TestTransaction.end();
        TestTransaction.start();
        assertTrue(TestTransaction.isFlaggedForRollback());
        Payment payment = paymentRepository.find(id);
        Assertions.assertNull(payment);
    }

    @Test
    @Transactional
    public void whenInsertPayment_InsertOrder() {
        Order order1 = new Order(100l, "Order1", "O1");
        Order order2 = new Order(200l, "Order2", "O2");
        Order order3 = new Order(300l, "Order3", "O3");
        List<Order> orders = new ArrayList<>(Arrays.asList(order1, order2, order3));
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
        Payment paymentEntity = CommonUtils.parseStringToObject(inputPaymentData, Payment.class);
        paymentEntity.setOrders(orders);
        paymentRepository.insert(paymentEntity);
        long orderId = order1.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
        order1 = entityManager.find(Order.class, orderId);
        Assertions.assertNotNull(order1);
    }

    @Test
    @Transactional
    public void whenInsertPayment_InsertOrderPaymentLink() {
        Order order1 = new Order(100l, "Order1", "O1");
        List<Order> orders = new ArrayList<>(Arrays.asList(order1));
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
        Payment paymentEntity = CommonUtils.parseStringToObject(inputPaymentData, Payment.class);
        order1.setPayment(paymentEntity);
        paymentEntity.setOrders(orders);
        paymentRepository.insert(paymentEntity);
        long orderId = order1.getId();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
        order1 = entityManager.find(Order.class, orderId);
        Assertions.assertNotNull(order1.getPayment());
    }

}
