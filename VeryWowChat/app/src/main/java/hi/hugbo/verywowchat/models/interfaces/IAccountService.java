package hi.hugbo.verywowchat.models.interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public interface IAccountService {
    String Login(Map<String, String> params, Context context, final SharedPreferences UserInfo);
    String Register (Map<String, String> params);
}
