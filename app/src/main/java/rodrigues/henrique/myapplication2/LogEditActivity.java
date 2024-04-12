package rodrigues.henrique.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LogEditActivity extends AppCompatActivity {
    private TextView eventDateTextView;
    private EditText distanceText;
    private EditText hourInputText;
    private EditText minuteInputText;
    private EditText secondInputText;
    private TextView alertTextView;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private String[] runItemsList;
    private String time;
    private String chosenItem;
    private Button saveButton;
    double distance;

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
                if (chosenItem == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No Run was chosen!");
                    builder.setMessage("Please select a Run");
                    alertWindowButtons(builder);
                }
                else if (distanceText == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No distance was given!");
                    builder.setMessage("Please write a distance for your run");
                    alertWindowButtons(builder);
                }
                else if (hourInputText == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No hour was given!");
                    builder.setMessage("Please select a time for your run");
                    alertWindowButtons(builder);
                }
                else if (minuteInputText == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No minute was given!");
                    builder.setMessage("Please select a time for your run");
                    alertWindowButtons(builder);
                }
                else if (minuteInputText == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No second was given!");
                    builder.setMessage("Please select a time for your run");
                    alertWindowButtons(builder);
                }
                else{
                    saveLogAction(view);
                }
            }
        });
    }

    private void initWidgets() {
        eventDateTextView = findViewById(R.id.eventDateTextView);
        eventDateTextView.setText("Date: " + CalendarUtils.formattedDated(CalendarUtils.selectedDate));
        distanceText = findViewById(R.id.distanceText);
        distanceText.setFilters(new InputFilter[]{ new InputFilterMinMax("0.0", "100.0")});

        hourInputText = findViewById(R.id.hourInputText);
        hourInputText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "10")});
        minuteInputText = findViewById(R.id.minuteInputText);
        minuteInputText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        secondInputText = findViewById(R.id.secondInputText);
        secondInputText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

        alertTextView = findViewById(R.id.alertTextView);
        runItemsList = getResources().getStringArray(R.array.runTypes);
        saveButton = findViewById(R.id.saveButton);
    }

    public void saveLogAction(View view) {
        String logName = chosenItem;
        distance = Double.parseDouble(distanceText.getText().toString());
        time = hourInputText.getText().toString() + ":" + minuteInputText.getText().toString() + ":" + secondInputText.getText().toString(); // Saving EditText fields directly into time variable
        Logg newLogg = new Logg(logName,Double.parseDouble(distanceText.getText().toString()), CalendarUtils.selectedDate, time);
        Logg.loggEventsList.add(newLogg);

        LogStrings newLogStrings = new LogStrings(logName, String.valueOf(distance), CalendarUtils.formattedDated(CalendarUtils.selectedDate), time);

        ArrayList<LogStrings> storedLogs = CalendarUtils.getStoredLogs(this);
        if (CalendarUtils.getStoredLogs(this).size() >0) {
            for (LogStrings log : storedLogs){
                LogStrings.logStringsList.add(log);
            }
        }

        LogStrings.logStringsList.add(newLogStrings);
        SharedPreferences sharedPreferences = getSharedPreferences("MyLogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String logsJson = gson.toJson(LogStrings.logStringsList);

        editor.putString("logs", logsJson);
        editor.apply();
        finish();
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
