package rodrigues.henrique.myapplication2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RunningInformationActivity extends AppCompatActivity {
    private Button longDistanceButton, mediumDistanceButton, shortDistanceButton, hillProgressionButton, tempoButton;

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
        hillProgressionButton = findViewById(R.id.hillProgressionInfoButton);
        tempoButton = findViewById(R.id.tempoInfoButton);
    }
    public void openLongDistanceInfo(){

    }
}
