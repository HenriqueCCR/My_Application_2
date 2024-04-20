package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarUtils {
    public static LocalDate selectedDate;

    private static final String SHARED_PREF_EVENTS = "MyEvents";
    private static final String EVENTS_KEY = "events";
    private static final String SHARED_PREF_LOGS = "MyLogs";
    private static final String LOGS_KEY = "logs";

    public static ArrayList<EventStrings> getStoredEvents(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_EVENTS, Context.MODE_PRIVATE);
        String eventsJson = sharedPreferences.getString(EVENTS_KEY, null);

        // If no data is stored - return empty list
        if (eventsJson == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type eventType = new TypeToken<List<EventStrings>>() {}.getType();
        return gson.fromJson(eventsJson, eventType);
    }
    public static void removeEvent(Context context, Event eventToRemove) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_EVENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Convert Logg to loggStrings for JSON
        EventStrings eventToRemoveConverted = new EventStrings(eventToRemove.getName(),
                                                    CalendarUtils.formattedDated(eventToRemove.getDate()),
                                                    eventToRemove.getTime(),
                                                    String.valueOf(eventToRemove.getVisibility()));

        // Get Loggs
        ArrayList<EventStrings> storedEvents = getStoredEvents(context);

        // Delete log
        for(int i = 0; i < storedEvents.size(); i++) {
            if (storedEvents.get(i).getDate().equals(eventToRemoveConverted.getDate()) && storedEvents.get(i).getName().equals(eventToRemoveConverted.getName())) {
                storedEvents.remove(i);
            }
        }

        // Convert updated list to JSON
        Gson gson = new Gson();
        String updatedEventsJson = gson.toJson(storedEvents);

        // Save updated lst back to SharedPreferences
        editor.putString(EVENTS_KEY, updatedEventsJson);
        editor.apply();
    }

    public static ArrayList<LogStrings> getStoredLogs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_LOGS, Context.MODE_PRIVATE);
        String logsJson = sharedPreferences.getString(LOGS_KEY, null);

        // If no data is stored - return empty list
        if (logsJson == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type logType = new TypeToken<List<LogStrings>>() {}.getType();
        return gson.fromJson(logsJson, logType);
    }

    public static void removeLog(Context context, Logg loggToRemove) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_LOGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Convert Logg to loggStrings for JSON
        LogStrings loggToRemoveConverted = new LogStrings(loggToRemove.getName(),
                                                String.valueOf(loggToRemove.getDistance()),
                                                CalendarUtils.formattedDated(loggToRemove.getDate()),
                                                loggToRemove.getTime());

        // Get Loggs
        ArrayList<LogStrings> storedLogs = getStoredLogs(context);

        // Delete log
        for(int i = 0; i < storedLogs.size(); i++) {
            if (storedLogs.get(i).getDate().equals(loggToRemoveConverted.getDate()) && storedLogs.get(i).getName().equals(loggToRemoveConverted.getName())) {
                storedLogs.remove(i);
            }
        }

        // Convert updated list to JSON
        Gson gson = new Gson();
        String updatedLogsJson = gson.toJson(storedLogs);

        // Save updated lst back to SharedPreferences
        editor.putString(LOGS_KEY, updatedLogsJson);
        editor.apply();
    }

    public static String formattedDated(LocalDate date) { // Returns formatted date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy"); //Day Month Year
        return date.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date) { //Returns formatted date short form for week view
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy"); // Month Year
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = null;
        yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for(int i = 1; i <= 42; i++) {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(null);
            }
            else {
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }
    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while(current.isBefore(endDate)){
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while(current.isAfter(oneWeekAgo)){
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY){
                return current;
            }
            current = current.minusDays(1);
        }

        return null;
    }
    public static int getMonthInt(String month){
        switch(month) {
            case "JANUARY":
                return 0;
            case "FEBRUARY":
                return 1;
            case "MARCH":
                return 2;
            case "APRIL":
                return 3;
            case "MAY":
                return 4;
            case "JUNE":
                return 5;
            case "JULY":
                return 6;
            case "AUGUST":
                return 7;
            case "SEPTEMBER":
                return 8;
            case "OCTOBER":
                return 9;
            case "NOVEMBER":
                return 10;
            case "DECEMBER":
                return 11;
            default:
                return 12; //ERROR HAS OCCURRED
        }
    }
    public static ArrayList<String> getRepeatedDates(Boolean repeat, int numberOfWeeks){ // Method is unfinished
        ArrayList<String> dates = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        int month = getMonthInt(CalendarUtils.selectedDate.getMonth().toString()); // Get month as Int
        startDate.set(CalendarUtils.selectedDate.getYear(), month, CalendarUtils.selectedDate.getDayOfMonth());

        // Loop through each day in calendar with cloned date
        Calendar currentDate = (Calendar) startDate.clone();

        if (repeat) {
            for(int i = 0; i < numberOfWeeks; i++) {
                // Save date to array
                dates.add(formattedDated(LocalDate.of(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH) + 1, currentDate.get(Calendar.DAY_OF_MONTH))));
                // Move day to next day
                currentDate.add(Calendar.DAY_OF_MONTH, 7);
            }
        }
        return dates;
    }
}