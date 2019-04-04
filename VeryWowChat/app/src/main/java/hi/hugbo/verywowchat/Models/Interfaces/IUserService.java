package hi.hugbo.verywowchat.Models.Interfaces;

import java.util.List;
import java.util.Map;

import hi.hugbo.verywowchat.entities.User;

public interface IUserService {

    /**
     * <pre>
     *     Usage : UserService.updateUser(requestBody,token)
     *       For : requestBody is a Map<String,String>
     *             token is a string
     *     After : Sends the HTTP PATCH to endpoint/auth/user with the request body and
     *             the users jwt token.
     * </pre>
     * @param requestbody data for updating the user
     * @param token User's JWT
     */
    public void updateUser(Map<String,String> requestbody, String token) throws Exception;

    /**
     * <pre>
     *     Usage : UserService.deleteMe(token)
     *       For : token is a string (users JWT)
     *     After : Sends a HTTP DELETE Request to endpoint/auth/user
     *             to delete the account who is linked to the token.
     * </pre>
     * @param token users json web token
     */
    public void deleteMe(String token) throws Exception;

    /**
     * fetch a list of all users who have sent user a friend request
     * @param token
     * @throws Exception
     */
    public List<User> getFriendRequests(String token) throws Exception;

    /**
     * decline a friend request from user friendUserName
     * @param token
     * @param friendUsername
     * @throws Exception
     */
    public void declineFriendRequest(String token, String friendUsername) throws Exception;

    /**
     * send a friend request to user / accept a friend request from user friendUserName
     * @param token
     * @param friendUsername
     * @throws Exception
     */
    public void addFriend(String token, String friendUsername) throws Exception;

    /**
     * <pre>
     *     Usage : GetFriends(token)
     *       For : token is a string
     *     After : Performs a HTTP GET Request on /auth/user/userName/friends and
     *             parses the resposnse to List<Friends> and returns it.
     * </pre>
     * @param token users json webtoken
     * @return returns a list of users friends
     */
    public List<User> getFriends(String token);

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
    public String RemoveFriend(String friendUserName,String token);
}
