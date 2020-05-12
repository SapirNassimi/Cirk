package com.example.cirk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView buttonPlay;
    SharedPreferences prefs;

    // TODO: add game over screen
    // TODO: make app compatible for all screen sizes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prefs = this.getSharedPreferences("game", Context.MODE_PRIVATE);

        buttonPlay = findViewById(R.id.startGame);
        buttonPlay.setOnClickListener(this);

        updateHighscores();
    }

    private void updateHighscores() {
        TextView avgTapTime = findViewById(R.id.avgTapTimeHighscore);
        TextView cirks = findViewById(R.id.circksHighscore);
        TextView totalTime = findViewById(R.id.totalTimeHighscore);

        avgTapTime.setText("Average Seconds Per Tap: " + getTimeAsString(prefs.getLong("averageTapTime", 0), true));
        cirks.setText("Cirks: " + prefs.getInt("cirks", 0));
        totalTime.setText("Total Time: " + getTimeAsString(prefs.getLong("totalTime", 0), false));
    }

    private String getTimeAsString(long time, Boolean isAvg) {
        String returnString = "";

        int seconds = (int)(time / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        int milliSeconds = (int)(time % 1000);

        if (isAvg) {
            if (minutes != 0) {
                returnString = String.format("%02d", minutes) +
                        ":" +
                        String.format("%02d", seconds) +
                        ":" +
                        String.format("%03d", milliSeconds);
            } else {
                returnString = String.format("%01d", seconds) + "." + String.format("%03d", milliSeconds);
            }
        } else {
            if (minutes != 0) {
                returnString = String.format("%02d", minutes) + ":";
            }

            returnString += String.format("%02d", seconds) + ":" + String.format("%03d", milliSeconds);
        }

        return  returnString;
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }
}