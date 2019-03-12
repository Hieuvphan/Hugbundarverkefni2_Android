package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.R;

public class ChatroomAdminInviteFragment extends Fragment {

    public ChatroomAdminInviteFragment() {

    }

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private static final String CHATROOM_NAME = "chatroom_name";
    private String mChatroomName;

    EditText edit_chatroom_admin_invitee;
    Button btn_chatroom_admin_invite;

    public static ChatroomAdminInviteFragment newInstance(String chatroomName){
        ChatroomAdminInviteFragment fragment = new ChatroomAdminInviteFragment();

        Bundle args = new Bundle();
        args.putString(CHATROOM_NAME, chatroomName);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_invite_member, container, false);

        edit_chatroom_admin_invitee = rootView.findViewById(R.id.edit_chatroom_admin_invitee);
        btn_chatroom_admin_invite = rootView.findViewById(R.id.btn_chatroom_admin_invite);

        final Context context = rootView.getContext();
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        mChatroomName = getArguments().getString(CHATROOM_NAME);
        btn_chatroom_admin_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String invitee = edit_chatroom_admin_invitee.getText().toString();
                    chatroomService.inviteAdminToChatroom(token, mChatroomName, invitee);
                    Toast.makeText(context.getApplicationContext(),"Administrator invite successfully sent",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}