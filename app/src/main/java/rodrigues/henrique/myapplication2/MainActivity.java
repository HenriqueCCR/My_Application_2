package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity Lifecycle","onCreate");
    }
    /**Called when user touches the button*/
    public void openCoinToss(View view){
        //Do something in response to button click
        Intent openCoinTossIntent = new Intent(getApplicationContext(), CoinTossActivity.class);
        startActivity(openCoinTossIntent);
    }
}