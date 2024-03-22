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

import com.as.challenge.utility.Constants;

public class GameActivity extends Activity {
    public final static Integer RECORD_AUDIO_REQUEST_CODE = 138038;
    private final static String TAG = "MainActivity";
    private SharedPreferences _sharedPreferences;
    private final boolean _resetEnvironmentFlag = true;
    private SoundMeter soundMeter;

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
                System.out.println("NEW QTE !");
            }

            @Override
            public void launchCupCoolingQTE() {
                System.out.println("CUP COOLING QTE");
            }

            @Override
            public void launchStickBalanceQTE() {
                System.out.println("STICK BALANCE QTE");
            }
        });

        setupSoundMeter();
        setContentView(new GameView(this));
    }

    private void setupSoundMeter() {
        soundMeter = new SoundMeter();
        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        } else soundMeter.start(this);
    }

    private void handleSoundMeterPermissionResult(@NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            soundMeter.start(this);
        } else {
            // TODO Handle microphone permission denied
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundMeter.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_REQUEST_CODE)
            handleSoundMeterPermissionResult(grantResults);
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

    public SharedPreferences getSharedPreferences() {
        return _sharedPreferences;
    }
}