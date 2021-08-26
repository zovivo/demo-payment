package vn.vnpay.preprocess.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrBlankValidator implements ConstraintValidator<NullOrBlank, String> {

    @Override
    public void initialize(final NullOrBlank constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (null == value)
            return false;
        return !value.trim().isEmpty();
    }
}