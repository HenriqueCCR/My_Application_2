package rodrigues.henrique.myapplication2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RunningInformationActivity extends AppCompatActivity {
    private Button longDistanceButton, mediumDistanceButton, shortDistanceButton, hillProgressionButton, tempoButton;
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
        hillProgressionButton = findViewById(R.id.hillProgressionInfoButton);
        tempoButton = findViewById(R.id.tempoInfoButton);

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

    public void openLongDistanceInfo(){

    }
}
