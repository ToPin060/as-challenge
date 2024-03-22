package com.as.challenge.qte;

import android.graphics.Canvas;

public interface QTE {
    public interface Callback {
        void onQTEFinish();
    }
    public void draw(Canvas canvas);
    public void trigger(QTE.Callback callback);
    public boolean isTriggered();
}
