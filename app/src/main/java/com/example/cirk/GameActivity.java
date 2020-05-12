package com.example.cirk;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);

        gameView = new GameView(this, displaySize.x, displaySize.y);

        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //gameView.resume();
    }
}