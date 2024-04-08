package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Event {

    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date){
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList){
            if(event.getDate().equals(date)){
                events.add(event);
            }
        }
        return events;
    }

    private String name;
    private LocalDate date;
    private String time;
    private boolean visible;


    public Event(String name, LocalDate date, String time, boolean visible) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String timeString) {
        this.time = timeString;
    }
    public boolean getVisibility() {
        return visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }
}
