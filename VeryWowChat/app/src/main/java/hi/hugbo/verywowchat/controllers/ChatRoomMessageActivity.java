package hi.hugbo.verywowchat.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.ChatMessageAdapter;
import hi.hugbo.verywowchat.entities.ChatMessage;

public class ChatRoomMessageActivity extends AppCompatActivity {

    private List<ChatMessage> mChatMessages;
    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_message);

        mChatMessages = new ArrayList<>();
        mChatMessages.add(new ChatMessage("dickbutt",true));
        mChatMessages.add(new ChatMessage("dickbutt1",false));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.chatMessages);
        recyclerView.setLayoutManager(layoutManager);
        ChatMessageAdapter adapter = new ChatMessageAdapter(mChatMessages);
        recyclerView.setAdapter(adapter);



    }
}
