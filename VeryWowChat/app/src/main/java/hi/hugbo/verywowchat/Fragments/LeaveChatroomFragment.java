package hi.hugbo.verywowchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.R;

public class LeaveChatroomFragment extends Fragment {

    public LeaveChatroomFragment() {

    }

    private ChatroomService chatroomService = new ChatroomServiceImplementation();
    private static final String CHATROOM_NAME = "chatroom_name";
    private String mChatroomName;

    public static LeaveChatroomFragment newInstance(String chatroomName){
        LeaveChatroomFragment fragment = new LeaveChatroomFragment();

        Bundle args = new Bundle();
        args.putString(CHATROOM_NAME, chatroomName);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leave_chatroom, container, false);

        Button btn_leave_chatroom = rootView.findViewById(R.id.btn_leave_chatroom);

        final Context context = rootView.getContext();
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        mChatroomName = getArguments().getString(CHATROOM_NAME);

        btn_leave_chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    chatroomService.leaveChatroom(token, mChatroomName);
                    Toast.makeText(context.getApplicationContext(),"Successfully left chatroom",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}
