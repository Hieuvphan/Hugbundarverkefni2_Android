package hi.hugbo.verywowchat.Models;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hi.hugbo.verywowchat.entities.Chatroom;

public class ChatroomServiceImplementation implements ChatroomService {

    private API_caller api_caller = API_caller.getInstance();

    public Chatroom createChatroom(String chatroomName, String displayName, String description, Boolean listed, Boolean invited_only) {

        Log.d("createChatroom", "in craete function");

        String path = "auth/chatroom/";
        String method = "POST";
        String token = this.getToken();

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("chatroomName", chatroomName);
        body.put("displayName", displayName);
        body.put("description", description);
        body.put("listed", listed);
        body.put("invited_only", invited_only);

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, body);

            int status = Integer.parseInt(result.get("status"));
            Log.d("createChatroom", "status: "+status);
            JSONObject resp_body = new JSONObject(result.get("response"));
            Log.d("createChatroom", "body: "+resp_body.toString());
        }catch (Exception e) {
            Log.e("createChatroom", "http req error: "+e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    protected String getToken(){
        return "Token eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2aWxoZWxtbCJ9.AZeGQJojwAO_p9IXHw8jHRtfgh8GCtQO1HIkolPL70Ghp91vplSgEHymeMOxsp_WIt_H3S9AF9yGozugJKXaZg";
    }
}
