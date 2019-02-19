package hi.hugbo.verywowchat.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import java.util.Arrays;
import java.util.List;

import hi.hugbo.verywowchat.entities.Chatroom;

public class CreateChatroomActivity extends AppCompatActivity {

    private ChatroomService chatroomService = new ChatroomServiceImplementation();

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

        edit_chatroom_name = findViewById(R.id.edit_chatroom_name);
        edit_chatroom_displayname = findViewById(R.id.edit_chatroom_displayname);
        edit_chatroom_description = findViewById(R.id.edit_chatroom_description);
        switch_listed = findViewById(R.id.switch_listed);
        switch_invited_only = findViewById(R.id.switch_invited_only);
        btn_create_chatroom = findViewById(R.id.btn_create_chatroom);
        edit_chatroom_tags = findViewById(R.id.edit_chatroom_tags);

        btn_create_chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatroomName = edit_chatroom_name.getText()+"";
                String displayName = edit_chatroom_displayname.getText()+"";
                String description= edit_chatroom_description.getText()+"";
                Boolean listed = switch_listed.isChecked();
                Boolean invitedOnly = switch_invited_only.isChecked();
                String tagInput = edit_chatroom_tags.getText()+"";
                List<String> tags = Arrays.asList(tagInput.split(","));

                Log.d("createChatroom", "tags len: "+tags.size());

                try{
                    Chatroom c = chatroomService.createChatroom(chatroomName, displayName, description, listed, invitedOnly, tags);

                    Log.d("createChatroom", c.toString());
                } catch(Exception e) {
                    // TODO: use toast for error messages
                    Log.e("createChatroom", "exception caught when calling service:"+e.getMessage());
                }
            }
        });
    }
}
