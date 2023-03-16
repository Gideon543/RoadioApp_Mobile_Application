package com.example.roadioapp;

import android.os.SystemClock;
import android.widget.Chronometer;

public class Configuration {

    // Initialize variable to track when the timer stops
    private long timeWhenStopped;

    /**
     * Configure chronometer when timer stop
     * @param timer
     */
    public void stopTimer(Chronometer timer){
        timer.stop();
        timeWhenStopped = timer.getBase() - SystemClock.elapsedRealtime();
    }

    /**
     * Configure chronometer when timer starts
     * @param timer
     */
    public void startTimer(Chronometer timer){
        timer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        timer.start();
    }
}
