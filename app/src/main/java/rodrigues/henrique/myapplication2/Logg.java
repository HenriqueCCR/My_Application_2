package rodrigues.henrique.myapplication2;

import java.time.LocalDate;
import java.util.ArrayList;

public class Logg {

    public static ArrayList<Logg> loggEventsList = new ArrayList<>();

    public static ArrayList<Logg> logsForDate(LocalDate date){
        ArrayList<Logg> loggs = new ArrayList<>();

        for(Logg logg : loggEventsList){
            if(logg.getDate().equals(date)){
                loggs.add(logg);
            }
        }
        return loggs;
    }

    private String name;
    private LocalDate date;
    private String time;
    private double distance;

    public Logg(String name, double distance, LocalDate date, String time){
        this.name = name;
        this.distance = distance;
        this.date = date;
        this.time = time;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getDistance() { return distance;}
    public void setDistance(double distance) { this.distance = distance;}
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String timeString) { this.time = timeString; }
}
