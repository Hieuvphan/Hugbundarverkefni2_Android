package hi.hugbo.verywowchat.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;

/**
 * @Author : Róman
 * Since our server can keep all types of files .txt , .jpeg, .word etc....
 * we need to have a way to store the stream of bites that is the content and parse it correctly
 * on the phone, so the user can open it and stuff.
 * This class holds the byte stream that the server send to us along with the content type
 * that was in the response header and sees to parse it correctly to an appropriate file type.
 * */
public class ResourceContent {

    private InputStream filestream;
    private String file_type;

    public ResourceContent(InputStream file, String type) {
        Log.d("dh", "ResourceContent()");
        filestream = file;
        file_type = type;
    }

    /**
     * <pre>
     *     Usage : ResourceContent.ConvertToBitmap();
     *     For   : Nothing
     *   After   : converts the InputStream of ResourceContent to a Bitmap object
     * </pre>
     * @return A Bitmap img
     */
    public Bitmap ConvertToBitmap(){
        Bitmap bitmap = BitmapFactory.decodeStream(filestream);
        return  bitmap;
    }

    public InputStream getFile() {
        return filestream;
    }

    public String getFile_type() {
        return file_type;
    }
}
