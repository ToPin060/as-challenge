package com.as.challenge.easteregg;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class EasterEggThread extends Thread {
    private EasterEggActivity _activity;
    private SurfaceHolder _surfaceHolder;
    private EasterEggView _view;

    private boolean _running = false;
    private Canvas _canvas;

    public EasterEggThread(SurfaceHolder surfaceHolder_, EasterEggView view_, EasterEggActivity activity_) {
        super();

        _activity = activity_;
        _surfaceHolder = surfaceHolder_;
        _view = view_;

        initializeAccelerometerBehavior();
    }

    @Override
    public void run() {
        while (_running) {
            _canvas = null;
            try {
                _canvas = _surfaceHolder.lockCanvas();
                synchronized (_surfaceHolder) {
                    _view.update();
                    _view.draw(_canvas);
                }
            } catch (Exception e) {
            } finally {
                if (_canvas != null) {
                    try {
                        _surfaceHolder.unlockCanvasAndPost(_canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                sleep(60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initializeAccelerometerBehavior() {
        _activity.getAccelerometerManager().setThreshold(20f);
        _activity.getAccelerometerManager().setBehavior((x_, y_, z_) -> {
            _view.onShake();
            _activity.getAccelerometerManager().stop();
        });

    }

    public void setRunning(boolean running_) {
        _running = running_;
    }
}