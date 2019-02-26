package hi.hugbo.verywowchat.Models;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hi.hugbo.verywowchat.entities.Chatroom;

public class ChatroomServiceImplementation implements ChatroomService {

    // pæling: láta token vera instance variable?

    private API_caller api_caller = API_caller.getInstance();

    private Map<String, Object> createChatroomBody(
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("chatroomName", chatroomName);
        body.put("displayName", displayName);
        body.put("description", description);
        body.put("listed", listed);
        body.put("invited_only", invited_only);

        JSONArray tagArray = new JSONArray();
        for(int i = 0; i < tags.size(); i++){
            tagArray.put(tags.get(i));
        }
        body.put("tags", tagArray);

        return body;
    }

    /**
     * Use VeryWowChat API to create a chatroom
     * @param chatroomName
     * @param displayName
     * @param description
     * @param listed
     * @param invited_only
     * @param tags
     * @return created chatroom
     * @throws Exception if name taken, invalid token...
     */
    public Chatroom createChatroom(
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception {
        String path = "auth/chatroom/";
        String method = "POST";
        String token = this.getToken();
        Map<String, Object> body = this.createChatroomBody(chatroomName, displayName, description, listed, invited_only, tags);

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, body);

            int status = Integer.parseInt(result.get("status"));
            JSONObject resp_body = new JSONObject(result.get("response"));

            if(status == 201){
                JSONArray jTags = resp_body.getJSONArray("tags");
                List<String> tagList = new ArrayList<String>();
                for(int i = 0; i < jTags.length(); i++){
                    tagList.add(jTags.getString(i));
                }

                Chatroom newChatroom = new Chatroom(
                        resp_body.getString("chatroomName"),
                        resp_body.getString("displayName"),
                        resp_body.getString("description"),
                        resp_body.getBoolean("listed"),
                        resp_body.getBoolean("invited_only"),
                        resp_body.getString("ownerUsername"),
                        resp_body.getLong("created"),
                        resp_body.getLong("lastMessageReceived"),
                        tagList
                );
                return newChatroom;
            }
            if(status >= 400 && status < 500){
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Chatroom updateChatroom(
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception {
        String path = "auth/chatroom/"+chatroomName;
        String method = "PATCH";
        String token = this.getToken();
        Map<String, Object> body = this.createChatroomBody(chatroomName, displayName, description, listed, invited_only, tags);

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, body);

            int status = Integer.parseInt(result.get("status"));
            JSONObject resp_body = new JSONObject(result.get("response"));

            if(status == 200){
                JSONArray jTags = resp_body.getJSONArray("tags");
                List<String> tagList = new ArrayList<String>();
                for(int i = 0; i < jTags.length(); i++){
                    tagList.add(jTags.getString(i));
                }

                Chatroom newChatroom = new Chatroom(
                        resp_body.getString("chatroomName"),
                        resp_body.getString("displayName"),
                        resp_body.getString("description"),
                        resp_body.getBoolean("listed"),
                        resp_body.getBoolean("invited_only"),
                        resp_body.getString("ownerUsername"),
                        resp_body.getLong("created"),
                        resp_body.getLong("lastMessageReceived"),
                        tagList
                );
                return newChatroom;
            }
            if(status >= 400 && status < 500){
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public List<Chatroom> getMyChatrooms() throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Chatroom> chatroomSearch(String searchTerm) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void inviteMemberToChatroom(String chatroomName, String userName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/invite/"+userName;
        String method = "POST";
        String token = this.getToken();
        Map<String, Object> body = new ArrayMap<>();

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, body);

            int status = Integer.parseInt(result.get("status"));

            if(status == 204){
                return; // success
            }
            if(status >= 400 && status < 500){
                JSONObject resp_body = new JSONObject(result.get("response")); // only need body if error
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void inviteAdminToChatroom(String chatroomName, String userName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/admininvite/"+userName;
        String method = "POST";
        String token = this.getToken();
        Map<String, Object> body = new ArrayMap<>();

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, body);

            int status = Integer.parseInt(result.get("status"));

            if(status == 204){
                return; // success
            }
            if(status >= 400 && status < 500){
                JSONObject resp_body = new JSONObject(result.get("response")); // only need body if error
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void leaveChatroom(String userName) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void joinChatroom(String chatroomName) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void acceptAdminInvite(String chatroomName) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void declineChatroomInvite(String chatroomName) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void declineChatroomAdminInvite(String chatroomName) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    protected String getToken(){
        return "Token eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2aWxoZWxtbCJ9.AZeGQJojwAO_p9IXHw8jHRtfgh8GCtQO1HIkolPL70Ghp91vplSgEHymeMOxsp_WIt_H3S9AF9yGozugJKXaZg";
    }
}
