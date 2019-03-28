package hi.hugbo.verywowchat.Models.Interfaces;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import hi.hugbo.verywowchat.entities.ChatMessage;
import hi.hugbo.verywowchat.entities.Chatroom;

public interface IChatRoomMessageService {

    void SendChatMessage(String chatID,String token, Map body) throws Exception;
    int GetCountChatLogs(String chatID,String token) throws Exception;
    List<ChatMessage> GetChatLogs(String chatId, String token, int offset, String LoggeInUser, int lastTimeStamp) throws Exception;
    Chatroom UpdateChat(String chatId, String token);
    void NotifyRead(String chatID,String token) throws IOException, JSONException;
}
