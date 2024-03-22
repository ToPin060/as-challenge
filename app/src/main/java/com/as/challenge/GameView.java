package com.as.challenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.as.challenge.utility.Constants;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private GameActivity _activity;
    private GameThread _thread;

    private int _x = 0;
    private int _y = 0;
    private boolean _isTouching= false;

    public GameView(Context context_) {
        super(context_);
        getHolder().addCallback(this);

        _activity = (GameActivity) context_;
        _thread = new GameThread(getHolder(), this);
        _y = _activity.getSharedPreferences().getInt(Constants.KEY_VALUE_Y, 1);

        setOnTouchListener(this);
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

        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                _isTouching = true;
                _x = (int) event.getX();
                _y = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                _isTouching = false;
                break;
        }
        return true;
    }

    public void update() {
        if (_isTouching){
            Log.d("TOUCH", "X -> "+ _x);
            Log.d("TOUCH", "Y -> "+_y);
            Log.d("TOUCH", "Touch -> "+ _isTouching);
        }
    }


}
