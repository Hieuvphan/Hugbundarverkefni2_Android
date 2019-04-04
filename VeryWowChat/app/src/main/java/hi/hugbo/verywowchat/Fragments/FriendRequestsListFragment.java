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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.FriendRequestItemAdapter;
import hi.hugbo.verywowchat.Models.Implementations.UserService;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * This component is responsible for displaying chatrooms the user is a member/admin/owner of
 * refer to MyChatroomItemAdapter to see how individual chatrooms are handled
 */
public class FriendRequestsListFragment extends Fragment {

    private SharedPreferences mUserInfo; //  stored user info

    private List<User> mUsers;
    private UserService userService = UserService.getInstance();
    private FriendRequestItemAdapter mFriendRequestItemAdapter;

    Context context;

    public FriendRequestsListFragment(){
        mUsers = new ArrayList<>();
    }

    public static FriendRequestsListFragment newInstance(){
        FriendRequestsListFragment fragment = new FriendRequestsListFragment();

        return fragment;
    }

    /**
     * Here I'm using setUserVisibleHint instead of onResume as the member invite tab
     * affects this tab, and since they are neighbours, this one won't be refreshed when
     * appropriate
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // make sure this isn't called before onCreateView finishes
        if(isVisibleToUser && context != null){
            fetchChatrooms();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatroom_list, container, false);
        // fetch context to access toasts and user preferenes
        context = rootView.getContext();

        /* Shared preferences allows us to store and retrieve data in a form of a (key,value) pairs.
           UserInfo stores 3 keys  1 = displayname , 2 = username, 3 = token */
        mUserInfo = context.getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT START ---------------------------------
         * -----------------------------------------------------------------------------------------*/

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_chatrooms);

        // define and assign layout manager for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // define and assign adapter for recycle view
        mFriendRequestItemAdapter = new FriendRequestItemAdapter(mUsers);
        recyclerView.setAdapter(mFriendRequestItemAdapter);

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT END ----------------------------------
         * -----------------------------------------------------------------------------------------*/
        return rootView;
    }

    private void fetchChatrooms(){
        // user token stored in shared preferences
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        // JWT token for API authentication
        String token = userInfo.getString("token","n/a");

        try {
            List<User> newUsers = userService.getFriendRequests(token);

            // empty the list
            mUsers.removeAll(mUsers);
            // fill with the new collection of chatrooms
            mUsers.addAll(newUsers);
            // make adapter refresh list
            mFriendRequestItemAdapter.notifyDataSetChanged();
            Toast.makeText(context.getApplicationContext(),"Friend requestors successfully fetched",Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
