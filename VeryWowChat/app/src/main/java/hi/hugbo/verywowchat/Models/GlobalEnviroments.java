package hi.hugbo.verywowchat.Models;

import java.util.PriorityQueue;

/**
 * @Author Róman
 *
 * This class acts as an .env file (environmental variables) since android compiles everything
 * to bytecode it's no use for them to have a .env file so we create our own class.
 *
 * This is mainly to prevent GODDAMM MERGE CONFLICTS !!!  nothing else.
 * We also want to have a singleton instance of this class.
 *
 * */
public class GlobalEnviroments {
    private static final GlobalEnviroments ourInstance = new GlobalEnviroments();

    public static GlobalEnviroments getInstance() { return ourInstance; }
    private GlobalEnviroments() { }

    /**
     * Set the local network IP address of your computer here!
     *
     * TODO: remember to change this!
     *
     * On macOS, run:
     *
     *   ifconfig | grep "inet" -
     */
    private final String HOST = "130.208.151.175"; // "130.208.151.152"

    // Port of server
    private final int PORT = 9090;


    private final String BASEURL = "http://" + HOST + ":" + PORT + "/";

    public String getAPI_BASEURL() {
        return BASEURL;
    }
}
