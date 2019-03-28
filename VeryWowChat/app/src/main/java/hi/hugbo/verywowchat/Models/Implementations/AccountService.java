package hi.hugbo.verywowchat.Models.Implementations;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import hi.hugbo.verywowchat.Models.Helpers.API_caller;
import hi.hugbo.verywowchat.Models.Helpers.ErrorLogger;
import hi.hugbo.verywowchat.Models.Interfaces.IAccountService;
import hi.hugbo.verywowchat.entities.Error;

/**
 * @Author Róman
 * AccountService is responsible for making HTTP Requests that have anything to do with
 * the account of the user such as Register new account , Login etc..
 * there should be only one instance of this service.
 * */
public class AccountService implements IAccountService {

    // need this for making singletons
    private static final AccountService ourInstance = new AccountService();
    public static AccountService getInstance() {
        return ourInstance;
    }
    private AccountService() { }

    /**
     * The ErrorLogger is used to map Json String objects into POJOS and it also should be
     * a singleton so we are dependant on receiving a instance of it from someone else
     * */
    private ErrorLogger errorLogger = ErrorLogger.getInstance();
    /**
     * The api_caller is used to send http requests to the VeryWowChat server
     * and also it is a singleton so we are dependant on reciving a instance of it from someone else
     * */
    private API_caller api_caller = API_caller.getInstance();

    public String Login(Map<String, String> params, Context context, SharedPreferences UserInfo) {
        /*
         *Send the HTTP request for login through the api_caller and then map the object
         *correctly based of the status code
         **/
        try {
            // Make the HTTP Request
            Map<String, String> result = api_caller.HttpRequest("login","POST","",params);
            // Parse the HTTP status code
            int status = Integer.parseInt(result.get("status"));
            // Parse the Json String into a JSON array
            JSONArray resp_body = new JSONArray(result.get("response"));

            // HTTP Request was a success
            if(status >= 200 && status < 300 ){
                /*
                 *Since its a successful request we know we should receive user information
                 *so we store this information in the shared preferences so that next time
                 *the use opens the app he will already be logged in
                 **/
                SharedPreferences.Editor editor = UserInfo.edit();
                editor.putString("username",resp_body.getJSONObject(0).get("username").toString());
                editor.putString("displayname",resp_body.getJSONObject(0).get("displayname").toString());
                editor.putString("token",resp_body.getJSONObject(0).get("token").toString());
                editor.commit(); // ALWAYS REMEMBER TO COMMIT ELSE IT WONT SAVE !
                return null;
            }

            // HTTP Request was a failure
            if(status >= 400 && status < 500){
                /*
                 *Since its an error we know we receive a array of errors which we have to
                 *map into POJOS and then display them.
                 *(NOTE : You do not have to map them to POJOS in my opinion it just adds more work
                 *        and drains phone resources u can solve everything with JSONArray and JSONObject classes)
                 **/
                // Create a List of errors
                List<Error> errors = errorLogger.CreateListOfErrors(resp_body.getJSONObject(0).getJSONArray("errors"));
                // Display a pop-up error message to the user with the errors received from the API
                return  errorLogger.ErrorsToString(errors);
            }
        } catch (IOException e) {
            Log.e("LoginError","IOException in Login \n message :"+e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("LoginError","JSONException in Login \n message :"+e);
            e.printStackTrace();
        }
        return "Something went wrong Unknown Error!";
    }

    public String Register(Map<String, String> params) {
        return null;
    }
}
