package com.as.challenge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    public DeadlyZoneQTE(GameView gameView) {
        this.gameView = gameView;
        rd = new Random();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(250, 0, 0));

        if (isLeftZone && (drawZone || isTimerRunning)) {
            canvas.drawRect(0, (float) gameView.getHeight() / 2, (float) gameView.getWidth() / 2, gameView.getHeight(), paint);
        } else if (drawZone || isTimerRunning) {
            canvas.drawRect((float) gameView.getWidth() / 2, (float) gameView.getHeight() / 2, gameView.getWidth(), gameView.getHeight(), paint);
        }
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
        }).start();

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
