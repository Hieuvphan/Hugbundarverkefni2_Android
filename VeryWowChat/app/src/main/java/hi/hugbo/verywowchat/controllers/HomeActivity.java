package hi.hugbo.verywowchat.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button mBtnLogout;
    private Button BtnCreateChatroom;
    private Button OpenChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BtnCreateChatroom = findViewById(R.id.btn_create_chatroom);
        BtnCreateChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateChatroomActivity.class);
                startActivity(intent);
            }
        });
        final SharedPreferences UserInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);

        mBtnLogout = findViewById(R.id.btn_logout);
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("here",UserInfo.getString("token","no token"));
                SharedPreferences.Editor spreferencesEditor = UserInfo.edit();
                spreferencesEditor.clear();
                spreferencesEditor.commit();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        OpenChat = findViewById(R.id.openChatRoom);
        OpenChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChatRoomMessageActivity.class);
                intent.putExtra("chatID","rruchat");
                startActivity(intent);
            }
        });
    }
}
