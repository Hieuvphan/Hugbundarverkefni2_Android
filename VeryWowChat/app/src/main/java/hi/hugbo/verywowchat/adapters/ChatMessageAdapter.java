package hi.hugbo.verywowchat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.ChatMessage;

/**
 * @Author Róman
 * The ChatMessageAdapter responsibility is to convert an ChatMessage object in a List at position X
 * into a list row item to be inserted into RecyclerView. The adapter requires the existence of a "ViewHolder" object
 * which describes and provides access to all the widgets, in our case we got 2 SentMessageHolder and ReceivedMessageHolder
 * both defined in the bottom of this class
 *          ( Reason they are defined here is cuz we can use a different onBindViewHolder function which makes things much simpler
 *            and also overall these Handlers should be private noone except the adapter who uses them should have access to them  )
 *
 * Every adapter has three primary methods that neeed to be overridden:
 *   - onCreateViewHolder to inflate the item layout and create the holder
 *   - onBindViewHolder to set the view attributes based on the data
 *   - getItemCount to determine the number of items
 * (Note u may be overwriting different variants of this methods based on how you extend the Adapter class)  */
public class ChatMessageAdapter extends RecyclerView.Adapter {

    /**
     * These flags will be used to determine what type of content we are dealing with
     * and help us inflate the appropriate view for the job.
     * The Reason for declaring flags is cuz android does not trust humans due to human error
     * so they want us to declare these flags
     * */
    private static final int SENT_BY_US = 0;
    private static final int SENT_BY_THEM = 1;
    private static final int SENT_BY_US_IMAGE = 2;
    private static final int SENT_BY_THEM_IMAGE = 3;

    /** Holds over all the chat messages we display on the screen */
    private List<ChatMessage> mMessages;

    /** Just reminder for myself : You can pass the Context from the Activity to this constructor
     *  then u can implement listeners and other things on the inflated view widgets.*/
    public ChatMessageAdapter(List<ChatMessage> messages) {
        mMessages = messages;
    }

    /**
     * To solve the 1st problem in onCreateViewHolder we override this method to create our
     * own flags to help us distinguish the content we receive */
    @Override
    public int getItemViewType(int position) {
        // get the message that will be displayed next on the screen
        ChatMessage message =  mMessages.get(position);

        Boolean sentByUs = message.getMyMessage();

        // if the message is sent to the chat by us and its a text message
        if (sentByUs && message.getMessage() != null && message.getBitmap() == null) {
            return SENT_BY_US;
        }
        // if the message is not sent to the chat by us and its a text message
        else if(!sentByUs && message.getMessage() != null && message.getBitmap() == null){
            return SENT_BY_THEM;
        }
        // if the message is sent to the chat by us and its a image
        else if(sentByUs && message.getBitmap() != null){
            return SENT_BY_US_IMAGE;
        }
        // if the message is not sent to the chat by us and its a image
        else {
            return SENT_BY_THEM_IMAGE;
        }
    }

    /**
     * We need to do 3 things
     *  1: Find out who send the message (was sent by us or received from others)
     *  2: Find out if its a img,file or a text message
     *  3: inflate the appropriate viewHolder so we could bind the data correctly to it */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // Based on the type of ViewType we receive we inflate the correct View
        if(viewType == SENT_BY_US){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbouble_send, parent, false);
            return new SentMessageHolder(view);
        }
        // if we send the img
        else if(viewType == SENT_BY_US_IMAGE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbouble_send_img,parent,false);
            return new SentImageHolder(view);
        }
        else if(viewType == SENT_BY_THEM_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbouble_received_img,parent,false);
            return new ReceiveImageHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbouble_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    /**
     * We need to do 3 things
     *  1: Find out who send the message (was sent by us or received from others)
     *  2: Find out if its a img,file or a text message
     *  3: bind the data to the appropriate viewHolder
     *  */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ChatMessage message = mMessages.get(i);

        // get the ViewType and bind the data
        switch (viewHolder.getItemViewType()) {
            case SENT_BY_US:
                ((SentMessageHolder) viewHolder).bind(message);
                break;
            case SENT_BY_THEM:
                ((ReceivedMessageHolder) viewHolder).bind(message);
                break;
            case  SENT_BY_US_IMAGE:
                ((SentImageHolder) viewHolder).bind(message);
                break;
            case  SENT_BY_THEM_IMAGE:
                ((ReceiveImageHolder) viewHolder).bind(message);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /** -------------------------------------------------------------------------------------------
     * ------------------------------------- VIEWHOLDERS ------------------------------------------
     * --------------------------------------------------------------------------------------------*/

    /** we can add or change viewholders later f.x when we need to add images it will be super
     * ez we just create a new view that displays the image and create a viewholder for it */


    private class SentMessageHolder extends RecyclerView.ViewHolder{

        private TextView textMessage;

        public SentMessageHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message_body);
        }

        public void bind(ChatMessage message) {
            textMessage.setText(message.getMessage());
        }
    }
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{

        private TextView textMessage;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message_body);
        }
        public void bind(ChatMessage message) {
            textMessage.setText(message.getMessage());
        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder{

        private TextView textMessage;
        private ImageView ImgView;

        public SentImageHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message_body);
            ImgView = itemView.findViewById(R.id.img_view);
        }

        public void bind(ChatMessage message) {
            ImgView.setImageBitmap(message.getBitmap());
            textMessage.setText(message.getMessage());
        }
    }
    private class ReceiveImageHolder extends RecyclerView.ViewHolder{

        private TextView textMessage;
        private ImageView ImgView;

        public ReceiveImageHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message_body);
            ImgView = itemView.findViewById(R.id.img_view);
        }

        public void bind(ChatMessage message) {
            ImgView.setImageBitmap(message.getBitmap());
            textMessage.setText(message.getMessage());
        }
    }
}
