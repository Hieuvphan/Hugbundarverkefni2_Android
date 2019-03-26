package hi.hugbo.verywowchat.Fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import static android.support.v4.content.ContextCompat.getSystemService;

/**
 * This component is responsible for displaying chatrooms the user is a member/admin/owner of
 * refer to MyChatroomItemAdapter to see how individual chatrooms are handled
 */
public class MyChatroomListFragment extends Fragment {

    private final String CHANNEL_ID = "USER_CHAT"; // channel name that the notifications are sent to
    private Handler mHandler; // Task handler that will be used to poll chat updates
    private ChatRoomMessageService mChatCaller = ChatRoomMessageService.getInstance(); // chat service to make http reuqests
    private SharedPreferences mUserInfo; //  stored user info

    private List<Chatroom> mChatrooms;
    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private MyChatroomItemAdapter mChatroomAdapter; // adapter that will display the messages

    Context context;

    public MyChatroomListFragment(){
        mChatrooms = new ArrayList<>();
    }

    public static MyChatroomListFragment newInstance(){
        Log.d("dh", "MyChatroomListFragment.newInstance()");
        MyChatroomListFragment fragment = new MyChatroomListFragment();

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("dh", "MyChatroomListFragment.onResume()");
        fetchChatrooms();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("dh", "MyChatroomListFragment.onCreateView()");
        View rootView = inflater.inflate(R.layout.chatroom_list, container, false);
        createNotificationChannel();
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
        Log.d("dh", "MyChatroomListFragment.fetchChatrooms()");
        // user token stored in shared preferences
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        // JWT token for API authentication
        String token = userInfo.getString("token","n/a");

        try {
            List<Chatroom> newChatrooms = chatroomService.getMyChatrooms(token);

            Log.d("dh", "MyChatroomListFragment.fetchChatrooms(): nr. of chat rooms: " + newChatrooms.size());

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(PollNotifications);
    }

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
                    if(mChatrooms.get(i).getLastRead() < updatedChat.getLastMessageReceived()){
                      // update the chat
                      mChatrooms.get(i).Update(updatedChat);
                      // display the  notification
                      MakePopUpMessage(i,mChatrooms.get(i).getChatroomName(),mChatrooms.get(i).getDisplayName());
                    }
                }
            }
            mHandler.postDelayed(this, 7000); // i want to poll every 5 seconds
        }
    };

    /**
     * <pre>
     *     Usage : MakePopUpMessage(id,chatID,chatName)
     *       For : id is a Int
     *             chatID is a string
     *             chatName is a string
     *     After : creates a new notification on the phone under channel CHANNEL_ID
     * </pre>
     * @param id chatID client side
     * @param chatID chatID server side
     * @param chatName chatroom name
     */
    public void MakePopUpMessage(int id,String chatID,String chatName) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, ChatRoomMessageActivity.newIntent(getContext(),chatID), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.people_icon)
                .setContentTitle("New chat notification")
                .setContentText(chatName+" has a new chat messages waiting for you")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        //  notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }

    /**
     *  Android 8.0 and higher, must register the app's notification channel
     *  No worries if this is called many times because
     *  It's safe to call this repeatedly because creating an existing notification channel performs no operation.
     *  */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =(NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
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
