package com.as.challenge;

import static java.lang.Thread.sleep;

import java.util.Random;

public class QTEHandler {
    private static final int MIN_SLEEP_TIME_MS = 5000;
    private static final int MAX_SLEEP_TIME_MS = 15000;
    private final Callback callback;
    private final Random random;

    public QTEHandler(Callback callback) {
        this.callback = callback;
        this.random = new Random();

        new Thread(() -> {
            while (true) {
                try {
                    int sleepTime = random.nextInt(MAX_SLEEP_TIME_MS - MIN_SLEEP_TIME_MS + 1) + MIN_SLEEP_TIME_MS;
                    sleep(sleepTime);
                    generateRandomEvent();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void generateRandomEvent() {
        int eventTypeIndex = random.nextInt(EVENT_TYPES.values().length);
        EVENT_TYPES eventType = EVENT_TYPES.values()[eventTypeIndex];

        callback.onNewQTE(eventType);

        switch (eventType) {
            case TEACUP_COOLING:
                callback.launchTeacupCoolingQTE();
                break;
            case STICK_BALANCE:
                callback.launchStickBalanceQTE();
                break;
            case DEADLY_ZONES:
                callback.launchDeadlyZoneQTE();
                break;
        }
    }

    enum EVENT_TYPES {
        TEACUP_COOLING, STICK_BALANCE, DEADLY_ZONES
    }

    public interface Callback {
        void onNewQTE(EVENT_TYPES eventType);

        void launchTeacupCoolingQTE();

        void launchStickBalanceQTE();

        void launchDeadlyZoneQTE();
    }
}
