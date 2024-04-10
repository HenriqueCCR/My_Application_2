package rodrigues.henrique.myapplication2;

import java.time.LocalDate;
import java.util.ArrayList;

public class Log {

    public static ArrayList<Log> logEventsList = new ArrayList<>();

    public static ArrayList<Log> logsForDate(LocalDate date){
        ArrayList<Log> logs = new ArrayList<>();

        for(Log log : logEventsList){
            if(log.getDate().equals(date)){
                logs.add(log);
            }
        }
        return logs;
    }

    private String name;
    private LocalDate date;
    private String time;
    private int distance;
    public Log(String name, int distance, LocalDate date, String time){
        this.name = name;
        this.distance = distance;
        this.date = date;
        this.time = time;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDistance() { return distance;}
    public void setDistance(int distance) { this.distance = distance;}
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String timeString) { this.time = timeString; }
}
