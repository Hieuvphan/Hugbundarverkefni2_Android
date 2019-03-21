package hi.hugbo.verywowchat.Adapters;

import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.ChatMessage;
import hi.hugbo.verywowchat.entities.Friend;

/**
 * @Author Róman
 * The FriendsAdapter responsibility is to convert an Friend object in a List at position X
 * into a list row item to be inserted into RecyclerView. The adapter requires the existence of a "ViewHolder" object
 * which describes and provides access to all the widgets, in our case we got FriendViewHolder
 *
 * Every adapter has three primary methods that neeed to be overridden:
 *   - onCreateViewHolder to inflate the item layout and create the holder
 *   - onBindViewHolder to set the view attributes based on the data
 *   - getItemCount to determine the number of items
 * (Note u may be overwriting different variants of this methods based on how you extend the Adapter class)
 */
public class FriendsAdapter extends RecyclerView.Adapter {

    // holds all the friends we display on the screen
    private List<Friend> mFriends;

    public FriendsAdapter(List<Friend> friends) {
        mFriends = friends;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_friend_list_item, viewGroup, false);
        return new FriendItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Friend friend = mFriends.get(i);
        ((FriendItemHolder) viewHolder).bind(friend);
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    /** -------------------------------------------------------------------------------------------
     * ------------------------------------- VIEWHOLDERS ------------------------------------------
     * --------------------------------------------------------------------------------------------*/

    private class FriendItemHolder extends RecyclerView.ViewHolder {

        private TextView textFriendName;
        private ImageButton btnDeleteFriend;

        public FriendItemHolder(View itemView) {
            super(itemView);
            textFriendName = itemView.findViewById(R.id.txtUserName);
            btnDeleteFriend = itemView.findViewById(R.id.bntRemoveFriend);
        }

        public void bind(Friend friend) {
            textFriendName.setText(friend.getDisplayName());

            btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("wtf","hello");
                }
            });
        }
    }
}
