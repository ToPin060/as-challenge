package com.as.challenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.as.challenge.utility.Constants;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public DeadlyZoneQTE deadlyZoneQTE;
    private final GameActivity _activity;
    private final GameThread _thread;
    private int _x = 0;
    private int _y = 0;

    public GameView(Context context_) {
        super(context_);
        getHolder().addCallback(this);

        _activity = (GameActivity) context_;
        _thread = new GameThread(getHolder(), this);
        _y = _activity.getSharedPreferences().getInt(Constants.KEY_VALUE_Y, 1);

        deadlyZoneQTE = new DeadlyZoneQTE(this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        _thread.setRunning(true);
        _thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                _thread.setRunning(false);
                _thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            handleQTEs(canvas);
        }
    }

    private void handleQTEs(Canvas canvas) {
        if (deadlyZoneQTE.isTriggered()) deadlyZoneQTE.draw(canvas);
    }

    public void update() {
        _x = (_x + 1) % 300;
    }

}
