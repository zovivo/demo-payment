package vn.vnpay.preprocess.service;


import vn.vnpay.preprocess.model.Payment;

public interface RabbitMQService {

    /**
     * hàm gửi dữ liệu thanh toán vào queue
     * @param payment Payment
     * @return void
     * @throws
     */
    public void send(Payment payment);

}
