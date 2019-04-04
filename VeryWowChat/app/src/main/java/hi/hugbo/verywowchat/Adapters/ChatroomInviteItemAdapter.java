package hi.hugbo.verywowchat.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hi.hugbo.verywowchat.Models.Interfaces.IChatroomService;
import hi.hugbo.verywowchat.Models.Implementations.ChatroomService;
import hi.hugbo.verywowchat.controllers.R;
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
public class ChatroomInviteItemAdapter extends RecyclerView.Adapter {

    /** Holds over all the chat messages we display on the screen */
    private List<Chatroom> mChatrooms;

    private IChatroomService chatroomService = new ChatroomService();

    /** Just reminder for myself : You can pass the Context from the Activity to this constructor
     *  then u can implement listeners and other things on the inflated view widgets.*/
    public ChatroomInviteItemAdapter(List<Chatroom> chatrooms) {
        mChatrooms = chatrooms;
    }

    /**
     * inflate the appropriate viewHolder so we could bind the data correctly to it
     * @param parent
     * @param viewType
     * @return new instance of ChatroomItemHolder
     */
    @Override
    public InviteItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_item, parent, false);
        return new InviteItemHolder(view);
    }

    /**
     * bind the data to the appropriate viewHolder
     * @param viewHolder
     * @param i the index
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Chatroom chatroom = mChatrooms.get(i);
        ((InviteItemHolder) viewHolder).bind(chatroom);
    }

    @Override
    public int getItemCount() {
        return mChatrooms.size();
    }

    private void removeInvite(Chatroom chatroom){
        int i = mChatrooms.indexOf(chatroom);
        mChatrooms.remove(i);
        notifyItemRemoved(i);
    }

    /** -------------------------------------------------------------------------------------------
     * ------------------------------------- VIEWHOLDERS ------------------------------------------
     * --------------------------------------------------------------------------------------------*/

    /** we can add or change viewholders later f.x when we need to add images it will be super
     * ez we just create a new view that displays the image and create a viewholder for it */


    private class InviteItemHolder extends RecyclerView.ViewHolder{

        private TextView invitee_header;
        private Button invite_accept;
        private Button invite_reject;

        Context context;

        String mChatroomName;

        public InviteItemHolder(View itemView) {
            super(itemView);
            invitee_header = itemView.findViewById(R.id.invitee_header);
            invite_accept = itemView.findViewById(R.id.invite_accept);
            invite_reject = itemView.findViewById(R.id.invite_reject);

            context = itemView.getContext();
        }

        public void bind(final Chatroom chatroom) {
            // set the text
            invitee_header.setText(chatroom.getDisplayName()+"("+chatroom.getChatroomName()+")");


            SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
            final String token = userInfo.getString("token","n/a");

            // fetch the chatroom name
            mChatroomName = chatroom.getChatroomName();
            // when button is pressed, attempt to join the given chatroom
            invite_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        chatroomService.joinChatroom(token, mChatroomName);

                        // remove from invite list
                        removeInvite(chatroom);

                        Toast.makeText(context.getApplicationContext(),"Successfully joined chatroom",Toast.LENGTH_LONG).show();
                    } catch(Exception e) {
                        Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
            // when button is pressed, attempt to reject the chatroom invite
            invite_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        chatroomService.declineChatroomInvite(token, mChatroomName);

                        // remove from invite list
                        removeInvite(chatroom);

                        Toast.makeText(context.getApplicationContext(),"Successfully rejected chatroom invite",Toast.LENGTH_LONG).show();
                    } catch(Exception e) {
                        Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
