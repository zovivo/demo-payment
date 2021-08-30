package vn.vnpay.preprocess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.vnpay.preprocess.controller.PaymentController;

@SpringBootTest
class PreprocessTransactionApplicationTests {

    @Autowired
    private PaymentController paymentController;


    @Test
    void contextLoads() {
    }

}
