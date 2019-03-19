package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hi.hugbo.verywowchat.Adapters.ChatMessageAdapter;
import hi.hugbo.verywowchat.Models.ChatRoomMessageService;
import hi.hugbo.verywowchat.entities.ChatMessage;

/** https://guides.codepath.com/android/using-the-recyclerview
 * @Author Róman
 * ChatRoomMessageActivity is responsible for showing the text messages of a specific chat room
 * and allow the user to send his own messages to the chat room. There are quite allot of things
 * needed to make this work.
 *
 * When ChatRoomMessageActivity is started  we need the following :
 *  1. In SharedPreferences should exist user along with his/hers JWT (Json Web Token)
 *  2. The chatID should be passed as a String in Extras
 *
 * RecycleView (widget):
 *  Displays chat messages on the screen, it uses ChatMessageAdapter to keep track
 *  of all the displayed messages and also display of new messages be it img,file or text
 *  and then the Adapater needs ViewHolders (more info in that class)
 *
 * Polling for messages on main Thread :
 *   We Poll messages every 2 seconds, the main chat should poll
 *   data on the main thread since the entire activity relies on the polling
 * */
public class ChatRoomMessageActivity extends AppCompatActivity {

    /* constant instead of literals to ensure we use the same key when putting and getting the extra
    *  this is something that andriod prefers we do.*/
    private static final String CHAT_ROOM_ID = "CHAT_ID";

    // widgets
    private ImageButton mBtnSendTxtMSG; // send text message button
    private TextView mUserTextMSG; // user text input that will be send to the chat

    // non-widgets
    private ChatRoomMessageService mChatCaller = ChatRoomMessageService.getInstance();// want only a singleton of this instance
    private List<ChatMessage> mChatMessages; // chat messages in the chat room
    private ChatMessageAdapter mChatAdapter; // adapter that will display the messages
    private String mChatRoomID;  // id of the chatroom that the user is talking on
    private SharedPreferences mUserInfo; // user info
    private Handler mHandler; // Hander is used for implementing polling
    private int mChatOffestFRONT; // offset on the chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dh", "ChatRoomMessageActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_message);

        /* Shared preferences allows us to store and retrieve data in a form of a (key,value) pairs.
           UserInfo stores 3 keys  1 = displayname , 2 = username, 3 = token */
        mUserInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);

        // Fetch ChatRoomID from Extras
        Intent intent = getIntent(); // Fetch the intent that started this activity

        /* u can and should check if this string exists first but without this string
        *  this activity cannot function so we roll the dice ... (BE PERFECT LIKE HOLY PROGRAMMERS IN DOOM) */
        mChatRoomID = intent.getStringExtra(CHAT_ROOM_ID); // get the chatID

        /* -----------------------------------------------------------------------------------------
        * --------------------------------- RecycleView INIT START ---------------------------------
        * -----------------------------------------------------------------------------------------*/
        mChatMessages = new ArrayList<>();

        /*
          RecyclerView provides 3 built-in layout managers:
            - LinearLayoutManager shows items in a vertical or horizontal scrolling list. (Like chat msg's)
            - GridLayoutManager shows items in a grid.
            - StaggeredGridLayoutManager shows items in a staggered grid.
         */

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        /* We dont need a reference to the RecycleView since you should never work with it directly
        *  insted we want to use the Adapter to display new messages
        *  There are many method available to use when notifying the adapter of different changes:
             - notifyItemChanged(int pos)	Notify that item at position has changed.
             - notifyItemInserted(int pos)	Notify that item reflected at position has been newly inserted.
             - notifyItemRemoved(int pos)	Notify that items previously located at position has been removed from the data set.
             - notifyDataSetChanged()	Notify that the dataset has changed. Use only as last resort !!!!!!!!!!!!!!!!
         */
        RecyclerView recyclerView = findViewById(R.id.chatMessages);
        recyclerView.setLayoutManager(layoutManager);
        mChatAdapter = new ChatMessageAdapter(mChatMessages);
        recyclerView.setAdapter(mChatAdapter);

        /* -----------------------------------------------------------------------------------------
         * --------------------------------- RecycleView INIT END ----------------------------------
         * -----------------------------------------------------------------------------------------*/

        // Assign references to widgets that we use
        mUserTextMSG = findViewById(R.id.edit_chatroom_sendmsg);


        /* mBtnSendTxtMSG will listen to be clicked on, when it will be clicked it will
         * make HTTP request via api_caller to send the user's text msg to the chat room */
        mBtnSendTxtMSG = findViewById(R.id.btn_submit_txt_msg);
        mBtnSendTxtMSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if there is no text then we dont make the HTTP Request
                if(mUserTextMSG.getText().toString().isEmpty()){
                    return;
                }
                // Create a Map from the data provided by the user
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", mUserTextMSG.getText().toString());
                try {
                    mChatCaller.SendChatMessage(mChatRoomID,mUserInfo.getString("token","n/a"),params);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogOutUser();
                }
                mUserTextMSG.setText("");
            }
        });

        // before stating the polling we need to know the initial offset of the chat room
        try {
           mChatOffestFRONT = mChatCaller.GetCountChatLogs(mChatRoomID,mUserInfo.getString("token","n/a"));
        } catch (Exception e) {
            e.printStackTrace();
            LogOutUser();
        }

        /* Start the Polling */
        mHandler = new Handler();
        mHandler.post(PollChatMsg);// Start the initial runnable task by posting through the handler
    }

    public static Intent newIntent(Context packageContext,String chatID) {
        Intent i = new Intent(packageContext, ChatRoomMessageActivity.class);
        i.putExtra(CHAT_ROOM_ID,chatID);
        return i;
    }

    /**
     * We need to remember that we could have pending code execution so we need
     * remove all the scheduled executions of a runnable
     *  */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(PollChatMsg);
    }

    /**
     * All the necessary things that have to be done inorder to logout the user from the app
     * */
    public void LogOutUser(){
        SharedPreferences.Editor spreferencesEditor = mUserInfo.edit();
        spreferencesEditor.clear();
        spreferencesEditor.commit();
        // redirect the user to the login activity
        startActivity(LoginActivity.newIntent(ChatRoomMessageActivity.this));
    }

    /**
     * We use a Handler to run code on a given thread.
     * This is done by constructing a Handler and then "posting" Runnable code
     * to the event message queue on the thread to be processed.
     * We will make a HTTP REQUEST every 2 seconds to the API for chat messages
     *
     * All the messages are of a Form so we need to map them to our ChatMessage Object
     *   {
     *      "senderUsername": "dah38",
     *      "senderDisplayName": "Davíð",
     *      "message": "Message 34",
     *      "timestamp": 1542540162938
     *   }
     * */
    private Runnable PollChatMsg = new Runnable() {
        @Override
        public void run() {
            try {
                int lastTime = -1;
                /* if a timestamp exists we use it to prevent mapping of old messages */
                if(mChatMessages.size() >0 ){
                    lastTime = mChatMessages.get(mChatMessages.size()-1).getTimeStamp();
                }

                // get our new chat messages
                List<ChatMessage> newMessages = mChatCaller.GetChatLogs(
                        mChatRoomID,
                        mUserInfo.getString("token","n/a"),
                        mChatOffestFRONT,
                        mUserInfo.getString("username","n/a"),
                        lastTime
                );

                if(newMessages.size() > 0 ) {
                    // need to save last message size
                    int currSize = mChatMessages.size();
                    // add the new messages to the messages we already have
                    mChatMessages.addAll(newMessages);
                    // notify the adapter of new objects that neeed to be displayed
                    mChatAdapter.notifyItemRangeInserted(currSize, newMessages.size());

                    // create a new offset
                    mChatOffestFRONT += newMessages.size();
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogOutUser();
            }

            /* Repeat this the same runnable code block again another 2 seconds
               'this' is referencing the Runnable object */
           mHandler.postDelayed(this, 2000);
        }
    };
}
