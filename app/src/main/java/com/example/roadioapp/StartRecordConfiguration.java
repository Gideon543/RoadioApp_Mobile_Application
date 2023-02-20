package com.example.roadioapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StartRecordConfiguration {
    // Initialize Permission Variables
    private String recordPermission = Manifest.permission.VIBRATE;
    private int PERMISSION_CODE = 22;

    // Initialize variable to track when the timer stops
    private long timeWhenStopped;

    // Check Recording Permission
    public boolean checkPermission(Context context, Activity activity){
        if(ActivityCompat.checkSelfPermission(context, recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    // Configure chronometer when timer stops
    public void stopTimer(Chronometer timer){
        timer.stop();
        timeWhenStopped = timer.getBase() - SystemClock.elapsedRealtime();
    }

    // Configure chronometer when timer starts
    public void startTimer(Chronometer timer){
        timer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        timer.start();
    }

    // Write accelerometer readings to internal storage
    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        try (FileOutputStream fos = mcoContext.openFileOutput(sFileName, Context.MODE_PRIVATE)) {
            fos.write(sBody.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read accelerometer readings from internal storage
    public void readFileFromInternalStorage(Context mcoContext, Activity activity, String sFileName, TextView view) throws FileNotFoundException {
//        FileInputStream fis = mcoContext.openFileInput(sFileName);
//        InputStreamReader isr = new InputStreamReader(fis);
//        BufferedReader bufferedReader = new BufferedReader(isr);
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while (true) {
//            try {
//                if (!((line = bufferedReader.readLine()) != null)) break;
//                sb.append(line);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return sb;

        FileInputStream fis = mcoContext.openFileInput(sFileName);
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        String contents;
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();


            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            contents = stringBuilder.toString();
            view.append(contents);

//           Toast.makeText(activity.getBaseContext(), contents , Toast.LENGTH_SHORT ).show();
        }
//        view.append(contents);

    }
}
