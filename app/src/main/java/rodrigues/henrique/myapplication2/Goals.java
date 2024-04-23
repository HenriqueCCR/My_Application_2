package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Goals {
    private static final String SHORT_DISTANCE_RUNS = "ShortDistanceRuns";
    private static final String SDR_KEY = "shortRuns";
    private static final String LONG_DISTANCE_RUNS = "LongDistanceRuns";
    private static final String LDR_KEY = "longRuns";

    public static ArrayList<Integer> getShortDistanceRuns(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHORT_DISTANCE_RUNS, Context.MODE_PRIVATE);
        String shortRunJson = sharedPreferences.getString(SDR_KEY, null);

        if (shortRunJson == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type shortRunType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(shortRunJson, shortRunType);
    }
    public static ArrayList<Integer> getLongDistanceRuns(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LONG_DISTANCE_RUNS, Context.MODE_PRIVATE);
        String longRunJson = sharedPreferences.getString(LDR_KEY, null);

        if (longRunJson == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type longRunType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(longRunJson, longRunType);
    }
}