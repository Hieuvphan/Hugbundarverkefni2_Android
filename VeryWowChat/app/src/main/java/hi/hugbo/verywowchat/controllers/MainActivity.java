package hi.hugbo.verywowchat.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

/**
 * @Author : Róman
 * This Activity is responsible for displaying and maitaining the login form and redirecting
 * the user to his/her home page if he/she is logged in.
 * */
public class MainActivity extends AppCompatActivity {

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
            inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(inten);
        }

        // No token exists so Login form will be displayed
        setContentView(R.layout.activity_main);

        // get a referance of the widgets
        mLogginUserName = findViewById(R.id.edit_login_username);
        mLogginPassword = findViewById(R.id.edit_login_password);

        /* Since phones work way differently, its good practice like with buttons to assign
        *  a reference to them and right away asign whatever the button needs to have like in
         *  this case a listener. We add a listener to the button whenever its clicked it will
         *  display a Registation form */
        mbtnRegister = findViewById(R.id.btn_register);
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        /* Get a referance of the button and assign a listner to it so when ever the button is clicked
        *  it will obtain the information from the form and pass it down to the api_caller. */
        mbtnLoggin = findViewById(R.id.btn_login);
        mbtnLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Map<String, String> result = api_caller.HttpRequest("login","POST","",params);
                    int status = Integer.parseInt(result.get("status"));
                    JSONArray resp_body = new JSONArray(result.get("response"));

                    if(status >= 200 && status < 300 ){
                        /* Since its a successful request we know we should recive user information
                        *  so we store this information in the shared preferences */
                        SharedPreferences.Editor editor = UserInfo.edit();
                        editor.putString("username",resp_body.getJSONObject(0).get("username").toString());
                        editor.putString("displayname",resp_body.getJSONObject(0).get("displayname").toString());
                        editor.putString("token",resp_body.getJSONObject(0).get("token").toString());
                        editor.commit();
                        /* We want to clear all history and start a new Actitivity */
                        Intent inten = new Intent(getApplicationContext(),HomeActivity.class);
                        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(inten);
                        return;
                    }
                    if(status >= 400 && status < 500){

                        String errors_msg = "";
                        for (int i = 0; i< resp_body.getJSONObject(0).getJSONArray("errors").length(); i++) {
                            errors_msg += resp_body.getJSONObject(0).getJSONArray("errors").getJSONObject(i).getString("message");
                        }
                        Toast.makeText(getApplicationContext(),errors_msg,Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
