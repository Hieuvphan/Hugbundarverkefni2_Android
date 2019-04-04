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

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.FriendsAdapter;
import hi.hugbo.verywowchat.Models.Implementations.UserService;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * @Author : Róman
 * This fragment is responsible for displaying the logged in users friends.
 * We use a recycle view to display all the friends a user has.
 */
public class FriendListFragment extends Fragment {

    private UserService mUserService = UserService.getInstance();// want only a singleton of this instance
    private FriendsAdapter mFriendsAdapter;
    private List<User> mFriends;
    private Context mContext;
    private SharedPreferences mUserInfo; // user info

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mContext = rootView.getContext();

        /* Shared preferences allows us to store and retrieve data in a form of a (key,value) pairs.
           UserInfo stores 3 keys  1 = displayname , 2 = username, 3 = token */
        mUserInfo = mContext.getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);

        /*-----------------------------------------------------------------------------------------
         * ---------------------------Recycle view START--------------------------------------------
         * ----------------------------------------------------------------------------------------*/
        mFriends = new ArrayList<>();

         /*
          RecyclerView provides 3 built-in layout managers:
            - LinearLayoutManager shows items in a vertical or horizontal scrolling list. (Like chat msg's)
            - GridLayoutManager shows items in a grid.
            - StaggeredGridLayoutManager shows items in a staggered grid.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(false);

        RecyclerView recyclerView = rootView.findViewById(R.id.friendsList);
        recyclerView.setLayoutManager(layoutManager);
        mFriendsAdapter = new FriendsAdapter(mFriends);
        recyclerView.setAdapter(mFriendsAdapter);

        /*-----------------------------------------------------------------------------------------
         * ---------------------------Recycle view END---------------------------------------------
         * ----------------------------------------------------------------------------------------*/
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        GetFriends();
    }

    /**
     * <pre>
     *     Usage : GetFriends()
     *      For  : nothing
     *     After : fetches users friends, adds them to the friends list
     *             and notifies the adapter of the change
     * </pre>
     */
    public void GetFriends() {
       List<User> newFrieds = mUserService.getFriends(mUserInfo.getString("token","n/a"));
       if(newFrieds == null) {
           return;
       }
       int currSize = mFriends.size();
       mFriends.addAll(newFrieds);
       mFriendsAdapter.notifyItemRangeInserted(currSize,mFriends.size());
    }

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        return fragment;
    }
}
