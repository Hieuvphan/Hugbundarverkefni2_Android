package hi.hugbo.verywowchat.entities;

/**
 * A user entity with properties
 *      username: acts as unique identifier for user
 *      displayName: user's visible name
 */
public class User {
    private String username;
    private String displayName;

    public User() {

    }

    public User(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }
}
