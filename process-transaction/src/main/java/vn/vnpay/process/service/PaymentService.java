package vn.vnpay.process.service;


import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.exception.CustomException;
import vn.vnpay.process.model.PaymentModel;
import vn.vnpay.process.response.ResponseData;

public interface PaymentService extends BaseService<Payment, Long> {

    /**
     * hàm thực thi xử lý dữ liệu thanh toán
     *
     * @param payment {@link PaymentModel}
     * @return responseData {@link ResponseData}
     * @throws Exception
     */
    public ResponseData executePayment(PaymentModel payment) throws RuntimeException, CustomException;

}
