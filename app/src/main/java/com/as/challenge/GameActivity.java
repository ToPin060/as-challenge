package com.as.challenge;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.as.challenge.utility.Constants;

public class GameActivity extends Activity {
    private final static String TAG = "MainActivity";
    private SharedPreferences _sharedPreferences;

    private boolean _resetEnvironmentFlag = true;

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

        setContentView(new GameView(this));
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