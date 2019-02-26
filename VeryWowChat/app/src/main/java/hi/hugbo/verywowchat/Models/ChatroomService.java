package hi.hugbo.verywowchat.Models;

import java.util.List;

import hi.hugbo.verywowchat.entities.Chatroom;

public interface ChatroomService {

    /**
     * Create a new chatroom
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
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception;

    /**
     * Update the chatroom with the chatroomName
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
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception;

    /**
     * Get a list of all chatrooms the user is a member of
     * @return list of chatrooms user is member of
     * @throws Exception if unauthorized
     */
    public List<Chatroom> getMyChatrooms() throws Exception;

    /**
     * Search for chatrooms user is not member of
     * @param searchTerm
     * @return list of chatrooms
     * @throws Exception if unauthorized, connection unestablished
     */
    public List<Chatroom> chatroomSearch(String searchTerm) throws Exception;

    /**
     * Send a user an invite to join a chatroom
     * @param chatroomName
     * @param userName
     * @throws Exception if user is already member, current user does not have access
     */
    public void inviteMemberToChatroom(String chatroomName, String userName) throws Exception;

    /**
     * Invite a user to become an admin of a chatroom
     * @param chatroomName
     * @param userName
     * @throws Exception if user is already admin, current user unauthorized to send invites
     */
    public void inviteAdminToChatroom(String chatroomName, String userName) throws Exception;

    /**
     * leave a given chatroom
     * @param userName
     * @throws Exception if not a member, is an owner
     */
    public void leaveChatroom(String userName) throws Exception;

    /**
     * accept an invite to join a chatroom, or join a chatroom if public
     * @param chatroomName
     * @throws Exception if already a member, if no invite and chatroom is private
     */
    public void joinChatroom(String chatroomName) throws Exception;

    /**
     * become an admin of a chatroom if you've been invited
     * @param chatroomName
     * @throws Exception if no admin invite
     */
    public void acceptAdminInvite(String chatroomName) throws Exception;

    /**
     * declines/deletes a received chatroom invite to a chatroom
     * @param chatroomName
     * @throws Exception if no invite to chatroom received
     */
    public void declineChatroomInvite(String chatroomName) throws Exception;

    /**
     * declines/deletes a received chatroom admin invite to a chatroom
     * @param chatroomName
     * @throws Exception if no invite to chatroom received
     */
    public void declineChatroomAdminInvite(String chatroomName) throws Exception;
}
