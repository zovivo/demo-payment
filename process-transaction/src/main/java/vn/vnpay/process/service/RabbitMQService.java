package vn.vnpay.process.service;


import vn.vnpay.process.model.PaymentModel;

public interface RabbitMQService {

    public void send(PaymentModel payment);

}
