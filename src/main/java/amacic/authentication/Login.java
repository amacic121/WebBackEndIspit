package amacic.authentication;

import amacic.exceptions.ValidationException;
import amacic.utils.EmailUtil;
import amacic.utils.StringUtil;

public class Login {

    private String email;
    private String password;

    public Login() {
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void validate() {
        if (StringUtil.isEmpty(email)) {
            throw new ValidationException("Validation of login unsuccessful, email is invalid");
        } else if (EmailUtil.isNotValidEmailAddress(email)) {
            throw new ValidationException("Validation of login unsuccessful, email is invalid");
        } else if (StringUtil.isEmpty(password)) {
            throw new ValidationException("Validation of login unsuccessful, password is invalid");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
