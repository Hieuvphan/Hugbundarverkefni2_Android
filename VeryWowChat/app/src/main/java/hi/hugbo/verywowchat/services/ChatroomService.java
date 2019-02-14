package hi.hugbo.verywowchat.services;

import java.util.List;

import hi.hugbo.verywowchat.entities.Chatroom;

public interface ChatroomService {
    public Chatroom createChatroom(
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            List<String> tags
    ) throws Exception;
}
