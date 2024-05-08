package rodrigues.henrique.myapplication2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunningInformationActivity extends AppCompatActivity {
    private Button longDistanceButton, mediumDistanceButton, shortDistanceButton, hillButton, tempoButton, progressionButton;
    public static TextView popupInfoWindowTextView;
    public String longDistanceTitle, mediumDistanceTitle, shortDistanceTitle, tempoTitle, hillTitle, progressionTitle;
    public static String windowTitle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_information);
        initWidgets();
        Log.i("Activity Lifecycle","onCreate");
    }
    protected void initWidgets() {
        longDistanceButton = findViewById(R.id.longDistanceInfoButton);
        mediumDistanceButton = findViewById(R.id.mediumDistanceInfoButton);
        shortDistanceButton = findViewById(R.id.shortDistanceInfoButton);
        hillButton = findViewById(R.id.hillInfoButton);
        tempoButton = findViewById(R.id.tempoInfoButton);
        progressionButton = findViewById(R.id.progressionInfoButton);

        longDistanceTitle = getResources().getStringArray(R.array.runTypesTitles)[0];
        mediumDistanceTitle = getResources().getStringArray(R.array.runTypesTitles)[1];
        shortDistanceTitle = getResources().getStringArray(R.array.runTypesTitles)[2];
        tempoTitle = getResources().getStringArray(R.array.runTypesTitles)[3];
        hillTitle = getResources().getStringArray(R.array.runTypesTitles)[4];
        progressionTitle = getResources().getStringArray(R.array.runTypesTitles)[5];

        popupInfoWindowTextView = findViewById(R.id.popupInfoWindow);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openInfo(View view){
        InputStream inputStream = null;
        if(view.getId() == R.id.longDistanceInfoButton) {
            // Create InputStream object
            inputStream = this.getResources().openRawResource(R.raw.long_distance_info);
            windowTitle = longDistanceTitle;
        }
        else if(view.getId() == R.id.mediumDistanceInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.medium_distance_info);
            windowTitle =  mediumDistanceTitle;
        }
        else if(view.getId() == R.id.shortDistanceInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.short_distance_info);
            windowTitle =  shortDistanceTitle;
        }
        else if(view.getId() == R.id.hillInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.hill_info);
            windowTitle =  hillTitle;
        }
        else if(view.getId() == R.id.tempoInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.tempo_info);
            windowTitle =  tempoTitle;
        }
        else if(view.getId() == R.id.progressionInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.progression_info);
            windowTitle =  progressionTitle;
        }

        //Create BufferedReader object
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        //Create StringBuffer object
        StringBuffer stringBuffer = new StringBuffer();

        String strData = "";

        if(inputStream!=null) {
            try {
                while((strData = bufferedReader.readLine()) != null) {
                    stringBuffer.append(strData + "\n");
                }

                popupInfoWindowTextView.setText(stringBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RunningInformationFragment dialogFragment = new RunningInformationFragment();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }
}
