package com.as.challenge;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private final SurfaceHolder _surfaceHolder;
    private final GameView _gameView;

    private boolean _running = false;
    private Canvas _canvas;

    public GameThread(SurfaceHolder surfaceHolder_, GameView gameView_) {
        super();

        this._surfaceHolder = surfaceHolder_;
        this._gameView = gameView_;
    }

    @Override
    public void run() {
        while (_running) {
            _canvas = null;
            try {
                _canvas = this._surfaceHolder.lockCanvas();
                synchronized (_surfaceHolder) {
                    this._gameView.update();
                    this._gameView.draw(_canvas);
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

    public void setRunning(boolean running_) {
        _running = running_;
    }
}
