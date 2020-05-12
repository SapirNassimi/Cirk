package com.example.cirk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.SystemClock;
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
    private int tappedCirclesCount;
    private long startTime;
    SharedPreferences prefs;

    // TODO: add 2 red obstacle circles
    // TODO: move all circles

    public GameView(Context context, int displayWidth, int displayHeight) {
        super(context);

        prefs = context.getSharedPreferences("game", Context.MODE_PRIVATE);

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
        tappedCirclesCount = 0;
        startTime = SystemClock.uptimeMillis();
        resume();
    }

    @Override
    public void run() {
        while (isPlaying) {
            draw();

            if (strikes > 0) {
                update();
                controlGame();
            } else {
                long totalGameTime = SystemClock.uptimeMillis() - startTime;
                long gameAverageTime = totalGameTime / tappedCirclesCount;
                long lastHighscore = prefs.getLong("averageTapTime", 0);

                if (lastHighscore > gameAverageTime || lastHighscore == 0) {
                    saveHighscore(gameAverageTime, tappedCirclesCount, totalGameTime);
                }

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

            drawStrikes();
            drawCounter();
            drawTimer();
            drawCircles();

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void controlGame() {
        try {
            gameThread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private Boolean didUserTouchTheCircle(float touchPositionX, float touchPositionY) {
        double xPart = Math.pow((double)(touchPositionX - circle.getLocation().x), 2);
        double yPart = Math.pow((double)(touchPositionY - circle.getLocation().y), 2);
        double equalsTo = Math.pow((double)(circle.getRadius()), 2);

        return (xPart + yPart) < equalsTo;
    }

    private String timeSinceGameStarted() {
        long currentTime = SystemClock.uptimeMillis() - startTime;
        int seconds = (int)(currentTime / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        int milliSeconds = (int)(currentTime % 1000);

        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ":" + String.format("%03d", milliSeconds);
    }

    private void drawCircles() {
        paint.setColor(circle.getColor());

        canvas.drawCircle(circle.getLocation().x,
                circle.getLocation().y,
                circle.getRadius(),
                paint);
    }

    private void drawStrikes() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Strikes: " + strikes, 50, 135, paint);
    }

    private void drawCounter() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(110);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(tappedCirclesCount + "", borders.x / 2, 150, paint);
    }

    private void drawTimer() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(timeSinceGameStarted(), borders.x - 350, 135, paint);
    }

    private void saveHighscore(long avgTapTime, int count, long totalTime) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("averageTapTime", avgTapTime);
        editor.putInt("cirks", count);
        editor.putLong("totalTime", totalTime);
        editor.apply();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (didUserTouchTheCircle(motionEvent.getX(), motionEvent.getY())) {
                tappedCirclesCount++;
                circle.generateRandomCircle();
            } else {
                strikes--;
            }
        }

        return true;
    }
}