package rodrigues.henrique.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LogEditActivity extends AppCompatActivity {
    private TextView eventDateTextView, alertTextView;
    private EditText distanceText;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private String[] runItemsList;
    private String time, chosenItem;
    private Button saveButton, timePickerButton;
    private double distance;
    private String hour, minute, second;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_edit);
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
                try {
                    time = hour + ":" + minute + ":" + second;

                    if (chosenItem == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                        builder.setCancelable(true);
                        builder.setTitle("No Run was chosen!");
                        builder.setMessage("Please select a Run");
                        alertWindowButtons(builder);
                        return;
                    }
                    if (chosenItem != null) {
                        distance = Double.parseDouble(distanceText.getText().toString());
                        if (distance == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                            builder.setCancelable(true);
                            builder.setTitle("Distance set to 0!");
                            builder.setMessage("You did more work than that I'm sure, please enter a valid distance");
                            alertWindowButtons(builder);
                            return;
                        }
                    }
                    if (time.equals("0:0:0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                        builder.setCancelable(true);
                        builder.setTitle("Time is empty!");
                        builder.setMessage("You're not faster than time, please enter a time");
                        alertWindowButtons(builder);
                    }
                    else{
                        saveLogAction(view);
                    }
                }
                catch (Exception e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No distance was given!");
                    builder.setMessage("Please write a distance for your run");
                    alertWindowButtons(builder);
                }
            }
        });
    }

    private void initWidgets() {
        eventDateTextView = findViewById(R.id.eventDateTextView);
        eventDateTextView.setText("Date: " + CalendarUtils.formattedDated(CalendarUtils.selectedDate));
        distanceText = findViewById(R.id.distanceText);
        distanceText.setFilters(new InputFilter[]{ new InputFilterMinMax("0.0", "100.0")});

        alertTextView = findViewById(R.id.alertTextView);
        runItemsList = getResources().getStringArray(R.array.runTypes);
        timePickerButton = findViewById(R.id.timePickerButton);
        saveButton = findViewById(R.id.saveButton);

        hour = "0";
        minute = "0";
        second = "0";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveLogAction(View view) {
        // Name, Distance, Time
        String logName = chosenItem;
        distance = Double.parseDouble(distanceText.getText().toString());
        time = hour + ":" + minute + ":" + second;

        // Create new Logg variable and add to log
        LogStrings newLogStrings = new LogStrings(logName,
                                                    String.valueOf(distance),
                                                    CalendarUtils.formattedDated(CalendarUtils.selectedDate),
                                                    time);

        ArrayList<LogStrings> storedLogs = CalendarUtils.getStoredLogs(this);

        storedLogs.add(newLogStrings);

        SharedPreferences sharedPreferences = getSharedPreferences("MyLogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String logsJson = gson.toJson(storedLogs);

        editor.putString("logs", logsJson);
        editor.apply();
        finish();
    }
    public void openTimePicker(View view) {
        // Create a NumberPicker dialog
        final NumberPicker hourPicker = new NumberPicker(this);
        hourPicker.setMinValue(0); // Set minimum value
        hourPicker.setMaxValue(10); // Set maximum value for hours
        hourPicker.setValue(Integer.parseInt(hour));

        final NumberPicker minutePicker = new NumberPicker(this);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(Integer.parseInt(minute));

        final NumberPicker secondPicker = new NumberPicker(this);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setValue(Integer.parseInt(second));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        // Add number pickers to LinearLayout
        layout.addView(hourPicker);
        layout.addView(minutePicker);
        layout.addView(secondPicker);

        //ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Dialog);
        // Create an AlertDialog with NumberPickers as its view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Time");
        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String setTime = String.format("%02d : %02d : %02d",
                        hourPicker.getValue(), minutePicker.getValue(), secondPicker.getValue());
                timePickerButton.setText(setTime);

                hour = String.valueOf(hourPicker.getValue());
                if(hour.length() == 1) { hour = "0" + hour; }

                minute = String.valueOf(minutePicker.getValue());
                if(minute.length() == 1) { minute = "0" + minute; }

                second = String.valueOf(secondPicker.getValue());
                if(second.length() == 1) { second = "0" + second; }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String setTime = String.format("%02d : %02d : %02d",
                        00, 00, 00);
                timePickerButton.setText(setTime);
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}