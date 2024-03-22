package com.as.challenge.qte;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;

import com.as.challenge.DefeatActivity;
import com.as.challenge.GameActivity;
import com.as.challenge.GameView;

import java.util.Random;

public class DeadlyZoneQTE implements QTE {
    private static final int FLASH_INTERVAL = 300; // Time between flashes in milliseconds
    private final GameView gameView;
    private final Random rd;
    private boolean isLeftZone;
    private boolean drawZone = false;
    private boolean isTriggered = false;
    private boolean isTimerRunning = false; // Variable to track timer status
    private final Drawable touillette;
    private Thread thread;
    private QTE.Callback callback;

    public DeadlyZoneQTE(GameView gameView, Drawable touillette) {
        this.gameView = gameView;
        rd = new Random();
        this.touillette = touillette;
    }

    public void draw(Canvas canvas) {
        if (drawZone || isTimerRunning) {
            Paint paint = new Paint();
            paint.setColor(Color.rgb(250, 0, 0));
            canvas.drawRect(getZoneBox(), paint);
        }
    }

    public Rect getZoneBox() {
        int width = gameView.getWidth();
        int height = gameView.getHeight();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        if (isLeftZone) return new Rect(0, halfHeight, halfWidth, height);
        return new Rect(halfWidth, halfHeight, width, height);
    }

    private void sleepForInterval() {
        try {
            Thread.sleep(FLASH_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void trigger(QTE.Callback callback) {
        this.callback = callback;
        this.isTriggered = true;
        isLeftZone = rd.nextBoolean();

        thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                drawZone = true;
                gameView.postInvalidate();
                sleepForInterval();
                drawZone = false;
                gameView.postInvalidate();
                sleepForInterval();
            }
            gameView.post(() -> startTimer());
            maybeKillTouillette();
        });

        thread.start();
    }

    private void maybeKillTouillette() {
        if(getZoneBox().contains(touillette.getBounds().centerX(), touillette.getBounds().centerY())) {
            Intent gameIntent = new Intent(gameView._activity, DefeatActivity.class);
            gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            gameView._activity.startActivity(gameIntent);
            gameView._activity.finish();
            gameView._activity.finishAffinity();
        }
    }

    private void startTimer() {
        new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                callback.onQTEFinish();
            }
        }.start();
    }

    public boolean isTriggered() {
        return isTriggered;
    }
}
