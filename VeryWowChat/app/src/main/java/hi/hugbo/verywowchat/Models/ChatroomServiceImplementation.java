package hi.hugbo.verywowchat.Models;

import android.support.annotation.NonNull;
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
import hi.hugbo.verywowchat.entities.User;

public class ChatroomServiceImplementation implements ChatroomService {


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

    public Chatroom getChatroom(String token, String chatroomName) throws Exception{
        String path = "auth/chatroom/"+chatroomName;
        String method = "GET";

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

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
                        resp_body.getLong("lastRead"),
                        tagList,
                        resp_body.getString("userRelation")
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
            String token,
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception {
        String path = "auth/chatroom/";
        String method = "POST";
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
                        resp_body.getLong("lastRead"),
                        tagList,
                        resp_body.getString("userRelation")
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
            String token,
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception {
        String path = "auth/chatroom/"+chatroomName;
        String method = "PATCH";
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
                        resp_body.getLong("lastRead"),
                        tagList,
                        resp_body.getString("userRelation")
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

    public List<Chatroom> getMyChatrooms(String token) throws Exception{
        String path = "auth/user/me/memberofchatrooms";
        String method = "GET";

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

            int status = Integer.parseInt(result.get("status"));

            if(status == 200){
                JSONArray resp_body = new JSONArray(result.get("response"));

                List<Chatroom> chatrooms = new ArrayList<>();
                for(int i = 0; i < resp_body.length(); i++) {
                    JSONObject c = resp_body.getJSONObject(i);

                    // compile the tags into a list
                    JSONArray jTags = c.getJSONArray("tags");
                    List<String> tagList = new ArrayList<String>();
                    for(int j = 0; j < jTags.length(); j++){
                        tagList.add(jTags.getString(j));
                    }

                    chatrooms.add(new Chatroom(
                            c.getString("chatroomName"),
                            c.getString("displayName"),
                            c.getString("description"),
                            c.getBoolean("listed"),
                            c.getBoolean("invited_only"),
                            c.getString("ownerUsername"),
                            c.getLong("created"),
                            c.getLong("lastMessageReceived"),
                            c.getLong("lastRead"),
                            tagList,
                            c.getString("userRelation")
                    ));
                }
                return chatrooms;
            }
            if(status >= 400 && status < 500){
                JSONObject resp_body = new JSONObject(result.get("response"));
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        }catch (Exception e) {
            e.printStackTrace();
            // TODO: why don't you just throw the exception `e`?
            throw new Exception(e.getMessage());
        }
    }

    public List<Chatroom> chatroomSearch(String token, @NonNull String searchTerm) throws Exception{
        String path = "auth/chatroom/search/listed/"+searchTerm;
        String method = "GET";

        if(searchTerm.length() == 0){
            throw new Exception("No search string");
        }

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

            int status = Integer.parseInt(result.get("status"));

            if(status == 200){
                JSONArray resp_body = new JSONArray(result.get("response"));

                List<Chatroom> chatrooms = new ArrayList<>();
                for(int i = 0; i < resp_body.length(); i++) {
                    JSONObject c = resp_body.getJSONObject(i);

                    // compile the tags into a list
                    JSONArray jTags = c.getJSONArray("tags");
                    List<String> tagList = new ArrayList<String>();
                    for(int j = 0; j < jTags.length(); j++){
                        tagList.add(jTags.getString(j));
                    }

                    chatrooms.add(new Chatroom(
                            c.getString("chatroomName"),
                            c.getString("displayName"),
                            c.getString("description"),
                            c.getBoolean("listed"),
                            c.getBoolean("invited_only"),
                            c.getString("ownerUsername"),
                            c.getLong("created"),
                            c.getLong("lastMessageReceived"),
                            c.getLong("lastRead"),
                            tagList,
                            c.getString("userRelation")
                    ));
                }
                return chatrooms;
            }
            if(status >= 400 && status < 500){
                JSONObject resp_body = new JSONObject(result.get("response"));
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void inviteMemberToChatroom(String token, String chatroomName, String userName) throws Exception{
        Log.d("invitee", "invitee: "+userName);
        String path = "auth/chatroom/"+chatroomName+"/invite/"+userName;
        String method = "POST";
        Map<String, Object> body = new ArrayMap<>();

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, body);

            int status = Integer.parseInt(result.get("status"));
            Log.d("invitee", "status: "+status);

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

    public void inviteAdminToChatroom(String token, String chatroomName, String userName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/admininvite/"+userName;
        String method = "POST";
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

    public void leaveChatroom(String token, String chatroomName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/leave/";
        String method = "DELETE";

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

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

    public void joinChatroom(String token, String chatroomName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/join/";
        String method = "POST";
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

    public void acceptAdminInvite(String token, String chatroomName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/acceptadmininvite/";
        String method = "POST";
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

    public void declineChatroomInvite(String token, String chatroomName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/rejectchatroominvite/";
        String method = "DELETE";

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

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

    public void declineChatroomAdminInvite(String token, String chatroomName) throws Exception{
        String path = "auth/chatroom/"+chatroomName+"/rejectadmininvite/";
        String method = "DELETE";

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

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

    public List<Chatroom> getChatroomInvites(String token) throws Exception{
        String path = "auth/user/me/chatroominvites";
        String method = "GET";

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

            int status = Integer.parseInt(result.get("status"));

            if(status == 200){
                JSONArray resp_body = new JSONArray(result.get("response"));

                List<Chatroom> chatrooms = new ArrayList<>();
                for(int i = 0; i < resp_body.length(); i++) {
                    JSONObject c = resp_body.getJSONObject(i);

                    // compile the tags into a list
                    JSONArray jTags = c.getJSONArray("tags");
                    List<String> tagList = new ArrayList<String>();
                    for(int j = 0; j < jTags.length(); j++){
                        tagList.add(jTags.getString(j));
                    }

                    chatrooms.add(new Chatroom(
                            c.getString("chatroomName"),
                            c.getString("displayName"),
                            c.getString("description"),
                            c.getBoolean("listed"),
                            c.getBoolean("invited_only"),
                            c.getString("ownerUsername"),
                            c.getLong("created"),
                            c.getLong("lastMessageReceived"),
                            null,
                            tagList,
                            null
                            ));
                }
                return chatrooms;
            }
            if(status >= 400 && status < 500){
                JSONObject resp_body = new JSONObject(result.get("response"));
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public List<Chatroom> getChatroomAdminInvites(String token) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<User> getFriendRequests(String token) throws Exception{
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
