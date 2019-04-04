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

import hi.hugbo.verywowchat.Models.Interfaces.IChatroomService;
import hi.hugbo.verywowchat.Models.Implementations.ChatroomService;
import hi.hugbo.verywowchat.controllers.R;

/**
 * This fragment is an independent component for joining a chatroom
 */
public class JoinChatroomFragment extends Fragment {

    public JoinChatroomFragment() {

    }

    private IChatroomService chatroomService = new ChatroomService();
    private static final String CHATROOM_NAME = "chatroom_name";
    private String mChatroomName;

    /**
     * Create an instance of JoinChatroomFragment
     * @param chatroomName the chatroom that user will attempt to join
     * @return a new instance of JoinChatroomFragment
     */
    public static JoinChatroomFragment newInstance(String chatroomName){
        JoinChatroomFragment fragment = new JoinChatroomFragment();

        Bundle args = new Bundle();
        args.putString(CHATROOM_NAME, chatroomName);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_join_chatroom, container, false);

        // fetch the widgets
        Button btn_join_chatroom = rootView.findViewById(R.id.btn_join_chatroom);

        // fetch the user token
        final Context context = rootView.getContext();
        SharedPreferences userInfo = context.getApplicationContext().getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        final String token = userInfo.getString("token","n/a");

        // fetch the chatroom name
        mChatroomName = getArguments().getString(CHATROOM_NAME);
        // when button is pressed, attempt to join the given chatroom
        btn_join_chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    chatroomService.joinChatroom(token, mChatroomName);
                    Toast.makeText(context.getApplicationContext(),"Successfully joined chatroom",Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}
