package hi.hugbo.verywowchat.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;

public class ChatroomInviteActivity extends AppCompatActivity {

    private ChatroomService chatroomService = new ChatroomServiceImplementation();

    private String mChatroomName;

    private TextView edit_chatroom_invitee;
    private Button btn_chatroom_invite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dh", "ChatroomInviteActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_invite_member);

        // get the chatroom name from extra
        Intent intent = getIntent();
        mChatroomName = intent.getStringExtra("chatroomName");

        edit_chatroom_invitee = findViewById(R.id.edit_chatroom_invitee);
        btn_chatroom_invite = findViewById(R.id.btn_chatroom_invite);

        SharedPreferences userInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        btn_chatroom_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inviteeName = edit_chatroom_invitee.getText().toString();

                try{
                    chatroomService.inviteMemberToChatroom(token, mChatroomName, inviteeName);

                    Toast.makeText(getApplicationContext(),"Invite successfully sent",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
