package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time, true);
        Event.eventsList.add(newEvent);

        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyEvents", Context.MODE_PRIVATE);

        // Get the SharedPreferences editor to edit data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the eventsList to a String using Gson
        Gson gson = new Gson();
        String eventsJson = gson.toJson(Event.eventsList);

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