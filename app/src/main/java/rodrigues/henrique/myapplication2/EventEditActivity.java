package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {
    private TextView eventDateTextView, selectedTimeTextView, alertTextView;
    private EditText repeatWeeks;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private String[] runItemsList;
    private String time;
    private String chosenItem, chosenDay;
    private Button saveButton;
    Button timeButton, repeatingEventButton;
    int hour, minute, numberOfRepeatWeeks;
    boolean repeatEvent;
    Toast t; // double check

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
        repeatWeeks = findViewById(R.id.repeatWeeks);
        repeatWeeks.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});
        runItemsList = getResources().getStringArray(R.array.runTypes);
        saveButton = findViewById(R.id.saveButton);
        timeButton = findViewById(R.id.timeButton);
        repeatingEventButton = findViewById(R.id.repeatEventButton);
        repeatEvent = false;
    }

    public void saveEventAction(View view) {
        ArrayList<String> dates = new ArrayList<>();
        dates = CalendarUtils.getRepeatedDates(repeatEvent, Integer.parseInt(repeatWeeks.getText().toString())); // get dates Strings

        // Need to create Try Catch for when user has clicked REPEAT but no boxes ticked
        String eventName = chosenItem;
        for(String date: dates) {
            // Create string copy for Json
            EventStrings newEventStrings = new EventStrings(eventName,
                    date,
                    time,
                    "true");

            ArrayList<EventStrings> storedEvents = CalendarUtils.getStoredEvents(this);
            //Check if event already exists
            for(EventStrings event : storedEvents) {
                if(LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("dd MMMM yyyy")).equals(date) && event.getTime().equals(newEventStrings.getTime())) {
                    makeToast("Oops! Event already exists for " + newEventStrings.getTime());
                    return; //If event already exists return without doing anything
                }
            }

            storedEvents.add(newEventStrings);

            // Get SharedPreferences instance
            SharedPreferences sharedPreferences = getSharedPreferences("MyEvents", Context.MODE_PRIVATE); // Saves all variables into String format - but can't save LocalDate into regular String only "00 00 0000"

            // Get the SharedPreferences editor to edit data
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Convert the eventsList to a String using Gson
            Gson gson = new Gson();

            //ArrayList<String> forEventsJson = new ArrayList<>(Arrays.asList(chosenItem, CalendarUtils.formattedDated(CalendarUtils.selectedDate), time, "true"));
            String eventsJson = gson.toJson(storedEvents); // Change EventStrings.eventsStringsList to storedEvents

            // Store the eventsJson string in SharedPreferences
            editor.putString("events", eventsJson);
            editor.apply();

            finish();
        }
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

    public void setRepeatEvent(View view) {
        if (repeatEvent) {
            repeatEvent = false;
            repeatingEventButton.setBackgroundColor(getResources().getColor(R.color.darkGray)); // Grey out the button
            repeatWeeks.setText("");
            repeatWeeks.setEnabled(false);
        }
        else {
            repeatEvent = true;
            repeatingEventButton.setBackgroundColor(getResources().getColor(R.color.lightBlue)); // GIve button colour to indicate active
            repeatWeeks.setEnabled(true);
        }
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
    private void makeToast (String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}