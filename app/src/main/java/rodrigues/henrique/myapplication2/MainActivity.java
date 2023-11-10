package rodrigues.henrique.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity Lifecycle","onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle","onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Lifecycle","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle","onResume");
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
}