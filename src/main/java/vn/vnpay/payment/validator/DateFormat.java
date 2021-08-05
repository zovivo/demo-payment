package vn.vnpay.payment.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DateFormatValidator.class)
@Documented
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface DateFormat {
    String message() default "wrong date format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
