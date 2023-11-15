package rodrigues.henrique.myapplication2;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsRepository {
    private static ItemsRepository sItemsRepository;
    private Context mApplicationContext;

    private LiveData<ArrayList<Item>> mItems;
    private LiveData<Item> mSelectedItem;

    private ItemsRepository(Context pApplicationContext) {
        this.mApplicationContext = pApplicationContext;
    }

    public static ItemsRepository getInstance(Context pApplicationContext) {
        if (sItemsRepository == null) {
            sItemsRepository = new ItemsRepository(pApplicationContext);
        }
        return sItemsRepository;
    }

    public LiveData<ArrayList<Item>> loadItemsFromJSON(){
        RequestQueue queue = Volley.newRequestQueue(mApplicationContext);
        String url = "https://www.goparker.com/600096/index.json";
        final MutableLiveData<ArrayList<Item>> mutableItems = new MutableLiveData<>(); // Live data can't be edited but it can be observed

        //Request a jsonObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ArrayList<Item> items = parseJSONResponse(response);
                        mutableItems.setValue(items);
                        mItems = mutableItems;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorResponse = "That didn't work";
                    }
                }
        );
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        return mutableItems;
    }

    private ArrayList<Item> parseJSONResponse(JSONObject pResponse) {
        ArrayList<Item> items = new ArrayList();
        try {
            JSONArray itemsArray = pResponse.getJSONArray("items");
            for (int i=0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                Item item = parseJSONItem(itemObject);
                items.add(item);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
    /**Above and below are 2 methods that take a JSONObject that contains a JSONArray which contains
     * JSONObjects which contain data required to construct ItemObject then used into a list of items
     * then return up the chain again to do stuff with the data later */
    private Item parseJSONItem(JSONObject pItemObject) throws org.json.JSONException {
        String title = pItemObject.getString("title");
        String link = pItemObject.getString("link");
        String date = pItemObject.getString("pubDate");
        String description = pItemObject.getString("description");

        Item item = new Item(title,link,date,description);

        return item;
    }

    public LiveData<ArrayList<Item>> getItems(){
        if(mItems == null) {
            mItems = loadItemsFromJSON();
        }
        return mItems;
    }

    public LiveData<Item> getItem(int pItemIndex) {

        LiveData<Item> transformedItem = Transformations.switchMap(mItems, items -> {
            MutableLiveData<Item> itemData = new MutableLiveData<>();
            Item item = items.get(pItemIndex);
            itemData.setValue(item);

            return itemData;
        });

        mSelectedItem = transformedItem;
        return mSelectedItem;
    }
}
