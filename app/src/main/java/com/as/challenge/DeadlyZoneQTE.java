package com.as.challenge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;

import java.util.Random;

public class DeadlyZoneQTE {
    private static final int FLASH_INTERVAL = 300; // Time between flashes in milliseconds
    boolean isLeftZone;
    private boolean drawZone = false;
    private final GameView gameView;
    private boolean isTriggered = false;
    private boolean isTimerRunning = false; // Variable to track timer status
    private CountDownTimer countDownTimer; // Timer object
    private final Random rd;
    private Drawable touillette;

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

    public void trigger() {
        this.isTriggered = true;
        isLeftZone = rd.nextBoolean();
        new Thread(() -> {
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
        }).start();
    }

    private boolean maybeKillTouillette() {
        // TODO KILL TOUILLETTE / GAME OVER
       return getZoneBox().contains(touillette.getBounds().centerX(), touillette.getBounds().centerY());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
            }
        };
        countDownTimer.start();
    }

    public boolean isTriggered() {
        return isTriggered;
    }
}
