package vn.vnpay.payment.service;

import vn.vnpay.payment.model.PaymentDTO;

public interface RabbitMQService {

    public void send(PaymentDTO payment);

}
