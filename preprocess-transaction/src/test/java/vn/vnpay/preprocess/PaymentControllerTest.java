package vn.vnpay.preprocess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.util.CommonUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

/**
 * Project: demo-payment
 * Package: vn.vnpay.preprocess
 * Author: zovivo
 * Date: 8/17/2021
 * Created with IntelliJ IDEA
 */

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testPaymentAPI() throws Exception {

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

        mvc.perform(MockMvcRequestBuilders.post("/payment/")
                .content(inputPaymentData).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"message\":\"Success\"")));
        ;

    }

    @Test
    public void testExecutePayment() throws Exception {

        String inputPapymentData = "{\n" +
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

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/payment/")
                .content(inputPapymentData).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ResponseData responseData = CommonUtils.parseStringToObject(content,ResponseData.class);
        Assertions.assertEquals("SUCCESS",responseData.getMessage());

    }

}
