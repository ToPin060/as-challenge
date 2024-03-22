package com.as.challenge;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;

public class TeacupCoolingQTE implements QTE{
    private final GameView gameView;
    private final Drawable teacup;
    private boolean isTriggered = false;
    private boolean threadRunning = true;
    private SoundMeter soundMeter;
    private Thread thread;
    private QTE.Callback callback;

    public TeacupCoolingQTE(GameView gameView, Drawable teacup) {
        this.gameView = gameView;
        this.teacup = teacup;
    }

    public void setSoundMeter(SoundMeter soundMeter) {
        this.soundMeter = soundMeter;
    }

    public void draw(Canvas canvas) {
        // TODO change teacup img
    }

    public void trigger(QTE.Callback callback) {
        this.callback = callback;
        this.soundMeter.start(gameView._activity);
        gameView.post(this::startTimer);
        thread = new Thread(() -> {
            while (threadRunning) {
                sleepForInterval(500);
                System.out.println("IS BLOWING ? " + soundMeter.isBlowingOnMicrophone());
                // Cool down teacup
            }
        });
        thread.start();
    }

    private void sleepForInterval(int interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isTriggered() {
        return false;
    }

    private void startTimer() {
        new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                threadRunning = true;
            }

            @Override
            public void onFinish() {
                threadRunning = false;
                soundMeter.stop();
                callback.onQTEFinish();
            }
        }.start();
    }

}
