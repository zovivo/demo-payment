package vn.vnpay.preprocess.service;


import vn.vnpay.preprocess.model.Payment;

public interface PaymentRedisService {

    public Payment getByTokenKey(String tokenKey);

}
