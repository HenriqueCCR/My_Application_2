package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EventStrings {

    public static ArrayList<EventStrings> eventsStringsList = new ArrayList<EventStrings>();

    public static ArrayList<Event> eventsForDate(ArrayList<EventStrings> storedEvents, LocalDate localDate){
        ArrayList<Event> events = new ArrayList<>();

        for(int i = 0; i < storedEvents.size(); i++) {
            if(LocalDate.parse(storedEvents.get(i).getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")).equals(localDate)){
                Event newEvent = new Event(storedEvents.get(i).getName(), LocalDate.parse(storedEvents.get(i).getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")), storedEvents.get(i).getTime(), Boolean.parseBoolean(storedEvents.get(i).getVisibility()));
                events.add(newEvent);
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
