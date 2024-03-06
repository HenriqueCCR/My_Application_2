package rodrigues.henrique.myapplication2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {
    public static LocalDate selectedDate;

    public static String formattedDated(LocalDate date) { // Returns formatted date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy"); //Day Month Year
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time) { // Returns formatted time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a"); // Hours:Minutes:Seconds
        return time.format(formatter);
    }
    public static String formattedTime2(LocalTime time) { // Returns formatted time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a"); // Hours:Minutes
        return time.format(formatter);
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
