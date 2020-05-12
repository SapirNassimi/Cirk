package com.example.cirk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

public class GameView extends SurfaceView implements Runnable {
    private Point borders;
    volatile boolean isPlaying;
    private Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Circle circle;
    private int strikes;
    private int pressedCirclesCount;

    // TODO: add 2 red obstacle circles
    // TODO: move all circles

    public GameView(Context context, int displayWidth, int displayHeight) {
        super(context);

        surfaceHolder = getHolder();
        paint = new Paint();

        Typeface typeface = ResourcesCompat.getFont(context, R.font.expletus_sans);
        paint.setTypeface(typeface);

        borders = new Point(displayWidth, displayHeight);

        circle = new Circle(new Point(displayWidth, displayHeight));
        circle.generateRandomCircle();

        startNewGame();
    }

    private void startNewGame() {
        strikes = 3;
        pressedCirclesCount = 0;
        resume();
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (strikes > 0) {
                update();
                draw();
                controlGame();
            } else {
                GameActivity activity = (GameActivity)getContext();
                activity.finish();
            }
        }
    }

    private void update() {

    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);

            paint.setTextSize(60);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Strikes: " + strikes, 50, 135, paint);

            // TODO: add timer

            paint.setTextSize(110);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(pressedCirclesCount + "", borders.x / 2, 150, paint);

            paint.setColor(circle.getColor());

            canvas.drawCircle(circle.getLocation().x,
                    circle.getLocation().y,
                    circle.getRadius(),
                    paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void controlGame() {
//        try {
//            gameThread.sleep(17);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void pause() {
        isPlaying = false;

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (didUserTouchTheCircle(motionEvent.getX(), motionEvent.getY())) {
                pressedCirclesCount++;
                circle.generateRandomCircle();
            } else {
                strikes--;
            }
        }

        return true;
    }

    private Boolean didUserTouchTheCircle(float touchPositionX, float touchPositionY) {
        double xPart = Math.pow((double)(touchPositionX - circle.getLocation().x), 2);
        double yPart = Math.pow((double)(touchPositionY - circle.getLocation().y), 2);
        double equalsTo = Math.pow((double)(circle.getRadius()), 2);

        return (xPart + yPart) < equalsTo;
    }
}