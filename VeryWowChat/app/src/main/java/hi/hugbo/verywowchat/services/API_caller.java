package hi.hugbo.verywowchat.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author Róman
 * This class is responsible for performing HTTP requests to the VeryWowChat API
 * and returning the request back to the caller functions.
 * There should only be one instance of this class and no more.
 * */
public class API_caller {
    /**
     * We want want to ensure that there will be only a single instance of this class
     * Mainly to prevent resource overuse since on phones resources are pretty tight
     * */
    private static final API_caller ourInstance = new API_caller();

    /**
     * We use the okHttp libary to make all of our HTTP Requests
     * Example i like : https://guides.codepath.com/android/Using-OkHttp
     * */
    private final OkHttpClient client = new OkHttpClient();
    private final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");

    /**
     * This is used to create a singleton instance of this class, also the empty class instantiator.
     * */
    public static API_caller getInstance() {
        return ourInstance;
    }
    private API_caller() {}

    /* Usage : HttpRequest(urlEndPoint,method,token,body)
    *    FOR : urlEndPoint is a string (Cannot be empty/null)
    *          method is a string      (Cannot be empty/null)
    *          token is a string       (can be null)
    *          body is a  Map          (can be null)
    *   After: Performs a synchronous HTTP (method) request one BASEURL/urlEndPoint and passes along the optional
    *          token in the header under "Authorization" and the body in the RequestBody as "application/JSON UTF 8*/
    public Map<String, String> HttpRequest(String urlEndPoint,String method,String token, Map body) throws IOException, JSONException {

        /* If the body was passed then we create a request body
        *  to send it with the HTTP request */
        RequestBody Rbody = null;
        if(body != null) {
            JSONObject parameter = new JSONObject(body);
            Rbody = RequestBody.create(JSON, parameter.toString());
        }

        /* if the token is null then we set it into a empty string */
        if(token == null) {
            token = "";
        }

        /* Create the HTTP Request
        *  We always send the empty token and body its up to the server to decide if it
        *  wants to process those things */
        Request request = new Request.Builder()
                .url("http://130.208.151.138:9090/"+urlEndPoint)
                .method(method,Rbody)
                .header("Authorization", token)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        // Create a Call object and dispatch the network request synchronously
        Response response = client.newCall(request).execute();

        /* Since we dont want to pass down the whole response we create our own object
        *  that will hold 2 things 1: status code of the response 2: the response body in json string form */
        Map<String, String>  resp = new HashMap<String, String>();
        JSONObject WrappedData = new JSONObject(response.body().string());

        // add the status code
        resp.put("status", String.valueOf(response.code()));

        // if the status code is 204 then we know there is no content
        if(response.code() == 204) {
            resp.put("response",null);
            return resp;
        }

        /* The server can throw some random stuff at us and we want to know what happens if it does */
        if(WrappedData.has("message")){
            resp.put("response",null);
            return resp;
        }

        // if the status code is 200-2xx then its a success
        if(response.code() >= 200 && response.code() < 300 ) {
            resp.put("response",WrappedData.getJSONObject("GoodResp").toString());
        }
        if(response.code() >= 400 && response.code() < 500 ) {
            resp.put("response",WrappedData.getJSONObject("BadResp").toString());
        }

        return resp;
    }

}
