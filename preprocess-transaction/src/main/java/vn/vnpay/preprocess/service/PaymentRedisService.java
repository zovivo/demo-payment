package vn.vnpay.preprocess.service;


import vn.vnpay.preprocess.model.Payment;

public interface PaymentRedisService {

    /**
     * hàm gửi lấy dữ liệu thanh toán bằng tokenKey từ cache Redis
     * @param tokenKey {@link String}
     * @return Payment
     * @throws
     */
    public Payment getByTokenKey(String tokenKey);

}
