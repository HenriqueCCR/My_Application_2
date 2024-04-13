package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {
    private TextView eventDateTextView;
    private TextView selectedTimeTextView;
    private TextView alertTextView;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private String[] runItemsList;
    private String time;
    private String chosenItem;
    private Button saveButton;
    Button timeButton;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, runItemsList);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenItem = adapterView.getItemAtPosition(i).toString();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add pop up when User inputs run for same time
                if (chosenItem == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No Run was chosen!");
                    builder.setMessage("Please select a Run");
                    alertWindowButtons(builder); // Sending re-usable code so alertWindowButtons()
                }
                else if (time == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No time was given!");
                    builder.setMessage("Please select a time for your run");
                    alertWindowButtons(builder);
                }
                else{
                    saveEventAction(view);
                }
            }
        });
    }

    private void initWidgets() {
        eventDateTextView = findViewById(R.id.eventDateTextView);
        selectedTimeTextView = findViewById(R.id.selectedTimeTextView);
        eventDateTextView.setText("Date: " + CalendarUtils.formattedDated(CalendarUtils.selectedDate));
        alertTextView = findViewById(R.id.alertTextView);
        runItemsList = getResources().getStringArray(R.array.runTypes);
        saveButton = findViewById(R.id.saveButton);
        timeButton = findViewById(R.id.timeButton);
    }

    public void saveEventAction(View view) {
        String eventName = chosenItem;

        // Create string copy for Json
        EventStrings newEventStrings = new EventStrings(eventName,
                                                            CalendarUtils.formattedDated(CalendarUtils.selectedDate),
                                                            time,
                                                            "true");

        // If stored events already exist add new event to current list
        ArrayList<EventStrings> storedEvents = CalendarUtils.getStoredEvents(this);
        if (CalendarUtils.getStoredEvents(this).size() > 0) {
            for (EventStrings event : storedEvents) {
                EventStrings.eventsStringsList.add(event);
            }
        }

        EventStrings.eventsStringsList.add(newEventStrings);

        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyEvents", Context.MODE_PRIVATE); // Saves all variables into String format - but can't save LocalDate into regular String only "00 00 0000"

        // Get the SharedPreferences editor to edit data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the eventsList to a String using Gson
        Gson gson = new Gson();

        //ArrayList<String> forEventsJson = new ArrayList<>(Arrays.asList(chosenItem, CalendarUtils.formattedDated(CalendarUtils.selectedDate), time, "true"));
        String eventsJson = gson.toJson(EventStrings.eventsStringsList); // Originally: String eventsJson = gson.toJson(Event.eventsList);

        // Store the eventsJson string in SharedPreferences
        editor.putString("events", eventsJson);
        editor.apply();

        finish();
    }

    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                time = (String.format(Locale.getDefault(), "%02d:%02d", hour, minute)); // %02d:%02d sets Hours and minutes to 2 decimal places
                selectedTimeTextView.setText(time);
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void alertWindowButtons(AlertDialog.Builder builder) {
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertTextView.setVisibility(View.VISIBLE);
            }
        });
        builder.show();
    }
}