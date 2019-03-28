package hi.hugbo.verywowchat.Models;

import android.support.v4.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hi.hugbo.verywowchat.Models.Helpers.API_caller;
import hi.hugbo.verywowchat.entities.User;

/**
 * @Author Róman
 * This service is responsible for making HTTP requests that have anything
 * to do with updating or deleting the user.
 * We also want this service to a singleton.
 * */
public class UserService {

    private static final UserService ourInstance = new UserService();
    private API_caller api_caller = API_caller.getInstance(); // need for making HTTP calls

    // need this for making singletons
    public static UserService getInstance() {
        return ourInstance;
    }
    private UserService() {
    }

    /**
     * <pre>
     *     Usage : UserService.UpdateUser(requestBody,token)
     *       For : requestBody is a Map<String,String>
     *             token is a string
     *     After : Sends the HTTP PATCH to endpoint/auth/user with the request body and
     *             the users jwt token.
     * </pre>
     * @param requestbody data for updating the user
     * @param token User's JWT
     */
    public void UpdateUser(Map<String,String> requestbody,String token) throws Exception {
        // Make the HTTP Request
        Map<String,String> result = api_caller.HttpRequest("auth/user/","PATCH",token,requestbody);
        // Parse the HTTP status code
        int status = Integer.parseInt(result.get("status"));
        // if the result is anything but 200 that means we have an error
        if(status != 200) {
            JSONObject errorMsg = new JSONObject(result.get("response"));
            throw new Exception(errorMsg.get("error").toString());
        }
    }

    /**
     * <pre>
     *     Usage : UserService.DeleteMe(token)
     *       For : token is a string (users JWT)
     *     After : Sends a HTTP DELETE Request to endpoint/auth/user
     *             to delete the account who is linked to the token.
     * </pre>
     * @param token users json web token
     */
    public void DeleteMe(String token) throws Exception {

        // Make the HTTP Request
        Map<String, String> result = api_caller.HttpRequest("auth/user/", "DELETE", token, null);
        // Parse the HTTP status code
        int status = Integer.parseInt(result.get("status"));
        // if the result is anything but 204 that means we have an error
        if(status != 204) {
           JSONObject errorMsg = new JSONObject(result.get("response"));
           throw new Exception(errorMsg.get("error").toString());
        }
    }

    /**
     * fetch a list of all users who have sent user a friend request
     * @param token
     * @throws Exception
     */
    public List<User> getFriendRequests(String token) throws Exception{
        String path = "auth/user/me/friendrequestors";
        String method = "GET";

        try{
            Map<String, String> result = api_caller.HttpRequest(path, method, token, null);

            int status = Integer.parseInt(result.get("status"));

            if(status == 200){
                JSONArray resp_body = new JSONArray(result.get("response"));

                List<User> users = new ArrayList<>();
                for(int i = 0; i < resp_body.length(); i++) {
                    JSONObject c = resp_body.getJSONObject(i);

                    users.add(new User(
                            c.getString("username"),
                            c.getString("displayName")
                    ));
                }
                return users;
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

    /**
     * decline a friend request from user friendUserName
     * @param token
     * @param friendUsername
     * @throws Exception
     */
    public void declineFriendRequest(String token, String friendUsername) throws Exception{
        String path = "auth/user/friendRequest/"+friendUsername;
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

    /**
     * send a friend request to user / accept a friend request from user friendUserName
     * @param token
     * @param friendUsername
     * @throws Exception
     */
    public void addFriend(String token, String friendUsername) throws Exception {
        String path = "auth/user/friends/" + friendUsername;
        String method = "POST";
        Map<String, Object> body = new ArrayMap<>();

        try {
            Map<String, String> result = api_caller.HttpRequest(path, method, token, body);

            int status = Integer.parseInt(result.get("status"));

            if (status == 204) {
                return; // success
            }
            if (status >= 400 && status < 500) {
                JSONObject resp_body = new JSONObject(result.get("response")); // only need body if error
                throw new Exception(resp_body.getString("error"));
            }

            throw new Exception("Something unexpected happened");

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    /**
     * <pre>
     *     Usage : GetFriends(userName,token)
     *       For : username is a string
     *             token is a string
     *     After : Performs a HTTP GET Request on /auth/user/userName/friends and
     *             parses the resposnse to List<Friends> and returns it.
     * </pre>
     * @param userName users username
     * @param token users json webtoken
     * @return returns a list of users friends
     */
    public List<User> getFriends(String userName, String token){
        List<User> friends = new ArrayList<>();
        try {
            Map<String,String> friends_respsonse = api_caller.HttpRequest("auth/user/"+userName+"/friends","GET",token,null);
            // Parse the HTTP status code
            int status = Integer.parseInt(friends_respsonse.get("status"));
            // if the status code is anything but 200 we return null
            if(status != 200) { return null; }
            JSONArray friends_json = new JSONArray(friends_respsonse.get("response"));

            for(int i = 0; i < friends_json.length(); i++) {
                friends.add(new User(
                        friends_json.getJSONObject(i).getString("username"),
                        friends_json.getJSONObject(i).getString("displayName")
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return friends;
    }

    /**
     * <pre>
     *    Usage : RemoveFriend(friendUserName, token)
     *     For  : friendUserName is a string
     *            token is string
     *    After : Performs a HTTP DELETE Request on /auth/user/friends/friendUserName
     *            and returns null if the Request was sucessfull
     * </pre>
     * @param friendUserName username of the friend we want to delete.
     * @param token users json web token.
     */
    public String RemoveFriend(String friendUserName,String token){
        String removed = null;
        try {
            Map<String,String> removeFriend = api_caller.HttpRequest("auth/user/friends/"+friendUserName,"DELETE",token,null);
            // Parse the HTTP status code
            int status = Integer.parseInt(removeFriend.get("status"));
            // if the status code is anything but 204 we return null
            if(status == 204) { return "Your friend has been removed."; }
            // if we made it here we received an error
            JSONObject errorJson = new JSONObject(removeFriend.get("response"));
            if(errorJson.has("error")){
                removed = errorJson.getString("error");
            }
            else {
                removed = "unknown error occured !";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return removed;
    }
}
