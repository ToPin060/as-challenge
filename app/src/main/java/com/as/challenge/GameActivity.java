package com.as.challenge;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.as.challenge.accelerometer.AccelerometerManager;
import com.as.challenge.qte.QTE;
import com.as.challenge.qte.QTEHandler;
import com.as.challenge.qte.TeacupCoolingQTE;
import com.as.challenge.utility.Constants;

public class GameActivity extends Activity {
    public final static Integer RECORD_AUDIO_REQUEST_CODE = 138038;
    private final static String TAG = "MainActivity";
    private final boolean _resetEnvironmentFlag = true;

    private SharedPreferences _sharedPreferences;
    private SoundMeter soundMeter;
    private GameView _gameView;
    private AccelerometerManager _accelerometerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        _sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        if (_resetEnvironmentFlag) resetEnvironment();
        else recoverEnvironment();

        // TODO REMOVE : Exemple usage of QTEHandler
        QTEHandler test = new QTEHandler(new QTEHandler.Callback() {
            @Override
            public void onNewQTE(QTEHandler.EVENT_TYPES eventType) {
            }

            @Override
            public QTE launchTeacupCoolingQTE() {
                if(_gameView != null) {
                    TeacupCoolingQTE qte = (TeacupCoolingQTE) _gameView.QTEs.get(1); // TODO CHANGE
                    qte.setSoundMeter(soundMeter);
                    return qte;
                }
                return null;
            }

            @Override
            public QTE launchDeadlyZoneQTE() {
                if(_gameView != null) {
                    return _gameView.QTEs.get(0); // TODO CHANGE
                }
                return null;
            }
        });

        setupSoundMeter();
        _accelerometerManager = new AccelerometerManager(this);

        _gameView = new GameView(this);
        setContentView(_gameView);
    }

  private void setupSoundMeter() {
        soundMeter = new SoundMeter();
        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }
    }
  
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_REQUEST_CODE)
            handleSoundMeterPermissionResult(grantResults);
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
        soundMeter.stop();
        _accelerometerManager.stop();
    }

    private void recoverEnvironment() {
        // Recover Y value
        int valeur_y = _sharedPreferences.getInt(Constants.KEY_VALUE_Y, 0);

        // Update Y value
        valeur_y = valeur_y + 1;
        Log.d(TAG, String.format("%s", valeur_y));
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putInt("VALUE_Y", valeur_y);
        editor.apply();
    }
    private void resetEnvironment() {
        // Reset Y value to 1
        int valeur_y = 1;
        Log.d(TAG, String.format("%s", valeur_y));
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putInt("VALUE_Y", valeur_y);
        editor.apply();
    }

    private void handleSoundMeterPermissionResult(@NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            soundMeter.start(this);
        } else {
            // TODO Handle microphone permission denied
        }
    }

    public SharedPreferences getSharedPreferences() {
        return _sharedPreferences;
    }
}