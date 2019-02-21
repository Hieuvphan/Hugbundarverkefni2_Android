package hi.hugbo.verywowchat.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hi.hugbo.verywowchat.Models.API_caller;
import hi.hugbo.verywowchat.Models.ErrorLogger;
import hi.hugbo.verywowchat.entities.Error;

/**
 * @Author : Róman
 * This Activity is responsible for displaying and maitaining the login form and redirecting
 * the user to his/her home page if he/she is logged in.
 * */
public class LoginActivity extends AppCompatActivity {

    /**
     * The login form consists of 4 widgets,
     * and we need to have referances to them so we could work with them.
     * the letter 'm' in front of the variable indicates that this is a member of this classes activity
     * */
    private TextView mLogginUserName;
    private TextView mLogginPassword;
    private TextView mbtnLoggin;
    private TextView mbtnRegister;

    /**
     * The api_caller is used to send http requests to the VeryWowChat server
     * and also it is a singleton so we are dependant on reciving a instance of it from someone else
     * */
    private API_caller api_caller = API_caller.getInstance();

    /**
     * The ErrorLogger is used to map Json String objects into POJOS and it also should be
     * a singleton so we are dependant on receiving a instance of it from someone else
     * */
    private ErrorLogger errorLogger = ErrorLogger.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ask teacher if this is a good solution ?
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // -----------------------------------------------------------------------------------------

        /* Shared preferences allows us to store and retrieve data in a form of a (key,value) pairs.
           UserInfo stores 3 keys  1 = displayname , 2 = username, 3 = token

           MODE_PRIVATE: File creation mode: the default mode, where the created file can only be accessed
                         by the calling application (or all applications sharing the same user ID).

           MODE_WORLD_READABLE: File creation mode: allow all other applications to have read access to the created file.

           MODE_WORLD_WRITEABLE : File creation mode: allow all other applications to have write access to the created file. */
        final SharedPreferences UserInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);

        // Check if the token exists in the storage if it does then we redirect the user to the HomeActivity
        if(UserInfo.contains("token")) {
            Intent inten = new Intent(getApplicationContext(),HomeActivity.class);
            inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(inten);
        }

        // No token exists so Login form will be displayed
        setContentView(R.layout.activity_main);

        // get a referance of the widgets
        mLogginUserName = findViewById(R.id.edit_login_username);
        mLogginPassword = findViewById(R.id.edit_login_password);

        /* Since phones work way differently, its good practice like with buttons to assign
        *  a reference to them and assign a listener right away.
        *  We add a listener to the button whenever its clicked it will  display a Registation form
        *  */
        mbtnRegister = findViewById(R.id.btn_register);
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        /* Get a referance of the button and assign a listner to it so when ever the button is clicked
        *  it will obtain the information from the form and pass it down to the api_caller. */
        mbtnLoggin = findViewById(R.id.btn_login);
        mbtnLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if the user has not filled the form then we dont make the HTTP Request since it will result in an error
                if(mLogginPassword.getText().toString().isEmpty() || mLogginUserName.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
                    return;
                }

                // Create a Map from the data provided by the user
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", mLogginPassword.getText().toString());
                params.put("userName", mLogginUserName.getText().toString());

                /* Send the HTTP request for login through the api_caller and then map the object
                *  correctly based of the status code */
                try {
                    // Make the HTTP Request
                    Map<String, String> result = api_caller.HttpRequest("login","POST","",params);
                    // Parse the HTTP status code
                    int status = Integer.parseInt(result.get("status"));
                    // Parse the Json String into a JSON array
                    JSONArray resp_body = new JSONArray(result.get("response"));

                    // HTTP Request was a success
                    if(status >= 200 && status < 300 ){
                        /* Since its a successful request we know we should receive user information
                        *  so we store this information in the shared preferences so that next time
                        *  the use opens the app he will already be logged in */
                        SharedPreferences.Editor editor = UserInfo.edit();
                        editor.putString("username",resp_body.getJSONObject(0).get("username").toString());
                        editor.putString("displayname",resp_body.getJSONObject(0).get("displayname").toString());
                        editor.putString("token",resp_body.getJSONObject(0).get("token").toString());
                        editor.commit(); // ALWAYS REMEMBER TO COMMIT ELSE IT WONT SAVE !
                        // Start the Homepage Activity for the user
                        Intent inten = new Intent(getApplicationContext(),HomeActivity.class);
                        inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(inten);
                        return;
                    }

                    // HTTP Request was a failure
                    if(status >= 400 && status < 500){
                        /* Since its an error we know we receive a array of errors which we have to
                        *  map into POJOS and then display them.
                        *  (NOTE : You do not have to map them to POJOS in my opinion it just adds more work
                        *          and drains phone resources u can solve everything with JSONArray and JSONObject classes) */
                        // Create a List of errors
                        List<Error> errors = errorLogger.CreateListOfErrors(resp_body.getJSONObject(0).getJSONArray("errors"));
                        // Display a pop-up error message to the user with the errors received from the API
                        Toast.makeText(getApplicationContext(),errorLogger.ErrorsToString(errors),Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Log.e("LoginError","IOException in Login \n message :"+e);
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("LoginError","JSONException in Login \n message :"+e);
                    e.printStackTrace();
                }
            }
        });

    }
}
