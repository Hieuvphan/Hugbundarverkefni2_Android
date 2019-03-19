package hi.hugbo.verywowchat.Fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
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
import hi.hugbo.verywowchat.Models.ChatRoomMessageService;
import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.ChatRoomMessageActivity;
import hi.hugbo.verywowchat.controllers.LoginActivity;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.ChatMessage;
import hi.hugbo.verywowchat.entities.Chatroom;

import static android.content.Context.MODE_PRIVATE;

/**
 * This component is responsible for displaying chatrooms the user is a member/admin/owner of
 * refer to MyChatroomItemAdapter to see how individual chatrooms are handled
 */
public class MyChatroomListFragment extends Fragment {

    private List<Chatroom> mChatrooms;
    private Handler mHandler;
    private ChatRoomMessageService mChatCaller = ChatRoomMessageService.getInstance();
    private SharedPreferences mUserInfo; // user info

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private MyChatroomItemAdapter mChatroomAdapter; // adapter that will display the messages

    Context context;

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
        fetchChatrooms();
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
        mChatroomAdapter = new MyChatroomItemAdapter(mChatrooms);
        recyclerView.setAdapter(mChatroomAdapter);

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT END ----------------------------------
         * -----------------------------------------------------------------------------------------*/

        // fetch the chatrooms
        // fetchChatrooms();

        /* Start the Polling for notifications */
        mHandler = new Handler();
        mHandler.post(PollNotifications);// Start the initial runnable task by posting through the handler
        return rootView;
    }

    private void fetchChatrooms(){
        // user token stored in shared preferences
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        // JWT token for API authentication
        String token = userInfo.getString("token","n/a");

        try {
            List<Chatroom> newChatrooms = chatroomService.getMyChatrooms(token);

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
    /**--------------------------------------------------------------------------------------------
     * -------------------------------Roman's Stuff here ------------------------------------------
     * --------------------------------------------------------------------------------------------*/

    /**
     * @Author Róman
     * This is the polling for notifications if a chatroom that a user
     * is a part of has a new message. If a new message is found
     * we display a notification tthat a user can click on to open the chat.
     * */
    private Runnable PollNotifications = new Runnable() {
        @Override
        public void run() {
            // For each chat we check if the spesific chat has a new notification
            for (int i = 0; i < mChatrooms.size() ; i++) {
                Chatroom updatedChat = mChatCaller.UpdateChat(mChatrooms.get(i).getChatroomName(),mUserInfo.getString("token","n/a"));
                // if update chat fails we will log out user
                if(updatedChat == null){
                    LogOutUser();
                }
                // else we check if the chatroom has recivied new message
                else {
                    // check if the chatroom recived new message
                    if(true){
                      // display the pop upp
                      MakePopUpMessage(mChatrooms.get(i).getChatroomName());
                    }
                }

            }
            mHandler.postDelayed(this, 7000); // i want to poll every 5 seconds
        }
    };

    public void MakePopUpMessage(String chatID) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }
    /**
     * All the necessary things that have to be done inorder to logout the user from the app
     * */
    public void LogOutUser(){
        SharedPreferences.Editor spreferencesEditor = mUserInfo.edit();
        spreferencesEditor.clear();
        spreferencesEditor.commit();
        // redirect the user to the login activity
        startActivity(LoginActivity.newIntent(getContext()));
    }
}
