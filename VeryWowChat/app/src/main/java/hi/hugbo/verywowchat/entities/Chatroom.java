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
    String chatroomName;
    String displayName;
    String description;
    Boolean listed;
    Boolean invited_only;
    String ownerUsername;
    Long created;
    Long lastMessageReceived;
    List<String> tags;


    /**
     *
     * @param chatroomName
     * @param displayName
     * @param description
     * @param listed
     * @param invited_only
     * @param ownerUsername
     * @param created
     * @param lastMessageReceived
     * @param tags
     */
    public Chatroom(
            String chatroomName,
            String displayName,
            String description,
            Boolean listed,
            Boolean invited_only,
            String ownerUsername,
            Long created,
            Long lastMessageReceived,
            List<String> tags
    ) {
        this.chatroomName = chatroomName;
        this.displayName = displayName;
        this.description = description;
        this.listed = listed;
        this.invited_only = invited_only;
        this.ownerUsername = ownerUsername;
        this.created = created;
        this.lastMessageReceived = lastMessageReceived;
        this.tags = tags;
    }

    @Override
    public String toString(){

        String tagItems = "";
        for (int i = 0; i < this.tags.size(); i++) {
            tagItems += "        "+this.tags.get(i)+"\n";
        }

        return  "{" +
                "\n    chatroomName: " + this.chatroomName +
                "\n    displayName: " + this.displayName +
                "\n    description: " + this.description +
                "\n    listed: " + this.listed +
                "\n    invited_only: " + this.invited_only +
                "\n    ownerUsername: " + this.ownerUsername +
                "\n    created: " + this.created +
                "\n    lastMessageReceived: " + this.lastMessageReceived +
                "\n    tags: [" +tagItems +
                "\n    ]" +
                "\n}";
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

    public List<String> getTags() {
        return tags;
    }
}
