package rodrigues.henrique.myapplication2;

import java.time.LocalDate;
import java.util.ArrayList;

public class LogStrings {

    public static ArrayList<LogStrings> logStringsList = new ArrayList<>();

    public static ArrayList<LogStrings> logsForDate(LocalDate date){
        ArrayList<LogStrings> logs = new ArrayList<>();

        for(LogStrings log : logStringsList){
            if(log.getDate().equals(date)){
                logs.add(log);
            }
        }
        return logs;
    }

    private String name;
    private String date;
    private String time;
    private String distance;
    public LogStrings(String name, String distance, String date, String time){
        this.name = name;
        this.distance = distance;
        this.date = date;
        this.time = time;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDistance() { return distance;}
    public void setDistance(String distance) { this.distance = distance;}
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String timeString) { this.time = timeString; }
}
