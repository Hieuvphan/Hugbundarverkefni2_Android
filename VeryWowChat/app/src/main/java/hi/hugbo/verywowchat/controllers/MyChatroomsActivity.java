package hi.hugbo.verywowchat.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.ChatMessageAdapter;
import hi.hugbo.verywowchat.Adapters.ChatroomItemAdapter;
import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.entities.Chatroom;

/**
 * @Author Vilhelm
 *
 * MyChatroomsActivity gives us a list of all the chatrooms the user is a member of.
 *
 * When a chatroom is selected, a new activity will open, allowing you to view or manage the
 * chatroom.
 *
 * We use shared preferences to get the user and JWT
 *
 * RecycleView used to contain the list of chatrooms.
 */
public class MyChatroomsActivity extends AppCompatActivity {

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private ChatroomItemAdapter mChatroomAdapter; // adapter that will display the messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_list);

        SharedPreferences userInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        List<Chatroom> chatrooms = new ArrayList<>();

        try {
            chatrooms = chatroomService.getMyChatrooms(token);

            for(Chatroom chatroom : chatrooms) {
                Log.d("getMyChatrooms", chatroom.toString());
            }

            Toast.makeText(getApplicationContext(),"Chatrooms successfully fetched",Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT START ---------------------------------
         * -----------------------------------------------------------------------------------------*/

        RecyclerView recyclerView = findViewById(R.id.rv_chatrooms);

        // define and assign layout manager for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true); // ???
        recyclerView.setLayoutManager(layoutManager);

        // define and assign adapter for recycle view
        mChatroomAdapter = new ChatroomItemAdapter(chatrooms);
        recyclerView.setAdapter(mChatroomAdapter);

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT END ----------------------------------
         * -----------------------------------------------------------------------------------------*/
    }
}
