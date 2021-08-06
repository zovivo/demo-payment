package vn.vnpay.payment.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

    private String code;
    private String message;
    private String responseId;
    private String checkSum;
    private String addValue;

    public ResponseData(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
