package rodrigues.henrique.myapplication2;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyJSONObjectResponse {
    void onResponse(JSONObject pObject, String pTag);
    void onError(VolleyError pError, String pTag);

}
