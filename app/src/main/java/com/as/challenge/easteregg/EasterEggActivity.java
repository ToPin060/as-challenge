package com.as.challenge.easteregg;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.as.challenge.accelerometer.AccelerometerBehavior;
import com.as.challenge.accelerometer.AccelerometerManager;

public class EasterEggActivity extends Activity {
    private final static String TAG = EasterEggActivity.class.getSimpleName();

    private AccelerometerManager _accelerometerManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        _accelerometerManager = new AccelerometerManager(this);

        setContentView(new EasterEggView(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        _accelerometerManager.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        _accelerometerManager.stop();
    }
    @Override
    protected void onStop() {
        super.onStop();
        _accelerometerManager.stop();
    }

    public AccelerometerManager getAccelerometerManager() {
        return _accelerometerManager;
    }
}
