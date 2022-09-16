package amacic.data;

import amacic.exceptions.ValidationException;
import amacic.utils.StringUtil;

public class Tag {

    private long id;
    private String value;


    public Tag() {
    }

    public Tag(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public void validate() {
        if (StringUtil.isEmpty(value)) {
            throw new ValidationException("Validation of tag unsuccessful");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


