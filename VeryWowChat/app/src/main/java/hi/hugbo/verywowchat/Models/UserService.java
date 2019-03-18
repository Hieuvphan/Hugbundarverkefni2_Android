package hi.hugbo.verywowchat.Models;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * @Author Róman
 * This service is responsible for making HTTP requests that have anything
 * to do with updating or deleting the user.
 * We also want this service to a singleton.
 * */
public class UserService {

    private static final UserService ourInstance = new UserService();
    private API_caller api_caller = API_caller.getInstance(); // need for making HTTP calls

    // need this for making singletons
    public static UserService getInstance() {
        return ourInstance;
    }
    private UserService() {
    }

    /**
     * <pre>
     *     Usage : UserService.UpdateUser(requestBody,token)
     *       For : requestBody is a Map<String,String>
     *             token is a string
     *     After : Sends the HTTP PATCH to endpoint/auth/user with the request body and
     *             the users jwt token.
     * </pre>
     * @param requestbody data for updating the user
     * @param token User's JWT
     */
    public void UpdateUser(Map<String,String> requestbody,String token) throws Exception {
        // Make the HTTP Request
        Map<String,String> result = api_caller.HttpRequest("auth/user/","PATCH",token,requestbody);
        // Parse the HTTP status code
        int status = Integer.parseInt(result.get("status"));
        // if the result is anything but 200 that means we have an error
        if(status != 200) {
            JSONObject errorMsg = new JSONObject(result.get("response"));
            throw new Exception(errorMsg.get("error").toString());
        }
    }

    /**
     * <pre>
     *     Usage : UserService.DeleteMe(token)
     *       For : token is a string (users JWT)
     *     After : Sends a HTTP DELETE Request to endpoint/auth/user
     *             to delete the account who is linked to the token.
     * </pre>
     * @param token users json web token
     */
    public void DeleteMe(String token) throws Exception {

        // Make the HTTP Request
        Map<String, String> result = api_caller.HttpRequest("auth/user/", "DELETE", token, null);
        // Parse the HTTP status code
        int status = Integer.parseInt(result.get("status"));
        // if the result is anything but 204 that means we have an error
        if(status != 204) {
           JSONObject errorMsg = new JSONObject(result.get("response"));
           throw new Exception(errorMsg.get("error").toString());
        }
    }
}
