package hi.hugbo.verywowchat.controllers;

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
import java.util.Map;

import hi.hugbo.verywowchat.services.API_caller;

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

    /* Ensure that the api_called is a singleton instance */
    private API_caller api_caller = API_caller.getInstance();
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
                if(mRegisterDisplayName.getText().toString().isEmpty() ||
                   mRegisterUsername.getText().toString().isEmpty() ||
                   mRegisterPassword.getText().toString().isEmpty() ||
                   mRegisterPasswordRepeat.getText().toString().isEmpty() ||
                   mRegisterEmail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
                    return;
                }

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

                try {
                    Map<String, String> result = api_caller.HttpRequest("register","POST","",params);
                    int status = Integer.parseInt(result.get("status"));

                    if(status == 204) {
                        Toast.makeText(getApplicationContext(),"New User has been Created ! \nplease visit your email address for validation",Toast.LENGTH_LONG).show();
                        return;
                    }

                    JSONArray resp_body = new JSONArray(result.get("response"));
                    if(status >= 400 && status < 500){
                        String errors_msg = "";
                        for (int i = 0; i< resp_body.getJSONObject(0).getJSONArray("errors").length(); i++) {
                            errors_msg += resp_body.getJSONObject(0).getJSONArray("errors").getJSONObject(i).getString("message");
                        }
                        Toast.makeText(getApplicationContext(),errors_msg,Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                catch (IOException e) {
                    Log.d("here","IO exeption");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d("here","jsom exeption");
                    e.printStackTrace();
                }
            }
        });


    }
}
