package com.as.challenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.as.challenge.utility.Constants;


public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    public DeadlyZoneQTE deadlyZoneQTE;
    private GameActivity _activity;
    private GameThread _thread;

    private Drawable _touillette;

    private int _x;
    private int _y;
    private int _xTou = 300;
    private int _yTou = 800;
    private boolean _isTouching= false;
    private boolean _touchingTouillette = false;

    public GameView(Context context_) {
        super(context_);
        getHolder().addCallback(this);

        _activity = (GameActivity) context_;
        _thread = new GameThread(getHolder(), this);


        setTouillette(context_, R.drawable.touillette_neutral);
        setTouilletteCoords(_xTou, _yTou);

        deadlyZoneQTE = new DeadlyZoneQTE(this, _touillette);

        setOnTouchListener(this);
        setFocusable(true);
    }

    public void setTouillette(Context context_, int ressource_){
        _touillette = ResourcesCompat.getDrawable(context_.getResources(), ressource_, null);
    }

    public void setTouilletteCoords(int x_, int y_){

        _xTou = x_;
        _yTou = y_;

        _touillette.setBounds(x_ - _touillette.getIntrinsicWidth() / 2,
                y_ - _touillette.getIntrinsicHeight() / 2,
                x_ + _touillette.getIntrinsicWidth() / 2,
                y_ + _touillette.getIntrinsicHeight() / 2);
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
          
            setTouilletteCoords(_xTou, _yTou);
            _touillette.draw(canvas);

            _touchingTouillette = (_xTou - _touillette.getIntrinsicWidth() / 2) < _x &&
                    (_xTou + _touillette.getIntrinsicWidth() / 2) > _x &&
                    (_yTou - _touillette.getIntrinsicHeight() / 2) < _y &&
                    (_yTou + _touillette.getIntrinsicHeight() / 2) > _y;

            if (_isTouching) {
                setTouilletteCoords(_x,_y);
            }
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                _x = (int) event.getX();
                _y = (int) event.getY();
                if(_touchingTouillette){
                    _isTouching = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                _isTouching = false;

                if (_xTou <=_touillette.getBounds().width() && _yTou <=_touillette.getBounds().height()){
                    _x = 500;
                    _y = 500;
                }
                else {
                    _x = 0;
                    _y = 0;
                }
                break;
        }
        return true;
    }

    private void handleQTEs(Canvas canvas) {
        if (deadlyZoneQTE.isTriggered()) deadlyZoneQTE.draw(canvas);
    }
  
    public void update() {
        if (_isTouching){

            boolean touilletteIsNull = _touillette == null;

            Log.d("TOUCH", "X -> "+ _x);
            Log.d("TOUCH", "Y -> "+_y);
            Log.d("TOUCH", "Touch -> "+ _isTouching);
        }
    }


}
