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

/**
 * This component is responsible for chatroom searches
 * Search for chatrooms the logged in user is not a member of
 * refer to SearchChatroomItemAdapter to see how individual chatrooms are handled
 */
public class SearchChatroomListFragment extends Fragment {

    private final List<Chatroom> mChatrooms;
    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private SearchChatroomItemAdapter mChatroomAdapter; // adapter that will display the messages

    public SearchChatroomListFragment(){
        Log.d("dh", "SearchChatroomListFragment()");
        mChatrooms = new ArrayList<>();
    }

    // widgets
    TextView editSearch;
    ImageButton btn_chatroom_search;

    Context context;

    public static SearchChatroomListFragment newInstance(){
        Log.d("dh", "SearchChatroomListFragment.newInstance()");
        SearchChatroomListFragment fragment = new SearchChatroomListFragment();

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("dh", "SearchChatroomListFragment.onResume()");
        if(editSearch.getText().toString().length() < 1){
            fetchChatrooms();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("dh", "SearchChatroomListFragment.onCreateView()");
        View rootView = inflater.inflate(R.layout.chatroom_search, container, false);
        // fetch context to access toasts and user preferenes
        context = rootView.getContext();
        // fetch the search bar widget
        editSearch = rootView.findViewById(R.id.edit_chatroom_search);
        btn_chatroom_search = rootView.findViewById(R.id.btn_chatroom_search);

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

        // make button do search
        btn_chatroom_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchChatrooms();
            }
        });

        return rootView;
    }

    private void fetchChatrooms(){
        Log.d("dh", "SearchChatroomListFragment.fetchChatrooms()");
        // fetch the search string
        String searchTerm = editSearch.getText().toString();
        // user token stored in shared preferences
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        // JWT token for API authentication
        String token = userInfo.getString("token","n/a");

        try {
            List<Chatroom> newChatrooms = chatroomService.chatroomSearch(token, searchTerm);

            // empty the list
            mChatrooms.removeAll(mChatrooms);
            // fill with the new collection of chatrooms
            mChatrooms.addAll(newChatrooms);
            // make adapter refresh list
            mChatroomAdapter.notifyDataSetChanged();
            Toast.makeText(context.getApplicationContext(),"Chatrooms successfully fetched",Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
