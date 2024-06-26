package com.as.challenge.qte;

import static java.lang.Thread.sleep;

import java.util.Random;

public class QTEHandler {
    private static final int MIN_SLEEP_TIME_MS = 3000;
    private static final int MAX_SLEEP_TIME_MS = 12000;
    private final Callback callback;
    private final Random random;
    private boolean QTEInProgress = false;

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

    private void launchQTE(QTE qte){
        if(qte == null || QTEInProgress) return;
        System.out.println("Launched QTE " + qte.getClass().getName());
        QTEInProgress = true;
        qte.trigger(() -> {
            System.out.println("Stopped QTE " + qte.getClass().getName());
            QTEInProgress = false;
        });
    }

    private void generateRandomEvent() {
        int eventTypeIndex = random.nextInt(EVENT_TYPES.values().length);
        EVENT_TYPES eventType = EVENT_TYPES.values()[eventTypeIndex];

        callback.onNewQTE(eventType);

        switch (eventType) {
            case TEACUP_COOLING:
                launchQTE(callback.launchTeacupCoolingQTE());
                break;
            case DEADLY_ZONES:
                launchQTE(callback.launchDeadlyZoneQTE());
                break;
        }
    }

    public enum EVENT_TYPES {
        TEACUP_COOLING, DEADLY_ZONES
    }

    public interface Callback {
        void onNewQTE(EVENT_TYPES eventType);

        QTE launchTeacupCoolingQTE();

        QTE launchDeadlyZoneQTE();
    }
}
