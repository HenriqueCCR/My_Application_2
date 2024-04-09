package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventStrings {

    public static ArrayList<EventStrings> eventsStringsList = new ArrayList<>();

    public static ArrayList<EventStrings> eventsForDate(LocalDate date){ // Can use this method to return all events from specific date
        ArrayList<EventStrings> events = new ArrayList<>();

        for(EventStrings event : eventsStringsList){
            if(event.getDate().equals(date)){
                events.add(event);
            }
        }
        return events;
    }

    private String name;
    private String date;
    private String time;
    private String visible;


    public EventStrings(String name, String date, String time, String visible) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String timeString) {
        this.time = timeString;
    }
    public String getVisibility() {
        return visible;
    }

    public void setVisibility(String visible) {
        this.visible = visible;
    }
}
