package hi.hugbo.verywowchat.controllers;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import hi.hugbo.verywowchat.Models.Interfaces.IChatroomService;
import hi.hugbo.verywowchat.Models.Implementations.ChatroomService;
import java.util.Arrays;
import java.util.List;

import hi.hugbo.verywowchat.entities.Chatroom;

/**
 * this activity is responsible for creating new chatrooms
 */
public class CreateChatroomActivity extends AppCompatActivity {

    private IChatroomService chatroomService = new ChatroomService();

    private TextView edit_chatroom_name;
    private TextView edit_chatroom_displayname;
    private TextView edit_chatroom_description;
    private Switch switch_listed;
    private Switch switch_invited_only;
    private TextView edit_chatroom_tags;
    private Button btn_create_chatroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);

        // fetch the widgets
        edit_chatroom_name = findViewById(R.id.edit_chatroom_name);
        edit_chatroom_displayname = findViewById(R.id.edit_chatroom_displayname);
        edit_chatroom_description = findViewById(R.id.edit_chatroom_description);
        switch_listed = findViewById(R.id.switch_listed);
        switch_invited_only = findViewById(R.id.switch_invited_only);
        btn_create_chatroom = findViewById(R.id.btn_create_chatroom);
        edit_chatroom_tags = findViewById(R.id.edit_chatroom_tags);

        // fetch the user token
        SharedPreferences userInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        // when the button is pressed, attempt to create a chatroom
        btn_create_chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fetch values from widgets
                String chatroomName = edit_chatroom_name.getText()+"";
                String displayName = edit_chatroom_displayname.getText().toString();
                String description= edit_chatroom_description.getText().toString();
                Boolean listed = switch_listed.isChecked();
                Boolean invitedOnly = switch_invited_only.isChecked();
                String tagInput = edit_chatroom_tags.getText().toString();
                List<String> tags = Arrays.asList(tagInput.split(","));

                try{
                    Chatroom c = chatroomService.createChatroom(token, chatroomName, displayName, description, listed, invitedOnly, tags);

                    Toast.makeText(getApplicationContext(),"Chat created successfully",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
