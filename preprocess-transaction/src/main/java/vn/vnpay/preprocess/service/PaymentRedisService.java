package vn.vnpay.preprocess.service;


import vn.vnpay.preprocess.model.Payment;

public interface PaymentRedisService {

    /**
     * hàm gửi lấy dữ liệu thanh toán bằng tokenKey từ cache Redis
     *
     * @param tokenKey {@link String}
     * @return Payment
     */
    public Payment getByTokenKey(String tokenKey);

    /**
     * hàm gửi kiểm tra tokenKey đã tồn tại trong cache Redis
     *
     * @param tokenKey {@link String}
     * @return boolean
     */
    public boolean isExistedTokenKey(String tokenKey);

}
