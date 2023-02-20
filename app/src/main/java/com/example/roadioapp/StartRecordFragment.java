package com.example.roadioapp;

import android.graphics.Color;
import android.hardware.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

import java.io.FileNotFoundException;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartRecordFragment} factory method to
 * create an instance of this fragment.
 */
public class StartRecordFragment extends Fragment implements SensorEventListener {

    // Declare component variables in the UI
    private MaterialButton recordBtn;
    private Chronometer timer;
    private TextView clickText;

    // Initialize access to configurations for the fragment
    StartRecordConfiguration configuration = new StartRecordConfiguration();

    // Initialize variable to track when the application is recording
    private boolean isRecording = false;

    // Declare sensor variables
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    // Declare x, y, z variables for accelerometer readings
    float x, y, z;
    double [] linear_acceleration = new double[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize UI Components
        recordBtn = view.findViewById(R.id.recordButton);
        timer = view.findViewById(R.id.record_timer);
        clickText = view.findViewById(R.id.page_descrp);

        // Initialize sensor objects
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        clickText.append("Idiot");

        //Set an event listener for the recording button
//        recordBtn.setOnClickListener(this);
    }


    //    StringBuilder sb = new StringBuilder();
    String sb;

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.recordButton:
//                if (isRecording) {
//                    // Stop recording
//                    recordBtn.setBackgroundColor(Color.parseColor("#FD009688"));
//                    configuration.stopTimer(timer);
//                    try {
//                        configuration.readFileFromInternalStorage(getContext(), getActivity(), "accelerometerReadings.txt", clickText);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    isRecording = false;
//
//                } else {
//                    // Start recording
//                    if (configuration.checkPermission(getContext(), getActivity())) {
//                        configuration.startTimer(timer);
//                        recordBtn.setBackgroundColor(Color.RED);
//
//                        isRecording = true;
//                    }
//                }
//                break;
//        }
//    }

    mSensorManager.registerEventListener();

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0] ;
        linear_acceleration[1] = event.values[1] ;
        linear_acceleration[2] = event.values[2] ;
        clickText.append("x");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}


