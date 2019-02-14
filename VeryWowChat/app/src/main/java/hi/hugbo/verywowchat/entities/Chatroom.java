package hi.hugbo.verywowchat.entities;

import java.util.List;

/**
 * @author vilhelm
 * @desc A chatroom entity with the following properties:
 *      chatroomName            unique identifyer for chatroom
 *      displayName             descriptive name for the chatroom
 *      description
 *      listed                  boolean: true for private, false for public chatrooms
 *      invited_only            do users need an invite to join
 *      created                 when was the chatroom created
 *      lastMessageReceived     when did the chatroom last receive a message
 *      tags                    list of tags for the chatroom
 */
public class Chatroom {
    // unique name serving as an identifier
    private String chatroomName;
    // non-unique name to be displayed
    private String displayName;
    // description of the chatroom
    private String description;
    // denotes the visibility of the chatroom: true means listed, false means
    // unlisted
    private Boolean listed;
    // denots the accessability of the chatroom: true means users can only join with
    // an invite, false means anyone can join
    private Boolean invited_only;
    // the username of the owner of the chatroom
    private String ownerUsername;
    // when the chatroom was created
    private Long created;
    // timestamp of when the latest message was received
    private Long lastMessageReceived;
    // the chatroom's tags
    private List<String> tags;

    /**
     * @param chatroomName
     * @param displayName
     * @param description
     * @param listed
     * @param invited_only
     * @param lastMessageReceived
     * @param tags
     */
    public Chatroom(String chatroomName, String displayName, String description, Boolean listed,
                    Boolean invited_only, Long lastMessageReceived, List<String> tags) {
        this.chatroomName = chatroomName;
        this.displayName = displayName;
        this.description = description;
        this.listed = listed;
        this.invited_only = invited_only;
        this.lastMessageReceived = lastMessageReceived;
        this.tags = tags;
    }
    // getters

    public String getChatroomName() {
        return chatroomName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getListed() {
        return listed;
    }

    public Boolean getInvited_only() {
        return invited_only;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public Long getCreated() {
        return created;
    }

    public Long getLastMessageReceived() {
        return lastMessageReceived;
    }

    public void setLastMessageReceived(Long lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public List<String> getTags() {
        return tags;
    }

}
