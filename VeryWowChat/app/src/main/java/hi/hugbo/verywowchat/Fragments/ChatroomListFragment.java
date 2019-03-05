package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.ChatroomItemAdapter;
import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.ChatRoomMessageActivity;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.Chatroom;

public class ChatroomListFragment extends Fragment {

    public ChatroomListFragment(){

    }

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private ChatroomItemAdapter mChatroomAdapter; // adapter that will display the messages

    public static ChatroomListFragment newInstance(){
        ChatroomListFragment fragment = new ChatroomListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatroom_list, container, false);

        Context context = rootView.getContext();

        List<Chatroom> chatrooms = new ArrayList<>();

        try {
            chatrooms = chatroomService.getMyChatrooms();

            for(Chatroom chatroom : chatrooms) {
                Log.d("getMyChatrooms", chatroom.toString());
            }

            Toast.makeText(context.getApplicationContext(),"Chatrooms successfully fetched",Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT START ---------------------------------
         * -----------------------------------------------------------------------------------------*/

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_chatrooms);

        // define and assign layout manager for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true); // ???
        recyclerView.setLayoutManager(layoutManager);

        // define and assign adapter for recycle view
        mChatroomAdapter = new ChatroomItemAdapter(chatrooms);
        recyclerView.setAdapter(mChatroomAdapter);

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT END ----------------------------------
         * -----------------------------------------------------------------------------------------*/

        return rootView;
    }

}
