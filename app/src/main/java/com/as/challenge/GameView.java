package com.as.challenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.as.challenge.qte.DeadlyZoneQTE;
import com.as.challenge.qte.QTE;
import com.as.challenge.qte.TeacupCoolingQTE;

import java.util.ArrayList;


import java.lang.ref.WeakReference;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private class LoadResourcesTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContextRef;
        private Drawable mTouilletteDrawable;
        private Drawable mTeaCupDrawable;
        private Drawable mTeaCupTouilletteInteraction;


        public LoadResourcesTask(Context context) {
            mContextRef = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Context context = mContextRef.get();
            if (context != null) {
                mTeaCupTouilletteInteraction = ResourcesCompat.getDrawable(context.getResources(), R.drawable.emoji_grimacing, null);
                mTouilletteDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.touillette_neutral, null);
                mTeaCupDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.teacup_neutral_xxhdpi, null);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            GameView gameView = GameView.this;
            if (gameView != null) {
                gameView.setTouilletteDrawable(mTouilletteDrawable);
                gameView.setTeaCupDrawable(mTeaCupDrawable);
                gameView.setTouilletteAndteacupInteractionDrawable(mTeaCupTouilletteInteraction);
            }
        }
    }

    public ArrayList<QTE> QTEs = new ArrayList<>();
    public final GameActivity _activity;
    private final GameThread _thread;

    private Drawable _touillette;
    private Drawable _teacup;

    private Context _context;
    public Random random = new Random();

    private Drawable _touilletteAndteacupInteraction;

    private int _x;
    private int _y;
    private int _xTou = 300;
    private int _yTou = 800;
    private int _xTeacup = 200;
    private int _yTeacup = 200;
    private boolean _isTouching = false;
    private int _xTeaCup;
    private int _yTeaCup;
    private int[][] _teacupLocations = {{200, 200}, {500, 1000}, {200, 1000}, {500, 200}};
    public int[] location;
    private boolean _touchingTouillette = false;
    private boolean _insideTeaCup = false;
    private boolean _interaction = false;

    public GameView(Context context_) {
        super(context_);
        getHolder().addCallback(this);
        _context = context_;
        location = _teacupLocations[random.nextInt(_teacupLocations.length)];

        _activity = (GameActivity) context_;
        _thread = new GameThread(getHolder(), this);


        setTouillette(context_, R.drawable.touillette_neutral);
        setTouilletteCoords(_xTou, _yTou);
        new LoadResourcesTask(_context).execute();

        //initDrawings();

        setTeaCup(context_, R.drawable.teacup_neutral_xxhdpi);
        setTeacupCoords(_xTeacup, _yTeacup);

        this.QTEs.add(new DeadlyZoneQTE(this, _touillette));
        this.QTEs.add(new TeacupCoolingQTE(this, _teacup));

        setOnTouchListener(this);
        setFocusable(true);
    }

    public void setTouillette(Context context_, int ressource_) {
        _touillette = ResourcesCompat.getDrawable(context_.getResources(), ressource_, null);
    }

    public void setTeaCup(Context context_, int ressource_) {
        _teacup = ResourcesCompat.getDrawable(context_.getResources(), ressource_, null);
    }

    public void setTeacupCoords(int x_, int y_) {

        _xTeacup = x_;
        _yTeacup = y_;

        _teacup.setBounds(x_ - _teacup.getIntrinsicWidth() / 2,
                y_ - _teacup.getIntrinsicHeight() / 2,
                x_ + _teacup.getIntrinsicWidth() / 2,
                y_ + _teacup.getIntrinsicHeight() / 2);
    }

    public void setTouilletteCoords(int x_, int y_) {
        _xTou = x_;
        _yTou = y_;

        _touillette.setBounds(x_ - _touillette.getIntrinsicWidth() / 2,
                y_ - _touillette.getIntrinsicHeight() / 2,
                x_ + _touillette.getIntrinsicWidth() / 2,
                y_ + _touillette.getIntrinsicHeight() / 2);
    }

    public void initDrawings() {
        setTouillette(_context, R.drawable.touillette_neutral);
        setTeaCup(_context, R.drawable.teacup_neutral_xxhdpi);

    }

    public void setElementCoord(Drawable element_, int x_, int y_) {
        element_.setBounds(x_ - element_.getIntrinsicWidth() / 2,
                y_ - element_.getIntrinsicHeight() / 2,
                x_ + element_.getIntrinsicWidth() / 2,
                y_ + element_.getIntrinsicHeight() / 2);
    }

    public void setTouilletteDrawable(Drawable d) {
        _touillette = d;
    }

    public void setTeaCupDrawable(Drawable d) {
        _teacup = d;
    }

    public void setTeaCupCoords(int x_, int y_) {

        _xTeaCup = x_;
        _yTeaCup = y_;

        _teacup.setBounds(x_ - _teacup.getIntrinsicWidth() / 2,
                y_ - _teacup.getIntrinsicHeight() / 2,
                x_ + _teacup.getIntrinsicWidth() / 2,
                y_ + _teacup.getIntrinsicHeight() / 2);
    }

    public void setTouilletteAndteacupInteractionDrawable(Drawable d) {
        _touilletteAndteacupInteraction = d;
    }

    public void setTouilletteAndteacupInteractionCoords(int x_, int y_) {

        _teacup.setBounds(x_ - _teacup.getIntrinsicWidth() / 2,
                y_ - _teacup.getIntrinsicHeight() / 2,
                x_ + _teacup.getIntrinsicWidth() / 2,
                y_ + _teacup.getIntrinsicHeight() / 2);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        _thread.setRunning(true);
        _thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                _thread.setRunning(false);
                _thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    private void handleQTEs(Canvas canvas) {
        this.QTEs.forEach(qte -> {
            if (qte.isTriggered()) qte.draw(canvas);
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);

            handleQTEs(canvas);

            Drawable d = ResourcesCompat.getDrawable(_context.getResources(), R.drawable.emoji_grimacing, null);
            setTouilletteAndteacupInteractionDrawable(d);

            setTouilletteCoords(_xTou, _yTou);
            _touillette.draw(canvas);
            _teacup.draw(canvas);


            setTeaCupCoords(location[0], location[1]);
            //setTeaCupCoords(200, 700);
            _teacup.draw(canvas);

            setTouilletteAndteacupInteractionCoords(300, 300);
            _touilletteAndteacupInteraction.draw(canvas);

            _touchingTouillette = (_xTou - _touillette.getIntrinsicWidth() / 2) < _x &&
                    (_xTou + _touillette.getIntrinsicWidth() / 2) > _x &&
                    (_yTou - _touillette.getIntrinsicHeight() / 2) < _y &&
                    (_yTou + _touillette.getIntrinsicHeight() / 2) > _y;

            _insideTeaCup = (_xTeaCup - _teacup.getIntrinsicWidth() / 2) < _x &&
                    (_xTeaCup + _teacup.getIntrinsicWidth() / 2) > _x &&
                    (_yTeaCup - _teacup.getIntrinsicHeight() / 2) < _y &&
                    (_yTeaCup + _teacup.getIntrinsicHeight() / 2) > _y;

            if (_isTouching) {
                setTouilletteCoords(_x, _y);
                setTouilletteCoords(_x, _y);

            }

            if (_interaction) {
                setTouilletteAndteacupInteractionCoords(_xTeaCup, _yTeaCup);
                _touilletteAndteacupInteraction.draw(canvas);
                _touillette.setVisible(false, false);
                _teacup.setVisible(false, false);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                _x = (int) event.getX();
                _y = (int) event.getY();
                if (_touchingTouillette) {
                    _isTouching = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                _isTouching = false;

                if (_insideTeaCup) {
                    _interaction = true;
                }

                if (_xTou <= _touillette.getBounds().width() && _yTou <= _touillette.getBounds().height()) {
                    _x = 500;
                    _y = 500;
                } else {
                    _x = 0;
                    _y = 0;
                }


                break;
        }
        return true;
    }

    public void update() {
        if (_isTouching) {
            //DEBUG
        }
    }
}




