package hi.hugbo.verywowchat.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/***/
public class Errors {

    List<Error> errors;

    // public constructor is necessary for collections
    public Errors() {
        errors = new ArrayList<Error>();
    }

    public static Errors parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        Errors errors = gson.fromJson(response, Errors.class);
        return errors;
    }


    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
