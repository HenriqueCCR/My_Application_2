package rodrigues.henrique.myapplication2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LogStrings {

    public static ArrayList<Logg> logsForDate(ArrayList<LogStrings> storedLogs, LocalDate localDate){
        ArrayList<Logg> logs = new ArrayList<>();

        for(int i = 0; i < storedLogs.size(); i++) {
            if(LocalDate.parse(storedLogs.get(i).getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")).equals(localDate)){
                Logg newLog = new Logg(storedLogs.get(i).getName(), Double.parseDouble(storedLogs.get(i).getDistance()), LocalDate.parse(storedLogs.get(i).getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")), storedLogs.get(i).getTime());
                logs.add(newLog);
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
