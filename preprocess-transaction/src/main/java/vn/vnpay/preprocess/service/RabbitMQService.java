package vn.vnpay.preprocess.service;


import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.response.ResponseData;

public interface RabbitMQService {

    /**
     * hàm gửi dữ liệu thanh toán vào queue
     *
     * @param payment Payment
     * @return ResponseData
     */
    public ResponseData send(Payment payment);

}
