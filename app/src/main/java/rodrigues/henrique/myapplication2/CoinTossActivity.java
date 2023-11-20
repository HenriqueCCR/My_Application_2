package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import java.util.Random;
import android.widget.TextView;
import android.widget.Toast;

public class CoinTossActivity extends AppCompatActivity {
    public Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);
        Log.i("Activity Lifecycle","onCreate");

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("ScriptureRef");
        Toast.makeText(getApplicationContext(), //Toast is a little message that pops up when activated
                "This is the extra string that we passed in: " + name,
                Toast.LENGTH_LONG).show();

        int numberOfTosses = retrievePreviousTosses();
        if(numberOfTosses == -1) {
            numberOfTosses = 1;
        }
        else {
            numberOfTosses++;
        }
        Toast.makeText(getApplicationContext(),
                "The coin has been tossed: " + numberOfTosses + " times.",
                Toast.LENGTH_LONG).show();
        storePreviousTosses(numberOfTosses);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle","onPause + Coin Toss Activity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Lifecycle","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle","onResume + Coin Toss Activity");
        TextView coinTossView = findViewById(R.id.coinTossView);

        String result = getCoinToss();

        coinTossView.setText(result);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity Lifecycle","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity Lifecycle","onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Activity Lifecycle","onRestart");
    }
    private String getCoinToss(){
        if(random.nextBoolean()){
            return getString(R.string.coinTossResult1);
        }
        return getString(R.string.coinTossResult2);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        TextView coinTossView = (TextView) findViewById(R.id.coinTossView);
        String responseString = coinTossView.getText().toString();
        data.putExtra("ResponseString",responseString);
        setResult(RESULT_OK, data);
        super.finish();
    }
    private void storePreviousTosses(int pNumberOfTosses) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(
                "rodrigues.henrique.MyApplication2",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(
                "numberOfTosses",
                pNumberOfTosses);
        editor.commit();
    }
    private int retrievePreviousTosses() {
        int previousTosses = 0;
        SharedPreferences sharedPreferences =
                this.getApplication().getSharedPreferences(
                        "rodrigues.henrique.MyApplication2",
                        Context.MODE_PRIVATE);
        previousTosses = sharedPreferences.getInt(
                "numberOfTosses",
                -1);
        return previousTosses;
    }
}