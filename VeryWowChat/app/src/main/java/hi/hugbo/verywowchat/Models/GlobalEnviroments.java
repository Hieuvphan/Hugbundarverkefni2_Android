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

    private final String BASEURL = "http://130.208.151.76:9090/";

    public String getAPI_BASEURL() {
        return BASEURL;
    }
}
