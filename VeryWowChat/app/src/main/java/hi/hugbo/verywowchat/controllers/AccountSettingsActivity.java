package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import hi.hugbo.verywowchat.Models.UserService;

/**
 * @Author Róman
 * This Activity is responsible for displaying and maitaining forms that allow
 * the user to change his Account profile settings such as DisplayName , Password and Email.
 * */
public class AccountSettingsActivity extends AppCompatActivity {

    /**
     * The Account settings layout consists of 10 widgets of which 4 are labels
     * another 4 are inputs for 1 for text, 2 for password and 1 for email
     * last 2 widgets are buttons
     * */
    private TextView mDisplayName;
    private TextView mEmail;
    private TextView mPassword;
    private TextView mPasswordRepeat;
    private Button mBtnSaveChanges;
    private Button mBtnDeleteAccount;

    // Service that handles our api calls
    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dh", "AccountSettingsActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        // Fetch the User info that is stored on the phone
        final SharedPreferences UserInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);

        mUserService = UserService.getInstance();

        // Obtain references to the widgets
        mDisplayName = findViewById(R.id.edit_settings_displayname);
        mEmail = findViewById(R.id.edit_settings_email);
        mPassword = findViewById(R.id.edit_settings_password);
        mPasswordRepeat = findViewById(R.id.edit_settings_password_repeat);

        //Obtain a reference to the save changes button and assign it a listener
        mBtnSaveChanges = findViewById(R.id.acc_settings_btn_save);
        mBtnSaveChanges.setOnClickListener(new View.OnClickListener() {
            /*
             * when the SaveChanges button is clicked we need
             * construct a request body that will be passed to the user service
             * to make the request of updating the user.
             * */
            @Override
            public void onClick(View v) {
               // if no fields have been filled out we respond with an error message
               if(mDisplayName.getText().toString().isEmpty() && mEmail.getText().toString().isEmpty() &&
                  mPassword.getText().toString().isEmpty() && mPasswordRepeat.getText().toString().isEmpty()){
                   Toast.makeText(getApplicationContext(),"Please fill inn fields you wish to update",Toast.LENGTH_LONG).show();
                   return;
               }

                // Create a Map from the data provided by the user
                Map<String, String> params = new HashMap<String, String>();

               // if the user wants to update his password we need to check if both passwords match
               if(!mPassword.getText().toString().isEmpty() || !mPasswordRepeat.getText().toString().isEmpty()){
                  if(!mPassword.getText().toString().equals(mPasswordRepeat.getText().toString())){
                      Toast.makeText(getApplicationContext(),"Both passwords have to match",Toast.LENGTH_LONG).show();
                      return;
                  }

                  params.put("password",mPassword.getText().toString());
               }

                // if the user filled out his display name
                if(!mDisplayName.getText().toString().isEmpty()) {
                   params.put("displayName",mDisplayName.getText().toString());
                }

                // if the user filled out his email
                if(!mEmail.getText().toString().isEmpty()){
                    params.put("email",mEmail.getText().toString());
                }

                try {
                    mUserService.UpdateUser(params,UserInfo.getString("token","N/A"));
                    SharedPreferences.Editor userEdit = UserInfo.edit();
                    userEdit.remove("displayname");
                    userEdit.putString("displayname",mDisplayName.getText().toString());
                    userEdit.commit();
                    Toast.makeText(getApplicationContext(),"Update successful ",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        //Obtain a reference to the Delete button and assign it a listener
        mBtnDeleteAccount = findViewById(R.id.acc_settings_btn_delete);
        mBtnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            /**
             * Since Deleting the account is a serious buisness we want to make sure that
             * the user is absolutely sure that he wants to do it. so we will display
             * another pop-up asking the user is he is sure that he wants to proccede
             * */
            @Override
            public void onClick(View v) {
               /*
                * This will display the snackbar that will prompt the user if he is sure
                * that he wants to delete his account
                * */
               ShowSnackBar(UserInfo);
            }
        });
    }

    /**
     * <pre>
     *     Usage : ShowSnackBar(spreferences);
     *       For : spreferences is  SharedPreferences
     *     After : Displays a Snackbar to the user for a short period of time,
     *             if he clicks on the "YES Delete" then a request is sent to
     *             the API for deleting his account else snackbar will disappear
     * </pre>
     * @param spreferences SharedPreferences of UserInfo
     */
    public void ShowSnackBar(final SharedPreferences spreferences){
        /*
         * A Snackbar is basically like a Toast its a pop up that is displayed for a short period of time
         * it is even ment to replace the Toast in the future, the reason why we use Snackbar here over toast is
         * the Snackbar has added features such as displaying buttons or even custom layouts in the pop-up
         * which we want in this case.
         *
         * We want to display this Snack for a short time, since the user should be 100% sure that he wants to delete his account.
         * */
        Snackbar snackbar = Snackbar.make(mBtnDeleteAccount,"Are you sure you want to delete your account ? This cannot be undone.",Snackbar.LENGTH_LONG);

        // Add a button in the Snack and give it the functionality to call the API to delete the user.
        snackbar.setAction("YES Delete", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // call the delete and if it goes through we clear the user toke and redirect the user to login page
                    mUserService.DeleteMe(spreferences.getString("token","n/a"));
                    Toast.makeText(getApplicationContext(),"We are sad to see you go :( ",Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor spreferencesE = spreferences.edit();
                    spreferencesE.clear();
                    spreferencesE.commit();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (Exception e) {
                    // TOAST INSIDE Snackbar UR A MADMAN!
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        snackbar.show();
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, AccountSettingsActivity.class);
        return i;
    }
}
