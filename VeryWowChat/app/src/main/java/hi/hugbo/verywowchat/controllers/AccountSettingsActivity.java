package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @Author Róman
 * This Activity is responsible for displaying and maitaining forms that allow
 * the user to change his Account profile settings such as DisplayName , Password and Email.
 * */
public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
    }


    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, AccountSettingsActivity.class);
        return i;
    }
}
