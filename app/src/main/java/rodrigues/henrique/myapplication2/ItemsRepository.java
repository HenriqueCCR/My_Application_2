package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ItemsRepository {
    private static ItemsRepository sItemsRepository;
    private Context mApplicationContext;

    //private LiveData<ArrayList<Item>> mItems;
    private MediatorLiveData<ArrayList<Item>> mItems;
    private LiveData<Item> mSelectedItem;

    private VolleyItemListRetriever mRemoteItemList;

    private ItemsRepository(Context pApplicationContext) {
        this.mApplicationContext = pApplicationContext;
        mItems = new MediatorLiveData<>();
        mRemoteItemList = new VolleyItemListRetriever("https://www.goparker.com/600096/index.json", pApplicationContext);
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
                        //mItems = mutableItems;
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
        String image = pItemObject.getString("image");

        Item item = new Item(title,link,date,description);
        item.setImageUrl(image);

        return item;
    }

    public LiveData<ArrayList<Item>> getItems(){
        LiveData<ArrayList<Item>> remoteData = mRemoteItemList.getItems();
        LiveData<ArrayList<Item>> localData = loadIndexLocally("index.json");
        mItems.addSource(remoteData,value->mItems.setValue(value));
        mItems.addSource(localData,value->mItems.setValue(value));

        return mItems;
    }

    public LiveData<Item> getItem(int pItemIndex) {

        LiveData<Item> transformedItem = Transformations.switchMap(mItems, items -> {
            MutableLiveData<Item> itemData = new MutableLiveData<>();
            Item item = items.get(pItemIndex);
            itemData.setValue(item);
            if(!loadImageLocally(Uri.parse(item.getImageUrl()).getLastPathSegment(), itemData)) {
                loadImage(item.getImageUrl(), itemData);
            }
            return itemData;
        });

        mSelectedItem = transformedItem;
        return mSelectedItem;
    }

    public void loadImage(String pUrl, MutableLiveData<Item> pItemData) {
        RequestQueue queue = Volley.newRequestQueue(mApplicationContext);
        final MutableLiveData<Item> mutableItem = pItemData;

        ImageRequest imageRequest = new ImageRequest(
                pUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                //do something with the image;
                Item item = mutableItem.getValue();
                item.setImage(bitmap);
                saveImageLocally(bitmap, Uri.parse(pUrl).getLastPathSegment());
                mutableItem.setValue(item);
            }
        },
        0,0,
        ImageView.ScaleType.CENTER_CROP,
        Bitmap.Config.RGB_565,
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorResponse = "That didn't work!";
            }
        });
        // Add the request to the RequestQueue.
        queue.add(imageRequest);
    }
    public void saveIndexLocally(JSONObject pIndexObject, String pFilename) {
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(
                    contextWrapper.openFileOutput(pFilename,Context.MODE_PRIVATE));
            outputStreamWriter.write(pIndexObject.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    private LiveData<ArrayList<Item>> loadIndexLocally(String pFilename) {
        JSONObject indexObject = null;
        MutableLiveData<ArrayList<Item>> mutableItems = new MutableLiveData<ArrayList<Item>>();
        try {
            InputStream inputStream = mApplicationContext.openFileInput(pFilename);

            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String builtString = stringBuilder.toString();
                indexObject = new JSONObject(builtString);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("JSONLoading","File not found: " + e.toString());
        }
        catch (IOException e) {
            Log.e("JSONLoading","Can not read file: " + e.toString());
        }
        catch (JSONException e) {
            Log.e("JSONLoading","json error: " + e.toString());
        }
        if(indexObject != null) {
            ArrayList<Item> items = parseJSONResponse(indexObject);
            mutableItems.setValue(items);
        }
        return mutableItems;
    }
    public void saveImageLocally(Bitmap pBitmap, String pFilename) {
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        File directory = contextWrapper.getDir("itemImages",Context.MODE_PRIVATE);
        File file = new File(directory,pFilename);
        if(!file.exists()) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                pBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean loadImageLocally(String pFilename, MutableLiveData<Item> pItemData) {
        boolean loaded = false;
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        File directory = contextWrapper.getDir("itemImages",Context.MODE_PRIVATE);
        File file = new File(directory,pFilename);
        if(file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                Item item = pItemData.getValue();
                item.setImage(bitmap);
                pItemData.setValue(item);

                fileInputStream.close();
                loaded = true;
            }
            catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return loaded;
    }
}
