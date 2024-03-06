package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private TextView eventDateTextView, eventTimeTextView;
    private String time;
    Button timeButton;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        eventDateTextView.setText("Date: " + CalendarUtils.formattedDated(CalendarUtils.selectedDate));
        timeButton = findViewById(R.id.timeButton);
    }

    private void initWidgets() {
        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDateTextView = findViewById(R.id.eventDateTextView);
    }

    public void saveEventAction(View view) {
        String eventName = eventNameEditText.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish();
    }
    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                //timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute)); // %02d:%02d sets Hours and minutes to 2 decimal places
                time = (String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}