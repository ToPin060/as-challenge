package com.as.challenge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;

import java.util.Random;

public class DeadlyZoneQTE {
    private final int flashInterval = 300; // Time between flashes in milliseconds
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
        Paint paint = new Paint();
        paint.setColor(Color.rgb(250, 0, 0));

        if (drawZone || isTimerRunning) {
            canvas.drawRect(getZoneBox(), paint);
        }
    }

    public Rect getZoneBox() {
        if(isLeftZone) return new Rect(0, gameView.getHeight() / 2, gameView.getWidth() / 2, gameView.getHeight());
        else return new Rect(gameView.getWidth() / 2, gameView.getHeight() / 2, gameView.getWidth(), gameView.getHeight());
    }

    public void trigger() {
        this.isTriggered = true;
        isLeftZone = rd.nextBoolean();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                this.drawZone = true;
                gameView.postInvalidate();
                try {
                    Thread.sleep(flashInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.drawZone = false;
                gameView.postInvalidate();
                try {
                    Thread.sleep(flashInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameView.post(() -> startTimer());
            maybeKillTouillette();
        }).start();

    }

    private boolean maybeKillTouillette() {
        // TODO KILL TOUILLETTE
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
