package com.example.roadioapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * Reads data from file stored in the internal memory
 * Also, performs feature extraction to get the Z-Values over a 10-second window
 */
public class DataReadingService extends Service {
    // Initialize the name of the file to read from
    private final String FILENAME = "accelerometerReadings";

    private int counter;

    private int startMode;

    // Initialize the class perform the feature extraction service
    FeatureExtractionService extractionService = new FeatureExtractionService();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Function reads data from internal memory at the moment
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Reprogramming the the code to read from a file every 10 seconds
        FileInputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput(this.FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
                count++;

                if (count == 20) {
                    Log.d("Readings", "" + lines.size());
                    computeZValuesOverAWindow(lines);
                    lines = new ArrayList<>();
                    count = 0;
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        finally {
//            Log.d("Count", "" + counter);
//            Log.d("Readings", "" + lines);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Computes the Z-Values over a 10-second window
     * @param arr
     */
    public void computeZValuesOverAWindow(ArrayList<String> arr){

        ArrayList<Double> z_values = new ArrayList<>();
        double final_latitude = 0;
        double final_longitude = 0;

        for (String item: arr){
            String[] item_contents = item.split(" ");

            // Get x-axis readings
            double x_axis = Double.parseDouble(item_contents[0]);

            // Get y-axis readings
            double y_axis = Double.parseDouble(item_contents[1]);

            // Get z-axis readings
            double z_axis = Double.parseDouble(item_contents[2]);

            // Get latitude readings
            double latitude = Double.parseDouble(item_contents[3]);

            // Get longitude readings
            double longitude = Double.parseDouble(item_contents[4]);

            // Add z-axis to the Z values list
            z_values.add(z_axis);

            // Update longitude and latitude values with the last record in the window
            final_latitude = latitude;
            final_longitude = longitude;
        }

        // Compute the Z-statistics
        double z_mean = extractionService.computeZMean(z_values);
        double z_variance = extractionService.computeZVariance(z_values);
        double z_sdv = extractionService.computeZSDV(z_values);
        double z_peak = extractionService.computeZHigh(z_values);
        double z_low = extractionService.computeZLow(z_values);

        //Testing to see if it worked
        Log.d("Readings", "" + z_mean + " " + z_variance + " " + z_sdv + " " + z_low + " " + z_peak + " " + final_latitude + " " + final_longitude );
    }

    /**
     * Function to write accelerometer and GPS readings to internal storage
     * */
    public void writeFileOnInternalStorage(String sBody) {
        String filename = this.FILENAME;
        try (FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(sBody.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
