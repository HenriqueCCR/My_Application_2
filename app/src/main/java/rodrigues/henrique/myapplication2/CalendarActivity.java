package rodrigues.henrique.myapplication2;

import static rodrigues.henrique.myapplication2.CalendarUtils.daysInMonthArray;
import static rodrigues.henrique.myapplication2.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ListView logListView;
    private Object chosenItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        initWidgets();
        Log.i("Activity Lifecycle","onCreate");
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        logListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenItem = adapterView.getItemAtPosition(i);
                removeItem(chosenItem);
            }
        });
    }
    protected  void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTextView);
        eventListView = findViewById(R.id.eventListView);
        logListView = findViewById(R.id.logListView);
    }

    protected void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
        setLogAdapter();
    }

    public void previousMonthAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null){
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);

        ArrayList<EventStrings> storedEvents = CalendarUtils.getStoredEvents(this);
        for (EventStrings event : storedEvents) {
            try{
                /*if(event.getTime().equals(dailyEvents.get(0).getTime())) {
                    continue;
                }*/
                if(LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")).equals(CalendarUtils.selectedDate)){
                    Event newEvent = new Event(event.getName(), LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")), event.getTime(), Boolean.parseBoolean(event.getVisibility()));
                    dailyEvents.add(newEvent);
                }
            }
            catch (Exception e) {
                // Empty dailyEvents Array
                System.out.println("Empty dailyEvents array - could not index");

                if(LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")).equals(CalendarUtils.selectedDate)){
                    Event newEvent = new Event(event.getName(), LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")), event.getTime(), Boolean.parseBoolean(event.getVisibility()));
                    dailyEvents.add(newEvent);
                }
            }
        }

        eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    private void setLogAdapter() {
        // Log class clashes with util Logs
        ArrayList<Logg> dailyLoggs = Logg.logsForDate(CalendarUtils.selectedDate);
        LogAdapter logAdapter = new LogAdapter(getApplicationContext(), dailyLoggs);
        logListView.setAdapter(logAdapter);

        ArrayList<LogStrings> storedLog = CalendarUtils.getStoredLogs(this);
        for (LogStrings log : storedLog) {
            try{
                if(LocalDate.parse(log.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")).equals(CalendarUtils.selectedDate)){
                    Logg newLog = new Logg(log.getName(), Double.parseDouble(log.getDistance()), LocalDate.parse(log.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")), log.getTime());
                    dailyLoggs.add(newLog);
                }
            }
            catch (Exception e) {
                System.out.println("Empty dailyLoggs array - could not index");

                if(LocalDate.parse(log.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")).equals(CalendarUtils.selectedDate)){
                    Logg newLog = new Logg(log.getName(), Double.parseDouble(log.getDistance()), LocalDate.parse(log.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")), log.getTime());
                    dailyLoggs.add(newLog);
                }
            }
        }

        logAdapter = new LogAdapter(getApplicationContext(), dailyLoggs);
        logListView.setAdapter(logAdapter);
    }

    public void removeItem(Object removeItem){
        ArrayList<LogStrings> storedLog = CalendarUtils.getStoredLogs(this);
        storedLog.remove(removeItem);

        setLogAdapter();
    }

    public void newEventAction(View view) { startActivity(new Intent(this, EventEditActivity.class)); }
    public void newLogAction(View view) {
        startActivity(new Intent(this, LogEditActivity.class));
    }
}