package amacic.data;

import amacic.exceptions.ValidationException;
import amacic.utils.EmailUtil;
import amacic.utils.StringUtil;

public class User {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String userType;
    private String status;
    private long id;

    public User() {
    }

    public User(long id, String email, String firstName, String lastName, String userType, String status, String password) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.status = status;
        this.password = password;
    }

    public void validate() {
        if (StringUtil.isEmpty(email)) {
            throw new ValidationException("Validation of user unsuccessful, email is empty");
        } else if (EmailUtil.isNotValidEmailAddress(email)) {
            throw new ValidationException("Validation of user unsuccessful, email is invalid");
        }else if (StringUtil.isEmpty(firstName)) {
            throw new ValidationException("Validation of user unsuccessful, first name is empty");
        } else if (StringUtil.isEmpty(lastName)) {
            throw new ValidationException("Validation of user unsuccessful, last name is empty");
        } else if (StringUtil.isEmpty(userType)) {
            throw new ValidationException("Validation of user unsuccessful, user type is empty");
        } else if (StringUtil.isEmpty(status)) {
            throw new ValidationException("Validation of user unsuccessful, status is empty");
        } else if (StringUtil.isEmpty(password)) {
            throw new ValidationException("Validation of user unsuccessful, password is empty");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}