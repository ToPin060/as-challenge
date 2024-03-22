package com.as.challenge.accelerometer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class AccelerometerManager {
    private final static String TAG = AccelerometerManager.class.getSimpleName();

    private Activity _activity;
    private SensorManager _sensorManager;

    private Sensor _sensor;
    private SensorEventListener _sensorEventListener;
    private AccelerometerBehavior _behavior;
    private float _threshold = 12f;
    private float _xMax, _yMax, _zMax;

    public AccelerometerManager(Activity activity_) {
        _activity = activity_;
        _sensorManager = (SensorManager) _activity.getSystemService(_activity.SENSOR_SERVICE);

        _sensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (_sensor == null) {
            Toast.makeText(_activity, "The device has no Gyroscope !", Toast.LENGTH_SHORT).show();
            return;
        }

        initializeEventListener();
    }

    public void initializeEventListener() {
        _sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = calibrateSensorValue(event.values[0]);
                float y = calibrateSensorValue(event.values[1]);
                float z = calibrateSensorValue(event.values[2]);

                if (x > _threshold || y > _threshold || z > _threshold) {
                    Log.d(TAG, "SHAKE DETECTED");
                    if (_behavior != null) {
                        _behavior.behavior(x, y, z);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };
    }

    public void start() {
        _sensorManager.registerListener(_sensorEventListener, _sensor, SensorManager.SENSOR_DELAY_GAME);
    }
    public void stop() {
        _sensorManager.unregisterListener(_sensorEventListener);
    }


    public float calibrateSensorValue(float value) {
        return (float) (((int) (value*10)) /10);
    }

    public AccelerometerBehavior getBehavior() {
        return _behavior;
    }
    public void setBehavior(AccelerometerBehavior behavior_) {
        _behavior = behavior_;
    }

    public float getThreshold() {
        return _threshold;
    }
    public void setThreshold(float threshold_) {
        _threshold = threshold_;
    }
}
