package hi.hugbo.verywowchat.Models.Helpers;

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
     *  Usage : ErrorLogger.CreateListOfErrors(string errorsString)
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
        List<Error> errorsPojos = new ArrayList<Error>() ; //initialize the constructor
        JSONArray errorsJson = new JSONArray(errorsString); // parse the string into a JSONARRAY

        // Iterate over each json object
        for (int i = 0; i < errorsJson.length(); i++){
            // get the current Json object
            JSONObject errorJson = errorsJson.getJSONObject(i);
            // map the error json object to error pojo
            Error newError = new Error(errorJson.getString("field").toString(),errorJson.get("message").toString());
            // Add the newError to Errors List
            errorsPojos.add(newError);
        }

        return errorsPojos;
    }

    /** Overload if u want to return a JSONArray object that is also possible */
    public List<Error> CreateListOfErrors(JSONArray errorsJson) throws JSONException {
        List<Error> errorsPojos = new ArrayList<Error>() ; //initialize the constructor

        // itarate over each json object
        for (int i = 0; i < errorsJson.length(); i++){
            // get the current Json object
            JSONObject errorJson = errorsJson.getJSONObject(i);
            // map the error json object to error pojo
            Error newError = new Error(errorJson.getString("field").toString(),errorJson.get("message").toString());
            // Add the newError to Errors List
            errorsPojos.add(newError);
        }

        return errorsPojos;
    }


    /**
     * <pre>
     *  Usage : ErrorLogger.ErrorsToString(List<Error> errors);
     *    For : errors is a List of Error Pojos
     *   After: Reads all the Error Objects and concatenates them into one string
     *          Each error message has its own Line.
     * </pre>
     * @param errors is a List<Error>
     * @return All Error.message's concatenated into one string
     */
    public String ErrorsToString(List<Error> errors){
        String returnString = "";
        // Iterate over each Error Pojo
        for(int i =0; i < errors.size(); i++) {
            // append the error message to the return string and make a new line
            returnString += errors.get(i).getMessage()+" \n";
        }
        return returnString;
    }

}
