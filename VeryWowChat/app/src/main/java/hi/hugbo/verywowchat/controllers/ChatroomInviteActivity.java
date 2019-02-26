package hi.hugbo.verywowchat.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.entities.Chatroom;

public class ChatroomInviteActivity extends AppCompatActivity {

    private ChatroomService chatroomService = new ChatroomServiceImplementation();

    private chatroomName;

    private TextView edit_chatroom_invitee;
    private Button btn_chatroom_invite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);

        edit_chatroom_invitee = findViewById(R.id.edit_chatroom_invitee);
        btn_chatroom_invite = findViewById(R.id.btn_chatroom_invite);

        btn_chatroom_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inviteeName = edit_chatroom_invitee.getText()+"";

                try{
                    Chatroom c = chatroomService.inviteMemberToChatroom(chatroomName, inviteeName);

                    Toast.makeText(getApplicationContext(),"Chat created successfully",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
