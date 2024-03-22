package com.as.challenge;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;

public class DeadlyZoneQTE {
    private boolean drawZone = false;
    private GameView gameView;
    private final int flashInterval = 300; // Time between flashes in milliseconds
    private Canvas canvas;
    private boolean isTriggered = false;
    private boolean isTimerRunning = false; // Variable to track timer status
    private CountDownTimer countDownTimer; // Timer object

    public boolean isTriggered() {
        return isTriggered;
    }

    public DeadlyZoneQTE(GameView gameView) {
        this.gameView = gameView;
        this.canvas = canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void draw(Canvas canvas){
        if(!isTimerRunning) return;
        Paint paint = new Paint();
        paint.setColor(Color.rgb(250, 0, 0));

        // Choose left or right zone based on logic (replace with your logic)
        boolean isLeftZone = true; // Replace with your condition to determine zone side
        if (drawZone && isLeftZone) {
            canvas.drawRect(0, (float) gameView.getHeight() / 2, (float) gameView.getWidth() / 2, gameView.getHeight(), paint);
        } else if(drawZone){
            canvas.drawRect((float) gameView.getWidth() / 2, (float) gameView.getHeight() / 2, gameView.getWidth(), gameView.getHeight(), paint);
        }
    }

    public void trigger() {
        this.isTriggered = true;
        gameView.post(() -> startTimer());
        new Thread(() -> {

            // Flash for a duration with a small interval between flashes
            for (int i = 0; i < 5; i++) { // Flash 5 times
                this.drawZone = true;
                gameView.postInvalidate(); // Trigger redraw
                try {
                    Thread.sleep(flashInterval); // Wait before next flash
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Clear the canvas after each flash to create the blinking effect
                this.drawZone = false;
                gameView.postInvalidate();
                try {
                    Thread.sleep(flashInterval); // Wait before next clear
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(5000, 1000) { // 5 seconds with 1 second interval
            @Override
            public void onTick(long millisUntilFinished) {
                // Timer is running, set the variable to true
                isTimerRunning = true;
            }

            @Override
            public void onFinish() {
                // Timer finished, set the variable to false
                isTimerRunning = false;
            }
        };

        // Start the timer
        countDownTimer.start();
    }
}
