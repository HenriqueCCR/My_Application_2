package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity Lifecycle","onCreate + MainActivity");
    }
    /**Called when user touches the button*/
    public void openCoinToss(View view){
        //Do something in response to button click
        Intent openCoinTossIntent = new Intent(getApplicationContext(), CoinTossActivity.class); //Explicit intent because specified own Activity (CointTossActivity)
        openCoinTossIntent.putExtra("ScriptureRef","Proverbs 6:6 MSG");
        startActivity(openCoinTossIntent);
    }
    public void openURL(View view){
        //Do something in response to button click
        Intent openImplicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.biblegateway.com/")); //Implicit intent used to start activity in another App without specifying app component to start
        startActivity(openImplicitIntent);                                                                             //instead specifying action and providing some data to perform the action
    }                                                                                                                 //Example - opening URL with URL link
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
}