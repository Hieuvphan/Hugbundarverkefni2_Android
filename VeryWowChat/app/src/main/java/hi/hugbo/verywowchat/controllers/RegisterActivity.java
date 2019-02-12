package hi.hugbo.verywowchat.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

        /* Good practice */
        mBtnRegister = findViewById(R.id.btn_register_submit);


    }
}
