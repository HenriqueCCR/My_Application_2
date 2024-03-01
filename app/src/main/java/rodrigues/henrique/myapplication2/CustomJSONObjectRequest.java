package rodrigues.henrique.myapplication2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class CustomJSONObjectRequest implements Response.Listener<JSONObject>, Response.ErrorListener {
    private VolleyJSONObjectResponse mVolleyJSONObjectResponse;
    private String mTag;
    private JsonObjectRequest mJsonObjectRequest;

    public JsonObjectRequest getJsonObjectRequest(){
        return mJsonObjectRequest;
    }

    public CustomJSONObjectRequest(int pMethod, String pUrl, JSONObject pJsonObject, String pTag, VolleyJSONObjectResponse pVolleyObjectResponse){
        this.mVolleyJSONObjectResponse = pVolleyObjectResponse;
        this.mTag = pTag;                                                                                    //Could send tag to index picture you're requesting - Specific use case but allows for other things too
        mJsonObjectRequest = new JsonObjectRequest(pMethod, pUrl, pJsonObject, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError pError) {
        mVolleyJSONObjectResponse.onError(pError, mTag);
    }

    @Override
    public void onResponse(JSONObject pResponse) {
        mVolleyJSONObjectResponse.onResponse(pResponse, mTag);
    }
}
