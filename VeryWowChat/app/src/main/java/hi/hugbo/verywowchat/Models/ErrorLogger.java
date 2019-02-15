package hi.hugbo.verywowchat.Models;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.entities.Error;

/**
 * @Author Róman
 * This class is responsible for creating error messages POJO'S from a given json string
 * There should only be one instance of this class and no more.
 * */
public class ErrorLogger {

    /**
     * We want want to ensure that there will be only a single instance of this class
     * Mainly to prevent resource overuse since on phones resources are pretty tight
     * */
    private static final ErrorLogger ourInstance = new ErrorLogger();

    /**
     * This is used to create a singleton instance of this class, the empty class instantiator is needed.
     * */
    public static ErrorLogger getInstance() {
        return ourInstance;
    }
    private ErrorLogger() {}

    /**
     *  <pre>
     *  Usage : CreateListOfErrors(string errorsString)
     *    For : errorsString is a JsonArray object in string form
     *          example of a valid JsonArray
     *           [
     *             {
     *              "field": "Field Name",
     *              "message": "Error Message"
     *             }
     *           ]
     *   After: Returns a List of Error Objects where each Error object has the Field,Message attributes
     * </pre>
     *
     * @param errorsString json object in string form
     * @return returns List<Error>
     */
    public List<Error> CreateListOfErrors(String errorsString) throws JSONException {
        // Use JSONArray for mapping of object
        JSONArray errorsJson = new JSONArray(errorsString);
        // the return object
        List<Error> errorsPojos = new ArrayList<Error>();

        // itterate over the errors and map them to Error Objects
        for (int i = 0; i < errorsJson.length(); i++) {
            JSONObject error = errorsJson.getJSONObject(i);
            errorsPojos.add(new Error(error.getString("field"),error.getString("message")));
        }
        // return the Errors as POJOS
        return  errorsPojos;
    }


    /**
     *
     * @param errors
     * @return
     */
    public String ErrorsToString(List<Error> errors){

    }

}
