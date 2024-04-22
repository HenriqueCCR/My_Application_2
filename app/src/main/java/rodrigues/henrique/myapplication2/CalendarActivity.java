package rodrigues.henrique.myapplication2;

import static rodrigues.henrique.myapplication2.CalendarUtils.daysInMonthArray;
import static rodrigues.henrique.myapplication2.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ListView logListView;
    private Button deleteButton;
    private Object chosenItem;
    Toast t; // double check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        initWidgets();
        Log.i("Activity Lifecycle","onCreate");
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        final Context context = this;

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*chosenItem = adapterView.getItemAtPosition(i);
                Event event = (Event) chosenItem;

                String toast = "Deleted Event item: " + event.getName() + " " + event.getTime();
                makeToast(toast);

                CalendarUtils.removeEvent(context, event);*/
                setEventAdapter();
            }
        });
        logListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenItem = adapterView.getItemAtPosition(i);
                Logg log = (Logg) chosenItem;

                String toast = "Deleted Log item: " + log.getName() + " " + log.getDistance() + " KM";
                makeToast(toast);

                CalendarUtils.removeLog(context, log);
                setLogAdapter();
            }
        });
    }
    private void makeToast (String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
    protected  void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTextView);
        eventListView = findViewById(R.id.eventListView);
        logListView = findViewById(R.id.logListView);
        deleteButton = findViewById(R.id.deleteButton);
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

    public void setEventAdapter() {
        ArrayList<EventStrings> storedEvents = CalendarUtils.getStoredEvents(this);

        ArrayList<Event> dailyEvents = EventStrings.eventsForDate(storedEvents, CalendarUtils.selectedDate);

        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);

        eventListView.setAdapter(eventAdapter);
    }

    private void setLogAdapter() {
        // Log class clashes with util Logs so changed it to Logg
        ArrayList<LogStrings> storedLogs = CalendarUtils.getStoredLogs(this);

        ArrayList<Logg> dailyLoggs = LogStrings.logsForDate(storedLogs, CalendarUtils.selectedDate);
        LogAdapter logAdapter = new LogAdapter(getApplicationContext(), dailyLoggs);
        logListView.setAdapter(logAdapter);
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
        setEventAdapter();
    }
    public void newLogAction(View view) { startActivity(new Intent(this, LogEditActivity.class)); setLogAdapter();}
}