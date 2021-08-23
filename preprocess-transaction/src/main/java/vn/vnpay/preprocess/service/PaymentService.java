package vn.vnpay.preprocess.service;

import vn.vnpay.preprocess.exception.CustomException;
import vn.vnpay.preprocess.dto.PaymentDTO;
import vn.vnpay.preprocess.response.ResponseData;

public interface PaymentService {

    /**
     * hàm thực thi tiền xử lý thanh toán
     * @param paymentDTO PaymentDTO
     * @return ResponseData
     * @throws CustomException
     */
    public ResponseData executePayment(PaymentDTO paymentDTO) throws CustomException;

}
