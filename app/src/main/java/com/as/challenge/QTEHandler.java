package com.as.challenge;

import static java.lang.Thread.sleep;

import java.util.Random;

public class QTEHandler {
    enum EVENT_TYPES {
        CUP_COOLING, STICK_BALANCE
    }

    public interface Callback {
        void launchCupCoolingQTE();
        void launchStickBalanceQTE();
    }

    private Callback callback;
    private Random random;
    private static final int MIN_SLEEP_TIME_MS = 10000;
    private static final int MAX_SLEEP_TIME_MS = 30000;

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

        switch (eventType) {
            case CUP_COOLING:
                callback.launchCupCoolingQTE();
                break;
            case STICK_BALANCE:
                callback.launchStickBalanceQTE();
                break;
        }
    }
}
