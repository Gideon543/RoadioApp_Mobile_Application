package com.example.roadioapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataWritingService extends Service implements SensorEventListener, LocationListener {

    // Declare sensor and location managers for accelerometer and GPS sensors
    private SensorManager sensorManager;
    LocationManager locationManager;
    Location location;

    //Initialize the name of the file to write to
    private final String FILENAME = "accelerometerReadings";

    // Declare axes and GPS points for accelerometer readings and location
    private double xAxis, yAxis, zAxis, latitude, longitude;

    // indicates how to behave if the service is killed
    private int startMode;

    // String
    private String testString;

    //FileOutStream
    private FileOutputStream fos;

    /**
     * Function called when the service is being created
     * **/
    @Override
    public void onCreate() {
        registerAccelerometerSensor();
        try {
            fos = getApplicationContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        };
    }

    /**
     * Function to register accelerometer sensor
     * */
    public void registerAccelerometerSensor(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Function to register GPS location sensor
     * */
    public Location getLocation(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            return  null;
        }
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200000, 10, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            return location;
        }else{
            Toast.makeText(getApplicationContext(), "No GPS Detected", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    /**
     * Function to track and record changes in sensor and location readings
     * */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Accelerometer readings
        xAxis = sensorEvent.values[0];
        yAxis = sensorEvent.values[1];
        zAxis = sensorEvent.values[2];

        // GPS Location readings
        Location location = this.getLocation();
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Prepare data to be written to internal memory
            testString = "" + xAxis + " " + yAxis + " " + zAxis + " " + latitude + " " + longitude + "\n";

            // Write data to internal storage
            try {
                fos.write(testString.getBytes());
                Log.d("Message tag", testString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Function to track changes in GPS location
     * */
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        this.longitude = location.getLongitude();
        this.longitude = location.getLatitude();
    }

    /**
     * Function to start the service when a call to startService() is made
     * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return startMode;
    }

    /**
     * Function to allow client to bind to the service with bindService()
     * */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Function to free up resources when the service stops running
     * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Override and not-so-useful functions
     * */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
