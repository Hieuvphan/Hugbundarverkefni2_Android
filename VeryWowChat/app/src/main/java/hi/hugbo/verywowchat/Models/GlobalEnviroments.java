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

    // TODO: this is a nice hack
    // TODO: Remember to change this!
    // TODO: If VeryWowChat server is not running at this HOST, then a corresponding error
    // should be brought up to the user, e.g. `Cannot connect to server.`.
    private final String HOST = "130.208.151.153"; // "130.208.151.152"
    private final int PORT = 9090;


    private final String BASEURL = "http:/" + HOST + ":" + PORT + "/";

    public String getAPI_BASEURL() {
        return BASEURL;
    }
}
