package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import hi.hugbo.verywowchat.Fragments.ChatroomAdminInviteFragment;
import hi.hugbo.verywowchat.Fragments.ChatroomInviteFragment;
import hi.hugbo.verywowchat.Fragments.JoinChatroomFragment;
import hi.hugbo.verywowchat.Fragments.UpdateChatroomFragment;
import hi.hugbo.verywowchat.Fragments.ViewChatroomFragment;

public class OwnerManageChatroomActivity extends FragmentActivity {

    // constants
    private static final String CHATROOM_NAME = "chatroom_name";

    // widgets
    TextView textView_chatroom_name;

    // non-widgets
    String mChatroomName;

    public static Intent newIntent(Context packageContext, String chatroomName) {
        Intent i = new Intent(packageContext, OwnerManageChatroomActivity.class);
        i.putExtra(CHATROOM_NAME, chatroomName);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_manage_chatroom);

        Intent intent = getIntent();
        mChatroomName = intent.getStringExtra(CHATROOM_NAME);

        textView_chatroom_name = findViewById(R.id.textView_chatroom_name);
        textView_chatroom_name.setText(mChatroomName);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.update_chatroom_fragment_container, UpdateChatroomFragment.newInstance(mChatroomName));
        ft.replace(R.id.member_invite_chatroom_fragment_container, ChatroomInviteFragment.newInstance(mChatroomName));
        ft.replace(R.id.admin_invite_chatroom_fragment_container, ChatroomAdminInviteFragment.newInstance(mChatroomName));
        // Complete the changes added above
        ft.commit();

    }

}
