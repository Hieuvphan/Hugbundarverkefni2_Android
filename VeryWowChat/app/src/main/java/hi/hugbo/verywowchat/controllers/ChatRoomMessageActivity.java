package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hi.hugbo.verywowchat.adapters.ChatMessageAdapter;
import hi.hugbo.verywowchat.models.implementations.ChatRoomMessageService;
import hi.hugbo.verywowchat.entities.ChatMessage;

/**
 * https://guides.codepath.com/android/using-the-recyclerview
 *
 * @Author Róman
 * ChatRoomMessageActivity is responsible for showing the text messages of a specific chat room
 * and allow the user to send his own messages to the chat room. There are quite allot of things
 * needed to make this work.
 * <p>
 * When ChatRoomMessageActivity is started  we need the following :
 * 1. In SharedPreferences should exist user along with his/hers JWT (Json Web Token)
 * 2. The chatID should be passed as a String in Extras
 * <p>
 * RecycleView (widget):
 * Displays chat messages on the screen, it uses ChatMessageAdapter to keep track
 * of all the displayed messages and also display of new messages be it img,file or text
 * and then the Adapater needs ViewHolders (more info in that class)
 * <p>
 * Polling for messages on main Thread :
 * We Poll messages every 2 seconds, the main chat should poll
 * data on the main thread since the entire activity relies on the polling
 */
public class ChatRoomMessageActivity extends AppCompatActivity {

    // TODO: I must somehow retrieve the image and reenter this activity/form, just as how I left it.
    // Once the image has been retrieved I'll have to, somehow, encode the bytes of the image as
    // base64.  Then I'll use similar code as Roman wrote to send the image its merry way.

    // NOTE: I think this will be a mixed bag.  So, OK, I take the image, then I need to base64
    // encode the bytes, and put it awkwardly in a JSON list stringified D:


    /* constant instead of literals to ensure we use the same key when putting and getting the extra
     *  this is something that andriod prefers we do.*/
    private static final String FINAL_ID = "MYFUCKINGASSPARTWHYUNOWORK";

    // widgets
    private ImageButton mBtnSendTxtMSG; // send text message button
    private TextView mUserTextMSG; // user text input that will be send to the chat

    private ImageButton mBtnTakePicture; // button to take picture

    private ImageButton mBtnSendAttachment; // button to pick

    // non-widgets
    private ChatRoomMessageService mChatCaller = ChatRoomMessageService.getInstance();// want only a singleton of this instance
    private List<ChatMessage> mChatMessages; // chat messages in the chat room
    private ChatMessageAdapter mChatAdapter; // adapter that will display the messages
    private String mChatRoomID;  // id of the chatroom that the user is talking on
    private SharedPreferences mUserInfo; // user info
    private SharedPreferences mCurrentChat; // current chat that is opened need to use this to comunicate between activities
    private Handler mHandler; // Hander is used for implementing polling
    private int mChatOffestFRONT; // offset on the chat
    private int mChatOffestBack; // back offset
    private RecyclerView recyclerView;

    // This is the request code that we then use to identify which *activity result* referred to
    // taking an image, i.e. when onActivityResult is called, a status code is sent with.  If the
    // status code is the same as this, then we have received an image.
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_PICK_FILE = 3;

    private String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.d("dh", "Environment.DIRECTORY_PICTURES: " + Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        // ChatRoomMessageActivity.this
        Log.d("dh", "Click!");
        // TODO: if possible, I want to immediately return after taking a picture.

        // TODO: I want to be able to somehow sense that the photography was cancelled.

        // Are intent is to take a picture.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Here we check if to ensure that at least some app exists on this phone that can
        // take a picture for us.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occured while create the File
                // TODO: figure out what we want to do here.
                Log.e("dh", "Could not create file!");
                ex.printStackTrace();
                toast("Error in taking picture");
                return;
            }

            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(
                        this,
                        "hi.hugbo.verywowchat.fileprovider",
                        photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                Log.d("dh", "Taking photo...");

                // So at this point there exists some app that can take a picture for us and
                // our app will not crash!
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }


        } else {
            toast("No app available to take picture.");
        }
    }

    // dispatchTakePictureIntent();
    private void dispatchUploadFile() {

        Log.d("dh", "find and send attachment...");


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        // intent.setType("file/*");

        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    private void toast(String msg) {
        Toast.makeText(
                getApplicationContext(),
                msg,
                Toast.LENGTH_LONG
        ).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("dh", "onCreate()");
        setContentView(R.layout.activity_chat_room_message);

        /* Shared preferences allows us to store and retrieve data in a form of a (key,value) pairs.
           UserInfo stores 3 keys  1 = displayname , 2 = username, 3 = token */
        mUserInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        mCurrentChat = getApplicationContext().getSharedPreferences("CurrentChat", MODE_PRIVATE);

        // Fetch ChatRoomID from Extras
        Intent intent = getIntent(); // Fetch the intent that started this activity

        /* u can and should check if this string exists first but without this string
         *  this activity cannot function so we roll the dice ... (BE PERFECT LIKE HOLY PROGRAMMERS IN DOOM) */
        mChatRoomID = intent.getStringExtra(FINAL_ID); // get the chatID

        // the chat room ID so other acitvities know what what room is opened
        SharedPreferences.Editor chatEditor = mCurrentChat.edit();
        chatEditor.putString("ChatID", mChatRoomID);
        chatEditor.commit();

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

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        /* We dont need a reference to the RecycleView since you should never work with it directly
        *  insted we want to use the Adapter to display new messages
        *  There are many method available to use when notifying the adapter of different changes:
             - notifyItemChanged(int pos)	Notify that item at position has changed.
             - notifyItemInserted(int pos)	Notify that item reflected at position has been newly inserted.
             - notifyItemRemoved(int pos)	Notify that items previously located at position has been removed from the data set.
             - notifyDataSetChanged()	Notify that the dataset has changed. Use only as last resort !!!!!!!!!!!!!!!!
         */
        recyclerView = findViewById(R.id.chatMessages);
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
                // NOTE: here messages are sent.

                String message = mUserTextMSG.getText().toString().trim();

                // if there is no text then we don't make the HTTP Request
                if (message.isEmpty()) {
                    return;
                }
                // Create a Map from the data provided by the user
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", message);
                try {
                    mChatCaller.SendChatMessage(mChatRoomID, mUserInfo.getString("token", "n/a"), params);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogOutUser();
                }
                mUserTextMSG.setText("");
            }
        });


        // TAKE PICTURE
        mBtnTakePicture = findViewById(R.id.btn_take_picture);
        mBtnTakePicture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        mBtnSendAttachment = findViewById(R.id.btn_submit_file);
        mBtnSendAttachment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchUploadFile();
            }
        });


        // before stating the polling we need to know the initial offset of the chat room
        try {
            mChatCaller.NotifyRead(mChatRoomID, mUserInfo.getString("token", "n/a"));
            mChatOffestFRONT = mChatCaller.GetCountChatLogs(mChatRoomID, mUserInfo.getString("token", "n/a"));
            mChatOffestBack = mChatOffestFRONT -1;
        } catch (Exception e) {
            e.printStackTrace();
            LogOutUser();
        }

        /* Start the Polling */
        mHandler = new Handler();
        mHandler.post(PollChatMsg);// Start the initial runnable task by posting through the handler

        // if the user scrolled to the top then we need to display older messages
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();
                // check if the user has reached top of the screen
                if (pastVisibleItems  == 0) {
                    if(mChatMessages.size() < 4) { return;} // fixes small bug that would poll 2 many msg's at once
                    if(mChatOffestBack < 4) { return; } // if nothing is left
                    GetOldMessages(); // fetch old messages
                }
            }
        });
    }

    /**
     * @param packageContext
     * @param chatID
     * @return
     */
    public static Intent newIntent(Context packageContext, String chatID) {
        Intent i = new Intent(packageContext, ChatRoomMessageActivity.class);
        i.putExtra(FINAL_ID, chatID);
        return i;
    }



    private void sendPicture() {
        File imgFile = new File(currentPhotoPath);

        if (imgFile.exists()) {
            Log.d("dh", "Image file exists!");

            int size = (int) imgFile.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imgFile));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                toast("Unable to send picture");
                return;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                toast("Unable to send picture");
                return;
            }

            String value = Base64.encodeToString(bytes, Base64.DEFAULT);
            Log.d("dh", "Base64 of file: " + value);

            Map<String, String> params = new HashMap<String, String>();
            params.put("message", "");
            params.put("attachment", value);

            try {
                mChatCaller.SendChatMessage(
                        mChatRoomID,
                        mUserInfo.getString("token", "n/a"),
                        params
                );
            } catch (Exception e) {
                e.printStackTrace();
                LogOutUser();
            }

        }
    }

    private void sendFile(Uri uri) {
        Log.d("dh", "Sending file...");
        Log.d("dh", uri.getPath());

        try {
            InputStream is = getContentResolver().openInputStream(uri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;

            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            byte[] bytes = baos.toByteArray();

            String value = Base64.encodeToString(bytes, Base64.DEFAULT);

            Log.d("dh", "Value: " + value);

            Map<String, String> params = new HashMap<String, String>();
            params.put("message", "");
            params.put("attachment", value);

            try {
                mChatCaller.SendChatMessage(
                        mChatRoomID,
                        mUserInfo.getString("token", "n/a"),
                        params
                );
            } catch (Exception e) {
                e.printStackTrace();
                toast("Unable to send chat message");
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
            toast("Unable to send file");
            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.d("dh", "onActivityResult()");
        Log.d("dh", "  Request code: " + requestCode);
        Log.d("dh", "  Result code: " + requestCode);
        Log.d("dh", "  Data: " + data);


        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (data != null) {
                    sendPicture();
                } else {
                    toast("Cancelled sending image!");
                }
                break;
            case REQUEST_PICK_FILE:
                if (data != null) {
                    Uri uri = data.getData();
                    sendFile(uri);
                } else {
                    toast("Cancelled sending file!");
                }
                break;
            default:
                break;
        }
    }


    /**
     * We need to remember that we could have pending code execution so we need
     * remove all the scheduled executions of a runnable
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("dh", "onDestroy()");
        mHandler.removeCallbacks(PollChatMsg);
        SharedPreferences.Editor chatEditor = mCurrentChat.edit();
        chatEditor.clear();
        chatEditor.commit();
    }

    /**
     * All the necessary things that have to be done inorder to logout the user from the app
     */
    public void LogOutUser() {
        SharedPreferences.Editor spreferencesEditor = mUserInfo.edit();
        spreferencesEditor.clear();
        spreferencesEditor.commit();
        // redirect the user to the login activity
        startActivity(LoginActivity.newIntent(ChatRoomMessageActivity.this));
    }

    public void  GetOldMessages(){
        try {

            // get our new chat messages
            List<ChatMessage> newMessages = mChatCaller.GetChatLogs(
                    mChatRoomID,
                    mUserInfo.getString("token", "n/a"),
                    mChatOffestBack - 4,
                    mUserInfo.getString("username", "n/a"),
                    -1
            );
            if (newMessages.size() > 0) {
                // add the new messages to the messages we already have
                mChatMessages.addAll(0,newMessages);
                mChatAdapter.notifyItemRangeInserted(0,newMessages.size());
                // create a new offset
                mChatOffestBack -= newMessages.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogOutUser();
        }
    }

    /**
     * We use a Handler to run code on a given thread.
     * This is done by constructing a Handler and then "posting" Runnable code
     * to the event message queue on the thread to be processed.
     * We will make a HTTP REQUEST every 2 seconds to the API for chat messages
     * <p>
     * All the messages are of a Form so we need to map them to our ChatMessage Object
     * {
     * "senderUsername": "dah38",
     * "senderDisplayName": "Davíð",
     * "message": "Message 34",
     * "timestamp": 1542540162938
     * }
     */
    private Runnable PollChatMsg = new Runnable() {
        @Override
        public void run() {
            try {
                int lastTime = -1;
                /* if a timestamp exists we use it to prevent mapping of old messages */
                if (mChatMessages.size() > 0) {
                    lastTime = mChatMessages.get(mChatMessages.size() - 1).getTimeStamp();
                }

                // get our new chat messages
                List<ChatMessage> newMessages = mChatCaller.GetChatLogs(
                        mChatRoomID,
                        mUserInfo.getString("token", "n/a"),
                        mChatOffestFRONT,
                        mUserInfo.getString("username", "n/a"),
                        lastTime
                );

                if (newMessages.size() > 0) {
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
