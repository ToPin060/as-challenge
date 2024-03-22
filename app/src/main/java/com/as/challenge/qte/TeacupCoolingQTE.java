package com.as.challenge.qte;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;

import androidx.core.content.res.ResourcesCompat;

import com.as.challenge.DefeatActivity;
import com.as.challenge.GameView;
import com.as.challenge.R;
import com.as.challenge.SoundMeter;
import com.as.challenge.Teacup;
import com.as.challenge.qte.QTE;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class TeacupCoolingQTE implements QTE {
    private final GameView gameView;
    private Teacup teacup;
    private boolean isTriggered = false;
    private boolean threadRunning = true;
    private SoundMeter soundMeter;
    private Thread thread;
    private QTE.Callback callback;
    private int temperature = 3;

    public TeacupCoolingQTE(GameView gameView, Teacup teacup) {
        this.gameView = gameView;
        this.teacup = teacup;
    }

    public void setSoundMeter(SoundMeter soundMeter) {
        this.soundMeter = soundMeter;
    }

    public void draw(Canvas canvas) {
    }

    public void trigger(QTE.Callback callback) {
        if(teacup == null) return;
        this.callback = callback;
        isTriggered = true;
        teacup.setDrawableResource(R.drawable.teacup_2_hot);
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

    private void loose(){
        Intent gameIntent = new Intent(gameView._activity, DefeatActivity.class);
        gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        gameView._activity.startActivity(gameIntent);
        gameView._activity.finish();
        gameView._activity.finishAffinity();
    }

    private void finishQTE(){
        if(temperature <= 0) {
            teacup.setDrawableResource(R.drawable.teacup_neutral_avg_size);
        }
        threadRunning = false;
        isTriggered = false;
        soundMeter.stop();
        callback.onQTEFinish();
        if(temperature > 0) loose();
        temperature = 3; // reset
    }

    public boolean isTriggered() {
        return isTriggered;
    }

    private void startTimer() {
        new CountDownTimer(20000, 1000) {
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
