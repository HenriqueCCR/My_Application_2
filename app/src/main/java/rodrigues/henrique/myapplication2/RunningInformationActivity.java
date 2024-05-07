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

import io.reactivex.internal.operators.maybe.MaybeFromFuture;

public class RunningInformationActivity extends AppCompatActivity {
    private Button longDistanceButton, mediumDistanceButton, shortDistanceButton, hillButton, tempoButton, progressionButton;
    public static TextView popupInfoWindowTextView;
    public String longDistanceTitle = "Long Distance Running";
    public String mediumDistanceTitle = "Medium Distance Running";
    public String shortDistanceTitle = "Short Distance Running";
    public String hillTitle = "Hill Runs";
    public String tempoTitle = "Tempo Runs";
    public String progressionTitle = "Progression Runs";
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
            inputStream = this.getResources().openRawResource(R.raw.sample);
            windowTitle = longDistanceTitle;
        }
        else if(view.getId() == R.id.mediumDistanceInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.other_sample);
            windowTitle =  mediumDistanceTitle;
        }
        else if(view.getId() == R.id.shortDistanceInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.other_sample);
            windowTitle =  shortDistanceTitle;
        }
        else if(view.getId() == R.id.hillInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.other_sample);
            windowTitle =  hillTitle;
        }
        else if(view.getId() == R.id.tempoInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.other_sample);
            windowTitle =  tempoTitle;
        }
        else if(view.getId() == R.id.progressionInfoButton) {
            inputStream = this.getResources().openRawResource(R.raw.other_sample);
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

        LongDistanceFragment dialogFragment = new LongDistanceFragment();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }
}
