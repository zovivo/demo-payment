package vn.vnpay.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.model.PaymentDTO;
import vn.vnpay.payment.model.response.ResponseData;
import vn.vnpay.payment.service.PaymentService;
import vn.vnpay.payment.util.ResponsePreProcessor;


@RestController
@RequestMapping(value = "/payment/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> executePayment(@RequestBody PaymentDTO paymentDTO) throws CustomException {
        paymentService.executePayment(paymentDTO);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, new ResponseData("00", "Success", "Mã duy nhất mỗi duy request", "", ""));
    }

}
