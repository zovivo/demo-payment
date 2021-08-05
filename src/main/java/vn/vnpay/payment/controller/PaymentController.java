package vn.vnpay.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.PaymentDTO;
import vn.vnpay.payment.model.response.ResponseData;
import vn.vnpay.payment.service.PaymentService;
import vn.vnpay.payment.util.ResponsePreProcessor;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/payment/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> executePayment(@RequestBody @Validated PaymentDTO paymentDTO, HttpServletRequest request) throws CustomException {
        paymentService.executePayment(paymentDTO);
        ResponseData responseData = new ResponseData();
        responseData.setCode("00");
        responseData.setMessage("Success");
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, responseData, request);
    }

}
