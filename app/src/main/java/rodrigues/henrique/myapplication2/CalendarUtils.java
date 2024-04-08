package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarUtils {
    public static LocalDate selectedDate;

    private static final String SHARED_PREF_NAME = "MyEvents";
    private static final String EVENTS_KEY = "events";

    public static ArrayList<String> getStoredEvents(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String eventsJson = sharedPreferences.getString(EVENTS_KEY, null);

        // If no data is stored - return empty list
        if (eventsJson == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type eventType = new TypeToken<List<Event>>() {}.getType();
        return gson.fromJson(eventsJson, eventType);
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


}
