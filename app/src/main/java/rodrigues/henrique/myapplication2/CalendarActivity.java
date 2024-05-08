package rodrigues.henrique.myapplication2;

import static rodrigues.henrique.myapplication2.CalendarUtils.daysInMonthArray;
import static rodrigues.henrique.myapplication2.CalendarUtils.monthYearFromDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ListView logListView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        initWidgets();
        Log.i("Activity Lifecycle","onCreate");
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setEventAdapter();
            }
        });
        logListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setLogAdapter();
            }
        });
    }
    protected void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTextView);
        eventListView = findViewById(R.id.eventListView);
        logListView = findViewById(R.id.logListView);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days,this, this);
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

    public void setEventAdapter() { //Order events by time
        ArrayList<EventStrings> storedEvents = CalendarUtils.getStoredEvents(this);

        ArrayList<Event> dailyEvents = EventStrings.eventsForDate(storedEvents, CalendarUtils.selectedDate);

        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);

        sortItemsByTime(dailyEvents);

        eventListView.setAdapter(eventAdapter);
    }

    private void setLogAdapter() {
        ArrayList<LogStrings> storedLogs = CalendarUtils.getStoredLogs(this);

        ArrayList<Logg> dailyLoggs = LogStrings.logsForDate(storedLogs, CalendarUtils.selectedDate);

        LogAdapter logAdapter = new LogAdapter(getApplicationContext(), dailyLoggs);

        logListView.setAdapter(logAdapter);
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }
    public void newLogAction(View view) { startActivity(new Intent(this, LogEditActivity.class)); }

    public static void sortItemsByTime(ArrayList<Event> itemsList) {
        Collections.sort(itemsList, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                // Parsing time string into LocalTime to compare

                LocalTime time1 = LocalTime.parse(event1.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime time2 = LocalTime.parse(event2.getTime(), DateTimeFormatter.ofPattern("HH:mm"));

                //compare the times
                return time1.compareTo(time2);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}