package hi.hugbo.verywowchat.entities;

/**
 * @Author Róman
 * This is a POJO that error messages from the API will be mapped to */
public class Error {

    private String field;
    private String message;

    public Error (String field, String message) {
        this.field = field;
        this.message = message;
    }
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
