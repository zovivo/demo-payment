package vn.vnpay.preprocess.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.preprocess.model.enu.ErrorCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends Exception {

    private ErrorCode errorCode;
    private String message;

    public CustomException(ErrorCode errorCode){
       this.errorCode = errorCode;
       this.message = errorCode.getDescription();
    }

}
