package hi.hugbo.verywowchat.controllers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hi.hugbo.verywowchat.models.implementations.UserService;

/**
 * This activity is responsible for allowing the user to send a friend request to another user
 */
public class AddFriendActivity extends AppCompatActivity {

    private UserService userService = UserService.getInstance();

    private TextView edit_friend_username;
    private Button btn_add_friend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // fetch the widgets
        edit_friend_username = findViewById(R.id.edit_friend_username);
        btn_add_friend = findViewById(R.id.btn_add_friend);

        // fetch the user token
        SharedPreferences userInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edit_friend_username.getText().toString();

                try{
                    userService.addFriend(token, username);

                    Toast.makeText(getApplicationContext(),"Friend successfully added",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
