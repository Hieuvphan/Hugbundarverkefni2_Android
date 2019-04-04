package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import hi.hugbo.verywowchat.fragments.LeaveChatroomFragment;
import hi.hugbo.verywowchat.fragments.ViewChatroomFragment;

public class MemberManageChatroomActivity extends FragmentActivity {

    // constants
    private static final String CHATROOM_NAME = "chatroom_name";

    // widgets
    TextView textView_chatroom_name;

    // non-widgets
    String mChatroomName;

    public static Intent newIntent(Context packageContext, String chatroomName) {
        Intent i = new Intent(packageContext, MemberManageChatroomActivity.class);
        i.putExtra(CHATROOM_NAME, chatroomName);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_manage_chatroom);

        Intent intent = getIntent();
        mChatroomName = intent.getStringExtra(CHATROOM_NAME);

        textView_chatroom_name = findViewById(R.id.textView_chatroom_name);
        textView_chatroom_name.setText(mChatroomName);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.view_chatroom_fragment_container, ViewChatroomFragment.newInstance(mChatroomName));
        ft.replace(R.id.leave_chatroom_fragment_container, LeaveChatroomFragment.newInstance(mChatroomName));
        // Complete the changes added above
        ft.commit();

    }

}
