package vn.vnpay.preprocess.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

    @Override
    public void initialize(final DateFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.getDefault())
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            dateFormatter.parse(value);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}