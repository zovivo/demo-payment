package vn.vnpay.preprocess.service;


import vn.vnpay.preprocess.model.Payment;

public interface RabbitMQService {

    public void send(Payment payment);

}
