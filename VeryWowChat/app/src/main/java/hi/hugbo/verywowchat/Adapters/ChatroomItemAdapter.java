package hi.hugbo.verywowchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hi.hugbo.verywowchat.controllers.ChatRoomMessageActivity;
import hi.hugbo.verywowchat.controllers.MyChatroomsActivity;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.ChatMessage;
import hi.hugbo.verywowchat.entities.Chatroom;

/**
 * @Author Vilhelm
 * The ChatMessageAdapter responsibility is to convert an Chatroom object in a List at position X
 * into a list row item to be inserted into RecyclerView. The adapter requires the existence of a "ViewHolder" object
 * which describes and provides access to all the widgets, in our case we got 2 ChatroomHolder
 * defined in the bottom of this class
 *          ( Reason they are defined here is cuz we can use a different onBindViewHolder function which makes things much simpler
 *            and also overall these Handlers should be private noone except the adapter who uses them should have access to them  )
 *
 * Every adapter has three primary methods that neeed to be overridden:
 *   - onCreateViewHolder to inflate the item layout and create the holder
 *   - onBindViewHolder to set the view attributes based on the data
 *   - getItemCount to determine the number of items
 * (Note u may be overwriting different variants of this methods based on how you extend the Adapter class)  */
public class ChatroomItemAdapter extends RecyclerView.Adapter {

    /** Holds over all the chat messages we display on the screen */
    private List<Chatroom> mChatrooms;

    /** Just reminder for myself : You can pass the Context from the Activity to this constructor
     *  then u can implement listeners and other things on the inflated view widgets.*/
    public ChatroomItemAdapter(List<Chatroom> chatrooms) {
        mChatrooms = chatrooms;
    }

    /**
     * inflate the appropriate viewHolder so we could bind the data correctly to it
     * @param parent
     * @param viewType
     * @return new instance of ChatroomItemHolder
     */
    @Override
    public ChatroomItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_list_item, parent, false);
        return new ChatroomItemHolder(view);
    }

    /**
     * bind the data to the appropriate viewHolder
     * @param viewHolder
     * @param i the index
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Chatroom chatroom = mChatrooms.get(i);
        ((ChatroomItemHolder) viewHolder).bind(chatroom);
    }

    @Override
    public int getItemCount() {
        return mChatrooms.size();
    }

    /** -------------------------------------------------------------------------------------------
     * ------------------------------------- VIEWHOLDERS ------------------------------------------
     * --------------------------------------------------------------------------------------------*/

    /** we can add or change viewholders later f.x when we need to add images it will be super
     * ez we just create a new view that displays the image and create a viewholder for it */


    private class ChatroomItemHolder extends RecyclerView.ViewHolder{

        private Button BtnManageChatroom;

        public ChatroomItemHolder(View itemView) {
            super(itemView);
            BtnManageChatroom = itemView.findViewById(R.id.btn_manage_chatroom);
        }

        public void bind(final Chatroom chatroom) {
            // set the text
            BtnManageChatroom.setText(chatroom.getDisplayName()+"("+chatroom.getChatroomName()+")");

            BtnManageChatroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    context.startActivity(ChatRoomMessageActivity.newIntent(
                        context.getApplicationContext(),
                        chatroom.getChatroomName()
                    ));
                }
            });
        }
    }
}
