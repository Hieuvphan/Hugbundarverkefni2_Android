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
    Long lastRead;
    List<String> tags;
    String userRelation;
    Boolean iAmRead;

    /**
     * @param chatroomName
     * @param displayName
     * @param description
     * @param listed
     * @param invited_only
     * @param ownerUsername
     * @param created
     * @param lastMessageReceived
     * @param lastRead
     * @param tags
     * @param userRelation
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
            Long lastRead,
            List<String> tags,
            String userRelation
    ) {
        this.chatroomName = chatroomName;
        this.displayName = displayName;
        this.description = description;
        this.listed = listed;
        this.invited_only = invited_only;
        this.ownerUsername = ownerUsername;
        this.created = created;
        this.lastMessageReceived = lastMessageReceived;
        this.lastRead = lastRead;
        this.tags = tags;
        this.userRelation = userRelation;
        this.iAmRead = false;
    }

    public void Update(Chatroom updatedChat) {
       //setChatroomName(updatedChat.getChatroomName());
       //setDisplayName(updatedChat.getDisplayName());
       //setListed(updatedChat.getListed());
       //setInvited_only(updatedChat.getInvited_only());
       //setOwnerUsername(updatedChat.getOwnerUsername());
       //setCreated(updatedChat.getCreated());
       setLastMessageReceived(updatedChat.getLastMessageReceived());
       setLastRead(updatedChat.getLastRead());
       //setTags(updatedChat.getTags());
       //setUserRelation(updatedChat.getUserRelation());
       this.iAmRead = true;
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
                "\n    lastRead: " + this.lastRead +
                "\n    userRelation: " + this.userRelation +
                "\n    tags: [" + tagItems +
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

    public String getUserRelation() {
        return userRelation;
    }

    public List<String> getTags() {
        return tags;
    }

    public Long getLastRead() {
        return lastRead;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setListed(Boolean listed) {
        this.listed = listed;
    }

    public void setInvited_only(Boolean invited_only) {
        this.invited_only = invited_only;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public void setLastMessageReceived(Long lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public void setLastRead(Long lastRead) {
        this.lastRead = lastRead;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setUserRelation(String userRelation) {
        this.userRelation = userRelation;
    }

    public Boolean getiAmRead() {
        return iAmRead;
    }

    public String getTagString(){
        if(tags.isEmpty()){
            return "";
        }
        String tagString = "";
        for(String tag : tags){
            tagString += tag+", ";
        }
        tagString = tagString.substring(0, tagString.length() -2);

        return tagString;
    }
}
