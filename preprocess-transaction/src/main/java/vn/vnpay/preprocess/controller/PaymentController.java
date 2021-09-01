package vn.vnpay.preprocess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.preprocess.dto.PaymentDTO;
import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.response.ResponseData;
import vn.vnpay.preprocess.service.PaymentService;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/payment/")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> executePayment(@RequestBody @Validated PaymentDTO paymentDTO, HttpServletRequest request) throws CustomException {
        putKeyToThread(paymentDTO.getTokenKey());
        ResponseData responseData = paymentService.executePayment(paymentDTO);
        return buildResponseEntity(HttpStatus.OK, responseData, request);
    }

}
