package vn.vnpay.preprocess.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
