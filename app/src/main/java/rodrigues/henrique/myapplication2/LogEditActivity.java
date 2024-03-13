package rodrigues.henrique.myapplication2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

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
    int hour, minute, second, distance;

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
                else if (distance > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No time was given!");
                    builder.setMessage("Please select a time for your run");
                    alertWindowButtons(builder);
                }
                /*else if (timeText == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogEditActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("No time was given!");
                    builder.setMessage("Please select a time for your run");
                    alertWindowButtons(builder);
                }*/
                else{
                    saveEventAction(view);
                }
            }
        });
    }

    private void initWidgets() {
        eventDateTextView = findViewById(R.id.eventDateTextView);
        eventDateTextView.setText("Date: " + CalendarUtils.formattedDated(CalendarUtils.selectedDate));
        distanceText = findViewById(R.id.distanceText);

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

    public void saveEventAction(View view) {
        String logName = chosenItem;
        Log newLog = new Log(logName,distance, CalendarUtils.selectedDate, time);
        Log.logEventsList.add(newLog);
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
