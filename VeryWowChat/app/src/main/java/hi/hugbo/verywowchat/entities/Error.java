package hi.hugbo.verywowchat.entities;

/**
 * @Author : Róman
 * The object that the errors from the servers response will be mapped to
 * */
public class Error {
    String field;

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

    String message;
}
