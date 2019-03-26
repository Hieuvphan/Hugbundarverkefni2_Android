package hi.hugbo.verywowchat.entities;

public class Friend {

    private String username;
    private String displayName;

    public Friend() {

    }

    public Friend(String name, String disName) {
        username = name;
        displayName = disName;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }
}
