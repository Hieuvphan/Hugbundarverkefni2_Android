package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import hi.hugbo.verywowchat.Models.Interfaces.IChatroomService;
import hi.hugbo.verywowchat.Models.Implementations.ChatroomService;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.Chatroom;

/**
 * This fragment is an independent component for viewing a chatroom
 */
public class ViewChatroomFragment extends Fragment {

    private TextView edit_chatroom_displayname;
    private TextView edit_chatroom_description;
    private Switch switch_listed;
    private Switch switch_invited_only;
    private TextView edit_chatroom_tags;
    private Button btn_create_chatroom;

    public ViewChatroomFragment() {

    }

    private IChatroomService chatroomService = new ChatroomService();
    private static final String CHATROOM_NAME = "join_chatroom_name";
    private String mChatroomName;

    public static ViewChatroomFragment newInstance(String chatroomName){
        ViewChatroomFragment fragment = new ViewChatroomFragment();

        Bundle args = new Bundle();
        args.putString(CHATROOM_NAME, chatroomName);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_chatroom, container, false);

        // fetch the widgets
        edit_chatroom_displayname = rootView.findViewById(R.id.edit_chatroom_displayname);
        edit_chatroom_description = rootView.findViewById(R.id.edit_chatroom_description);
        switch_listed = rootView.findViewById(R.id.switch_listed);
        switch_invited_only = rootView.findViewById(R.id.switch_invited_only);
        edit_chatroom_tags = rootView.findViewById(R.id.edit_chatroom_tags);

        // fetch the user token
        final Context context = rootView.getContext();
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        // fetch the chatroom name
        mChatroomName = getArguments().getString(CHATROOM_NAME);

        // fetch data and fill widgets
        Chatroom chatroom;
        try{
            chatroom = chatroomService.getChatroom(token, mChatroomName);

            // fill widgets with chatroom data
            edit_chatroom_displayname.setText(chatroom.getDisplayName());
            edit_chatroom_description.setText((chatroom.getDescription()));
            switch_listed.setChecked(chatroom.getListed());
            switch_invited_only.setChecked((chatroom.getInvited_only()));
            edit_chatroom_tags.setText(chatroom.getTagString());
        } catch(Exception e) {
            Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return rootView;
    }

}
