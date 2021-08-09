package vn.vnpay.process.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.model.response.ResponseData;
import vn.vnpay.process.service.PaymentService;
import vn.vnpay.process.util.ResponsePreProcessor;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/payment/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> executePayment(@RequestBody @Validated PaymentModel paymentModel, HttpServletRequest request) throws CustomException {
        ResponseData responseData = paymentService.executePayment(paymentModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, responseData, request);
    }

}
