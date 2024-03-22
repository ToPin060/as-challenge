package com.as.challenge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public interface QTE {
    public interface Callback {
        void onQTEFinish();
    }
    public void draw(Canvas canvas);
    public void trigger(QTE.Callback callback);
    public boolean isTriggered();
}
