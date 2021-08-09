package vn.vnpay.preprocess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.model.dto.PaymentDTO;
import vn.vnpay.preprocess.model.response.ResponseData;
import vn.vnpay.preprocess.service.PaymentService;
import vn.vnpay.preprocess.util.ResponsePreProcessor;

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
        ResponseData responseData = paymentService.executePayment(paymentDTO);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, responseData, request);
    }

}
