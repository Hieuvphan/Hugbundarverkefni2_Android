package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
 * This Activity is responsible for displaying and maintaining the registration form
 * Taking inputs from the user and forwarding HTTP requests to the api_called
 * */
public class RegisterActivity extends AppCompatActivity {

    /**
     * The Registration form consists of 6 widgets,
     * and we need to have referances to them so we could work with them.
     * the letter 'm' in front of the variable indicates that this is a member of this classes activity
     * */
    private TextView mRegisterUsername;
    private TextView mRegisterDisplayName;
    private TextView mRegisterPassword;
    private TextView mRegisterPasswordRepeat;
    private TextView mRegisterEmail;
    private Button mBtnRegister;

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
        setContentView(R.layout.activity_register);

        // set a referance to all the widgets
        mRegisterUsername = findViewById(R.id.edit_register_username);
        mRegisterDisplayName = findViewById(R.id.edit_register_displayname);
        mRegisterPassword = findViewById(R.id.edit_register_password);
        mRegisterPasswordRepeat = findViewById(R.id.edit_register_password_repeat);
        mRegisterEmail = findViewById(R.id.edit_register_email);

        /* Good practice to reference a object and then assign a listener to it.
        *  when the user clicks on the register button we will first check if the
        *  form is not empty then the form data will be mapped and passed to the api_controller
        *  to make the HTTP request */
        mBtnRegister = findViewById(R.id.btn_register_submit);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if the user left field or fields empty we dont make the HTTP Request
                if(mRegisterDisplayName.getText().toString().isEmpty() ||
                   mRegisterUsername.getText().toString().isEmpty() ||
                   mRegisterPassword.getText().toString().isEmpty() ||
                   mRegisterPasswordRepeat.getText().toString().isEmpty() ||
                   mRegisterEmail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
                    return;
                }
                // if Both passwords dont Match then we dont make the HTTP Request
                if(!mRegisterPassword.getText().toString().equals(mRegisterPasswordRepeat.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Both passwords must match",Toast.LENGTH_LONG).show();
                    return;
                }

                // Create a Map from the data provided by the user
                Map<String, String> params = new HashMap<String, String>();
                params.put("displayName",mRegisterDisplayName.getText().toString());
                params.put("email",mRegisterEmail.getText().toString());
                params.put("password",mRegisterPassword.getText().toString());
                params.put("passwordReap",mRegisterPasswordRepeat.getText().toString());
                params.put("userName",mRegisterUsername.getText().toString());

                /* Send the HTTP request for Registration through the api_caller and then map the object
                 *  correctly based of the status code */
                try {
                    // Make the Http Request
                    Map<String, String> result = api_caller.HttpRequest("register","POST","",params);
                    // Parse HTTP Status code
                    int status = Integer.parseInt(result.get("status"));

                    /* If status code is 204 from the API thats means the request was successful and
                     * no content was returned */
                    if(status == 204) {
                        // display a message
                        Toast.makeText(getApplicationContext(),"New User has been Created ! \nplease visit your email address for validation",Toast.LENGTH_LONG).show();
                        return;
                    }
                    // if we made it here then it means we have an error and it also means we have a body
                    // parse the JSON string
                    JSONArray resp_body = new JSONArray(result.get("response"));
                    if(status >= 400 && status < 500){
                        // Create a List of errors
                        List<Error> errors = errorLogger.CreateListOfErrors(resp_body.getJSONObject(0).getJSONArray("errors"));
                        // Display a pop-up error message to the user with the errors received from the API
                        Toast.makeText(getApplicationContext(),errorLogger.ErrorsToString(errors),Toast.LENGTH_LONG).show();
                    }

                }
                catch (IOException e) {
                    Log.e("RegisterError","IOException in Register \n message :"+e);
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("RegisterError","JSONException in Register \n message :"+e);
                    e.printStackTrace();
                }
            }
        });


    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, RegisterActivity.class);
        return i;
    }
}
