package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.MyChatroomItemAdapter;
import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.Chatroom;

/**
 * This component is responsible for displaying chatrooms the user is a member/admin/owner of
 * refer to MyChatroomItemAdapter to see how individual chatrooms are handled
 */
public class MyChatroomListFragment extends Fragment {

    private List<Chatroom> mChatrooms;

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private MyChatroomItemAdapter mChatroomAdapter; // adapter that will display the messages

    public MyChatroomListFragment(){
        mChatrooms = new ArrayList<>();
    }

    public static MyChatroomListFragment newInstance(){
        MyChatroomListFragment fragment = new MyChatroomListFragment();

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        // TODO: refresh list
        Log.d("onresume", "onResume called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("testusmaximus", "in wrong fragment ");
        View rootView = inflater.inflate(R.layout.chatroom_list, container, false);
        // fetch context to access toasts and user preferenes
        Context context = rootView.getContext();
        // user token stored in shared preferences
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        // JWT token for API authentication
        String token = userInfo.getString("token","n/a");

        try {
            mChatrooms = chatroomService.getMyChatrooms(token);

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
        mChatroomAdapter = new MyChatroomItemAdapter(mChatrooms);
        recyclerView.setAdapter(mChatroomAdapter);

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT END ----------------------------------
         * -----------------------------------------------------------------------------------------*/

        return rootView;
    }

}
