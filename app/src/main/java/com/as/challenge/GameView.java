package com.as.challenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.as.challenge.utility.Constants;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameActivity _activity;
    private GameThread _thread;

    private int _x = 0;
    private int _y = 0;

    public GameView(Context context_) {
        super(context_);
        getHolder().addCallback(this);

        _activity = (GameActivity) context_;
        _thread = new GameThread(getHolder(), this);
        _y = _activity.getSharedPreferences().getInt(Constants.KEY_VALUE_Y, 1);

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

            Paint paint = new Paint();
            paint.setColor(Color.rgb(250, 0 , 0));
            canvas.drawRect(_x, 100 * _y, _x +100, 200, paint);
        }
    }
    public void update() {
        _x = (_x + 1) % 300;
    }

}
