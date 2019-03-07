package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.SearchChatroomItemAdapter;
import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.Chatroom;

public class SearchChatroomListFragment extends Fragment {

    private final List<Chatroom> mChatrooms;

    public SearchChatroomListFragment(){
        mChatrooms = new ArrayList<>();
    }

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private SearchChatroomItemAdapter mChatroomAdapter; // adapter that will display the messages

    public static SearchChatroomListFragment newInstance(){
        SearchChatroomListFragment fragment = new SearchChatroomListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatroom_search, container, false);
        final Context context = rootView.getContext();

        final TextView editSearch = rootView.findViewById(R.id.edit_chatroom_search);

        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        ImageButton btn_chatroom_search = rootView.findViewById(R.id.btn_chatroom_search);


        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT START ---------------------------------
         * -----------------------------------------------------------------------------------------*/

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_chatrooms);

        // define and assign layout manager for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // define and assign adapter for recycle view
        mChatroomAdapter = new SearchChatroomItemAdapter(mChatrooms);
        recyclerView.setAdapter(mChatroomAdapter);

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT END ----------------------------------
         * -----------------------------------------------------------------------------------------*/


        btn_chatroom_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String searchTerm = editSearch.getText().toString();

            try {
                List<Chatroom> newChatrooms = chatroomService.chatroomSearch(token, searchTerm);

                // empty the list
                mChatrooms.removeAll(mChatrooms);
                // fill with the new collection of chatrooms
                mChatrooms.addAll(newChatrooms);

                mChatroomAdapter.notifyDataSetChanged();
                Toast.makeText(context.getApplicationContext(),"Chatrooms successfully fetched",Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            }
        });

        return rootView;
    }

}
