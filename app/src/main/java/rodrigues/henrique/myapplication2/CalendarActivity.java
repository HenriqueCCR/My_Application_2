package rodrigues.henrique.myapplication2;

import static rodrigues.henrique.myapplication2.CalendarUtils.daysInMonthArray;
import static rodrigues.henrique.myapplication2.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ListView logListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        initWidgets();
        Log.i("Activity Lifecycle","onCreate");
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
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
    }

    private void setLogAdapter() {
        // Log class clashes with util Logs
        ArrayList<rodrigues.henrique.myapplication2.Log> dailyLogs = rodrigues.henrique.myapplication2.Log.logsForDate(CalendarUtils.selectedDate);
        LogAdapter logAdapter = new LogAdapter(getApplicationContext(), dailyLogs);
        logListView.setAdapter(logAdapter);
    }

    public void newEventAction(View view) { startActivity(new Intent(this, EventEditActivity.class)); }
    public void newLogAction(View view) {
        startActivity(new Intent(this, LogEditActivity.class));
    }
}