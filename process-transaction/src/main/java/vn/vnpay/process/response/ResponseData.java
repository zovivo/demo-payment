package vn.vnpay.process.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.process.constant.CustomCode;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData implements Serializable {

    private String code;
    private String message;
    private String responseId;
    private String checkSum;
    private Object data;
    private String addValue;

    public ResponseData(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseData(CustomCode customCode) {
        this.code = customCode.getStatusCode();
        this.message = customCode.getDescription();
    }

}
