package hi.hugbo.verywowchat.adapters;

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

import hi.hugbo.verywowchat.models.implementations.UserService;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.User;

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
public class FriendRequestItemAdapter extends RecyclerView.Adapter {

    /** Holds over all the chat messages we display on the screen */
    private List<User> mUsers;

    private UserService userService = UserService.getInstance();

    /** Just reminder for myself : You can pass the Context from the Activity to this constructor
     *  then u can implement listeners and other things on the inflated view widgets.*/
    public FriendRequestItemAdapter(List<User> users) {
        mUsers = users;
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
        User user = mUsers.get(i);
        ((InviteItemHolder) viewHolder).bind(user);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    private void removeInvite(User user){
        int i = mUsers.indexOf(user);
        mUsers.remove(i);
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

        String mRequestorUsername;

        public InviteItemHolder(View itemView) {
            super(itemView);
            invitee_header = itemView.findViewById(R.id.invitee_header);
            invite_accept = itemView.findViewById(R.id.invite_accept);
            invite_reject = itemView.findViewById(R.id.invite_reject);

            context = itemView.getContext();
        }

        public void bind(final User user) {
            // set the text
            invitee_header.setText(user.getDisplayName()+"("+user.getUsername()+")");


            SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
            final String token = userInfo.getString("token","n/a");

            // fetch the chatroom name
            mRequestorUsername = user.getUsername();
            // when button is pressed, attempt to become admin of chatroom
            invite_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        userService.addFriend(token, mRequestorUsername);

                        // remove from invite list
                        removeInvite(user);

                        Toast.makeText(context.getApplicationContext(),"Successfully added friend",Toast.LENGTH_LONG).show();
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
                        userService.declineFriendRequest(token, mRequestorUsername);

                        // remove from invite list
                        removeInvite(user);

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
