package hi.hugbo.verywowchat.Models;

import java.util.PriorityQueue;

/**
 * @Author Róman
 * This class act's as a .evn file (environmental variables) since android compiles everything
 * to bytecode it's no use for them to have a .env file so we create our own class.
 * This is mainly to prevent GODDAMM MERGE CONFLICTS !!!  nothing else.
 * We also want to have a singleton instance of this class.
 * */
public class GlobalEnviroments {
    private static final GlobalEnviroments ourInstance = new GlobalEnviroments();

    public static GlobalEnviroments getInstance() { return ourInstance; }
    private GlobalEnviroments() { }

    // URL to API
    private final String API_ENDPOINT = "";

    // User Credentials
    private String TOKEN = "";
    private String USERNAME = "";
    private String DISPLAYNAME = "";

    /***
     * <pre>
     *     Usage : GlobalEnviroments.ClearUserInfo()
     *       For : Nothing
     *     After : sets the User Credential fields to empty strings
     * </pre>
     */
    public void ClearUserInfo() {
        setDISPLAYNAME("");
        setTOKEN("");
        setUSERNAME("");
    }

    public String getAPI_ENDPOINT() {
        return API_ENDPOINT;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getDISPLAYNAME() {
        return DISPLAYNAME;
    }

    public void setDISPLAYNAME(String DISPLAYNAME) {
        this.DISPLAYNAME = DISPLAYNAME;
    }
}
