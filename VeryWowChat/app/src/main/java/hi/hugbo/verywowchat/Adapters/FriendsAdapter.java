package hi.hugbo.verywowchat.Adapters;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hi.hugbo.verywowchat.Models.UserService;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.User;

import static android.content.Context.MODE_PRIVATE;

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
    private List<User> mFriends;

    public FriendsAdapter(List<User> friends) {
        mFriends = friends;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_friend_list_item, viewGroup, false);
        return new FriendItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        User friend = mFriends.get(i);
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
        // Service that handles our api calls
        private UserService mUserService;

        public FriendItemHolder(View itemView) {
            super(itemView);
            textFriendName = itemView.findViewById(R.id.txtUserName);
            btnDeleteFriend = itemView.findViewById(R.id.bntRemoveFriend);
            mUserService = UserService.getInstance();

        }

        public void bind(final User friend) {
            textFriendName.setText(friend.getDisplayName());

            /**
             * When the user clicks on the X button, we will display a snack back that
             * asks the user if he is sure if he wants to remove his friend from his friends list
             * */
            btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences UserInfo = v.getContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
                    ShowSnackBar(friend,UserInfo.getString("token","N/A"));
                }
            });
        }

        public void ShowSnackBar(final User friend, final String token){
            /*
             * A Snackbar is basically like a Toast its a pop up that is displayed for a short period of time
             * it is even ment to replace the Toast in the future, the reason why we use Snackbar here over toast is
             * the Snackbar has added features such as displaying buttons or even custom layouts in the pop-up
             * which we want in this case.
             *
             * We want to display this Snack for a short time, since the user should be 100% sure that he wants to remove his friend.
             * */
            Snackbar snackbar = Snackbar.make(btnDeleteFriend,"Are you sure you want remove "+friend.getDisplayName()+" from you're friends?",Snackbar.LENGTH_LONG);

            // Add a button in the Snack and give it the functionality to call the API to remove his friend.
            snackbar.setAction("YES Remove", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String removedFriend = mUserService.RemoveFriend(friend.getUsername(),token);
                    Toast.makeText(v.getContext(),removedFriend,Toast.LENGTH_LONG).show();
                    int friendToRemove =  mFriends.indexOf(friend);
                    mFriends.remove(friendToRemove);
                    notifyItemRemoved(friendToRemove);
                }
            });
            snackbar.show();
        }

    }
}
