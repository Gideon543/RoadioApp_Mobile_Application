package com.example.roadioapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartRecordFragment} factory method to
 * create an instance of this fragment.
 */
public class StartRecordFragment extends Fragment implements View.OnClickListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    // Declare component variables in the UI
    private MaterialButton recordBtn;
    private Chronometer timer;
    private TextView clickText;

    // Declare sensor manager variable
    private SensorManager sensorManager;

    // Initialize access to configurations for the fragment
    Configuration configuration = new Configuration();

    // Initialize variable to track when the application is recording
    private boolean isRecording = false;

    // Create an intents
    private Intent dataWritingServiceIntent;
    private Intent dataReadingServiceIntent;

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

        //Request access to location
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        //Request access to vibration
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.VIBRATE}, 224);

        //Initialize intents to use both the write and read services
        dataWritingServiceIntent = new Intent(getContext(), DataWritingService.class);
        dataReadingServiceIntent = new Intent(getContext(), DataReadingService.class);

        //Set an event listener for the recording button
        recordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recordButton:
                if (isRecording) {
                    // Stop recording
                    recordBtn.setBackgroundColor(Color.parseColor("#FD009688"));
                    configuration.stopTimer(timer);
                    getActivity().stopService(dataWritingServiceIntent);
                    getActivity().startService(dataReadingServiceIntent );
                    interactWithCreateAPI();
                    isRecording = false;

                } else {
                    // Start recording
                    getActivity().stopService(dataReadingServiceIntent );
                    getActivity().startService(dataWritingServiceIntent);
                    configuration.startTimer(timer);
                    recordBtn.setBackgroundColor(Color.RED);
                    isRecording = true;
                }
                break;
        }
    }

    private void interactWithCreateAPI() {
        //if validation passes
        HashMap<String, String> params = new HashMap<>();
        params.put("accelerometer_datafile", "Hello World");

        //Calling the create API
        MakeNetworkRequest request = new MakeNetworkRequest(API_URLs.URL_CREATE, params, CODE_POST_REQUEST, getContext());
        String response = request.performBackgroundRequest();
        request.onPostExecute(response);

    }
}



