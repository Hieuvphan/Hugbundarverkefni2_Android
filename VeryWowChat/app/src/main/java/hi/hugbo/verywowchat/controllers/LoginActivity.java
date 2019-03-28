package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Map;


import hi.hugbo.verywowchat.Models.Implementations.AccountService;

/**
 * @Author : Róman
 * This Activity is responsible for displaying and maitaining the login form and redirecting
 * the user to his/her home page if he/she is logged in.
 * */
public class LoginActivity extends AppCompatActivity {

    /**
     * The login form consists of 4 widgets,
     * and we need to have references to them so we could work with them.
     * the letter 'm' in front of the variable indicates that this is a member of this classes activity
     * */
    private TextView mLogginUserName;
    private TextView mLogginPassword;
    private TextView mbtnLoggin;
    private TextView mbtnRegister;

    /**
     * The service that will handle the login in functionality
     * */
    private AccountService mAccountService = AccountService.getInstance();

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

        // Check if the token exists in the storage if it does then we redirect the user to the HomePageActivity
        if(UserInfo.contains("token")) {
            Intent inten = new Intent(getApplicationContext(),HomePageActivity.class);
            inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(inten);
        }

        // No token exists so Login form will be displayed
        setContentView(R.layout.activity_main);

        // get a referance of the widgets
        mLogginUserName = findViewById(R.id.edit_login_username);
        mLogginPassword = findViewById(R.id.edit_login_password);

        /*
         *Since phones work way differently, its good practice like with buttons to assign
         *a reference to them and assign a listener right away.
         *We add a listener to the button whenever its clicked it will  display a Registation form
         **/
        mbtnRegister = findViewById(R.id.btn_register);
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.newIntent(LoginActivity.this));
            }
        });

        /*
         *Get a referance of the button and assign a listner to it so when ever the button is clicked
         *it will obtain the information from the form and pass it down to the api_caller.
         **/
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

                // Call the service to perform the Login
                String login = mAccountService.Login(params,getApplicationContext(),UserInfo);

                // if the service returns null then it means it was successfull
                if(login == null) {
                    startActivity(HomePageActivity.newIntent(LoginActivity.this));
                    return;
                }
                Toast.makeText(getApplicationContext(),login,Toast.LENGTH_LONG).show();
            }
        });

    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}
