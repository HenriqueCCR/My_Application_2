package rodrigues.henrique.myapplication2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_DIALOG_RESPONSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity Lifecycle","onCreate + MainActivity");
    }
    /**Called when user touches the button*/

    public void openCalendar(View view){
        Intent openCalendarIntent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(openCalendarIntent);
    }
    public void openInfoPage(View view){
        Intent openInfoPageIntent = new Intent(getApplicationContext(), RunningInformationActivity.class);
        startActivity(openInfoPageIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle","onPause + MainActivity");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle","onResume + MainActivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_DIALOG_RESPONSE) {
            if(data.hasExtra("ResponseString")) {
                //do something here with data
                String responseString = data.getExtras().getString("ResponseString");
                Toast.makeText(getApplicationContext(),
                        "This is the response we got: " + responseString,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}