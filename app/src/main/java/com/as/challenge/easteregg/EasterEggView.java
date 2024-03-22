package com.as.challenge.easteregg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.as.challenge.R;
import com.as.challenge.utility.Constants;

public class EasterEggView extends SurfaceView implements SurfaceHolder.Callback {
    private final int _windowWidth, _windowHeigh;
    private EasterEggActivity _activity;
    private EasterEggThread _thread;

    private Drawable _teacup;

    public EasterEggView(Context context_) {
        super(context_);
        getHolder().addCallback(this);

        _activity = (EasterEggActivity) context_;
        _thread = new EasterEggThread(getHolder(), this);

        _windowWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        _windowHeigh = Resources.getSystem().getDisplayMetrics().heightPixels;

        _teacup = ResourcesCompat.getDrawable(context_.getResources(), R.drawable.teacup_neutral_avg_size, null);
        _teacup.setBounds(
                _windowWidth / 2 - _teacup.getIntrinsicWidth() / 2 + 40,
                _windowHeigh - _teacup.getIntrinsicHeight() - 100,
                _windowWidth - _teacup.getIntrinsicWidth() / 2 + 40,
                _windowHeigh - 100);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        _thread.setRunning(true);
        _thread.start();
    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) { }
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
            _teacup.draw(canvas);
        }
    }

    public void update() {

    }

}