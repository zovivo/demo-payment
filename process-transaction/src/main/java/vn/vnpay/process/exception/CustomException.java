package vn.vnpay.process.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.process.constant.CustomCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends Exception {

    private CustomCode customCode;

}
