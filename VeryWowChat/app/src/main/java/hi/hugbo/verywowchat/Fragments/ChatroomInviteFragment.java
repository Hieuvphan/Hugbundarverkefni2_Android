package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.R;

public class ChatroomInviteFragment extends Fragment {

    public ChatroomInviteFragment() {

    }

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private static final String CHATROOM_NAME = "chatroom_name";
    private String mChatroomName;

    EditText edit_chatroom_invitee;
    Button btn_chatroom_invite;

    public static ChatroomInviteFragment newInstance(String chatroomName){
        ChatroomInviteFragment fragment = new ChatroomInviteFragment();

        Bundle args = new Bundle();
        args.putString(CHATROOM_NAME, chatroomName);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_member, container, false);

        edit_chatroom_invitee = rootView.findViewById(R.id.edit_chatroom_invitee);
        btn_chatroom_invite = rootView.findViewById(R.id.btn_chatroom_invite);

        final Context context = rootView.getContext();
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        mChatroomName = getArguments().getString(CHATROOM_NAME);
        btn_chatroom_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String invitee = edit_chatroom_invitee.getText().toString();
                    chatroomService.inviteMemberToChatroom(token, mChatroomName, invitee);
                    Toast.makeText(context.getApplicationContext(),"Successfully invited to chatroom",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}
