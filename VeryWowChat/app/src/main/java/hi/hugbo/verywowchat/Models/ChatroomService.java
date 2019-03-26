package hi.hugbo.verywowchat.Models;

import java.util.List;

import hi.hugbo.verywowchat.entities.Chatroom;
import hi.hugbo.verywowchat.entities.User;

public interface ChatroomService {

    /**
     * fetch the chatroom with the corresponding chatroom name
     * @param token user authentication JWT token
     * @param chatroomName
     * @return the chatroom
     * @throws Exception if chatroomname is not in use
     */
    public Chatroom getChatroom(String token, String chatroomName) throws Exception;

    /**
     * Create a new chatroom
     * @param token user authentication JWT token
     * @param chatroomName
     * @param displayName
     * @param description
     * @param listed
     * @param invited_only
     * @param tags
     * @return the created chatroom
     * @throws Exception if chatroomName is taken or missing, authentication failed
     */
    public Chatroom createChatroom(
            String token,
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception;

    /**
     * Update the chatroom with the chatroomName
     * @param token user authentication JWT token
     * @param chatroomName
     * @param displayName
     * @param description
     * @param listed
     * @param invited_only
     * @param tags
     * @return the updated chatroom
     * @throws Exception if chatroom does not exist, if unauthorized
     */
    public Chatroom updateChatroom(
            String token,
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception;

    /**
     * Get a list of all chatrooms the user is a member of
     * @param token user authentication JWT token
     * @return list of chatrooms user is member of
     * @throws Exception if unauthorized
     */
    public List<Chatroom> getMyChatrooms(String token) throws Exception;

    /**
     * Search for chatrooms user is not member of
     * @param token user authentication JWT token
     * @param searchTerm
     * @return list of chatrooms
     * @throws Exception if unauthorized, connection unestablished
     */
    public List<Chatroom> chatroomSearch(String token, String searchTerm) throws Exception;

    /**
     * Send a user an invite to join a chatroom
     * @param token user authentication JWT token
     * @param chatroomName
     * @param userName
     * @throws Exception if user is already member, current user does not have access
     */
    public void inviteMemberToChatroom(String token, String chatroomName, String userName) throws Exception;

    /**
     * Invite a user to become an admin of a chatroom
     * @param token user authentication JWT token
     * @param chatroomName
     * @param userName
     * @throws Exception if user is already admin, current user unauthorized to send invites
     */
    public void inviteAdminToChatroom(String token, String chatroomName, String userName) throws Exception;

    /**
     * leave a given chatroom
     * @param token user authentication JWT token
     * @param userName
     * @throws Exception if not a member, is an owner
     */
    public void leaveChatroom(String token, String userName) throws Exception;

    /**
     * accept an invite to join a chatroom, or join a chatroom if public
     * @param token user authentication JWT token
     * @param chatroomName
     * @throws Exception if already a member, if no invite and chatroom is private
     */
    public void joinChatroom(String token, String chatroomName) throws Exception;

    /**
     * become an admin of a chatroom if you've been invited
     * @param token user authentication JWT token
     * @param chatroomName
     * @throws Exception if no admin invite
     */
    public void acceptAdminInvite(String token, String chatroomName) throws Exception;

    /**
     * declines/deletes a received chatroom invite to a chatroom
     * @param token user authentication JWT token
     * @param chatroomName
     * @throws Exception if no invite to chatroom received
     */
    public void declineChatroomInvite(String token, String chatroomName) throws Exception;

    /**
     * declines/deletes a received chatroom admin invite to a chatroom
     * @param token user authentication JWT token
     * @param chatroomName
     * @throws Exception if no invite to chatroom received
     */
    public void declineChatroomAdminInvite(String token, String chatroomName) throws Exception;

    /**
     * fetch a list of all chatroom who have sent user a membership invite
     * @param token
     * @throws Exception
     */
    public List<Chatroom> getChatroomInvites(String token) throws Exception;

    /**
     * fetch a list of all chatroom who have sent user an admin invite
     * @param token
     * @throws Exception
     */
    public List<Chatroom> getChatroomAdminInvites(String token) throws Exception;

    /**
     * fetch a list of all users who have sent user a friend request
     * @param token
     * @throws Exception
     */
    public List<User> getFriendRequests(String token) throws Exception;
}
