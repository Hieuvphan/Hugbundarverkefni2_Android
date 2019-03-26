package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Adapters.ChatroomAdminInviteItemAdapter;
import hi.hugbo.verywowchat.Adapters.ChatroomInviteItemAdapter;
import hi.hugbo.verywowchat.Models.ChatRoomMessageService;
import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.Chatroom;

import static android.content.Context.MODE_PRIVATE;

/**
 * This component is responsible for displaying chatrooms the user is a member/admin/owner of
 * refer to MyChatroomItemAdapter to see how individual chatrooms are handled
 */
public class ChatroomAdminInvitesListFragment extends Fragment {

    private final String CHANNEL_ID = "USER_CHAT"; // channel name that the notifications are sent to
    private Handler mHandler; // Task handler that will be used to poll chat updates
    private ChatRoomMessageService mChatCaller = ChatRoomMessageService.getInstance(); // chat service to make http reuqests
    private SharedPreferences mUserInfo; //  stored user info

    private List<Chatroom> mChatrooms;
    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private ChatroomAdminInviteItemAdapter mChatroomAdapter; // adapter that will display the messages

    Context context;

    public ChatroomAdminInvitesListFragment(){
        mChatrooms = new ArrayList<>();
    }

    public static ChatroomAdminInvitesListFragment newInstance(){
        ChatroomAdminInvitesListFragment fragment = new ChatroomAdminInvitesListFragment();

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
        mChatroomAdapter = new ChatroomAdminInviteItemAdapter(mChatrooms);
        recyclerView.setAdapter(mChatroomAdapter);

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
            List<Chatroom> newChatrooms = chatroomService.getChatroomInvites(token);

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
