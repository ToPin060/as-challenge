package com.as.challenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class Teacup extends Drawable {
    private Drawable mDrawable;
    private int _xTeacup = 200;
    private int _yTeacup = 200;
    private Context context;

    public Teacup(Context context, int resourceId) {
        this.context = context;
        mDrawable = ResourcesCompat.getDrawable(context.getResources(), resourceId, null);
        setupCoords();
    }

    public void setDrawableResource(int resourceId) {
        _xTeacup = mDrawable.getBounds().centerX();
        _yTeacup = mDrawable.getBounds().centerY();
        mDrawable = ResourcesCompat.getDrawable(context.getResources(), resourceId, null);
        setupCoords();
        invalidateSelf(); // Trigger redraw
    }

    public void setupCoords() {
        mDrawable.setBounds(_xTeacup - mDrawable.getIntrinsicWidth() / 2,
            _yTeacup - mDrawable.getIntrinsicHeight() / 2,
            _xTeacup + mDrawable.getIntrinsicWidth() / 2,
            _yTeacup + mDrawable.getIntrinsicHeight() / 2);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
