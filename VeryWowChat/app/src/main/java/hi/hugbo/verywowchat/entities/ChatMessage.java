package hi.hugbo.verywowchat.entities;

import android.graphics.Bitmap;

/**
 * @Author Róman
 * When we receive a message from API we map that JSON object into this POJO
 * */
public class ChatMessage {
    // chatroom message in text form
    private String Message;
    // indicates if this message was send by this user ( TO display his messages on right side)
    private Boolean MyMessage;
    // hold image if the user send an image
    private Bitmap Bitmap;

    public ChatMessage(String Message, Boolean MyMessage) {
        this.Message = Message;
        this.MyMessage = MyMessage;
        this.Bitmap = null;
    }

    public ChatMessage(boolean MyMessage,Bitmap Bitmap) {
        this.MyMessage = MyMessage;
        this.Bitmap = Bitmap;
        this.MyMessage = null;
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

}
