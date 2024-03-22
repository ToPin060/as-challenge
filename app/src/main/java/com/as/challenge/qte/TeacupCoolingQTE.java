package com.as.challenge.qte;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;

import com.as.challenge.GameView;
import com.as.challenge.SoundMeter;
import com.as.challenge.qte.QTE;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class TeacupCoolingQTE implements QTE {
    private final GameView gameView;
    private final Drawable teacup;
    private boolean isTriggered = false;
    private boolean threadRunning = true;
    private SoundMeter soundMeter;
    private Thread thread;
    private QTE.Callback callback;
    private int temperature = 5; // TODO MAP EACH TEMPERATURE TO AN IMAGE OF TEACUP

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
        Queue<Boolean> values = new ArrayDeque<>(4);
        thread = new Thread(() -> {
            while (threadRunning) {
                System.out.println("TEACUP TEMPERATURE : " + temperature);
                sleepForInterval(1000);
                boolean isBlowing = soundMeter.isBlowingOnMicrophone();
                System.out.println("IS BLOWING ? " + isBlowing);
                if(values.size() == 4) values.poll();
                values.offer(isBlowing);

                if(values.stream().anyMatch(Boolean::booleanValue)) {
                    temperature--;
                    if(temperature == 0) finishQTE();
                }
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

    private void finishQTE(){
        // TODO CHECK IF TEACUP STILL HOT do something
        threadRunning = false;
        soundMeter.stop();
        callback.onQTEFinish();
    }

    public boolean isTriggered() {
        return false;
    }

    private void startTimer() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                threadRunning = true;
            }

            @Override
            public void onFinish() {
                if(threadRunning) finishQTE();
            }
        }.start();
    }

}
