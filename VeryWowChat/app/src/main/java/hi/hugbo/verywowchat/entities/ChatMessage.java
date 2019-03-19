package hi.hugbo.verywowchat.entities;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @Author Róman
 * When we receive a message from API we map that JSON object into this POJO
 */
public class ChatMessage {
    // Username of the sender
    private String Username;
    // Displayname of the sender
    private String DisplayName;
    // chatroom message in text form
    private String Message;
    // timestamp of the last message received from the chatroom
    private int TimeStamp;
    // hold image if the user send an image
    private Bitmap Bitmap;
    // indicates if this message was send by this user ( TO display his messages on right side)
    private Boolean MyMessage;

    public ChatMessage(String username, String displayName, String message, int timeStamp, String OwnerOfThisAcc) {
        Log.d("dh", "ChatMessage()");
        Username = username;
        DisplayName = displayName;
        Message = message;
        TimeStamp = timeStamp;
        Bitmap = null;
        if (username.equals(OwnerOfThisAcc)) {
            MyMessage = true;
        } else {
            MyMessage = false;
        }
    }

    public ChatMessage(String username, String displayName, String message, Bitmap img, int timeStamp, String OwnerOfThisAcc) {
        Log.d("dh", "ChatMessage()");
        Username = username;
        DisplayName = displayName;
        Message = message;
        TimeStamp = timeStamp;
        Bitmap = img;
        if (username.equals(OwnerOfThisAcc)) {
            MyMessage = true;
        } else {
            MyMessage = false;
        }
    }

    public void setBitmap(Bitmap img) {
        Bitmap = img;
    }

    public String getMessage() {
        return Message;
    }

    public Boolean getMyMessage() {
        return MyMessage;
    }

    public android.graphics.Bitmap getBitmap() {
        return Bitmap;
    }

    public String getUsername() {
        return Username;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public int getTimeStamp() {
        return TimeStamp;
    }
}
