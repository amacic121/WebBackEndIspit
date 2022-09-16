package amacic.utils;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailUtil {
    public static boolean isNotValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return !validator.isValid(email);
    }
}
