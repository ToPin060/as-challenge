package com.as.challenge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public interface QTE {
    public void draw(Canvas canvas);
    public void trigger();
    public boolean isTriggered();
}
