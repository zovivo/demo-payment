package vn.vnpay.preprocess.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.preprocess.enu.CustomCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends Exception {

    private CustomCode customCode;
    private String message;

    public CustomException(CustomCode customCode){
       this.customCode = customCode;
       this.message = customCode.getDescription();
    }

}
