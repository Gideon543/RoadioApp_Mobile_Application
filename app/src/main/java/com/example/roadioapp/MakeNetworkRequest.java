package com.example.roadioapp;


import android.content.Context;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MakeNetworkRequest {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    //the url where we need to send the request
    String url;

    //the parameters
    HashMap<String, String> params;

    //the request code to define whether it is a GET or POST
    int requestCode;

    //context from application
    Context context;

    //constructor to initialize values
    MakeNetworkRequest(String url, HashMap<String, String> params, int requestCode, Context context) {
        this.url = url;
        this.params = params;
        this.requestCode = requestCode;
        this.context = context;
    }

    protected void onPostExecute(String s) {
        try {
            JSONObject object = new JSONObject(s);
            if (!object.getBoolean("error")) {
                Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected String performBackgroundRequest(){
        RequestHandler requestHandler = new RequestHandler();
        if (requestCode == CODE_POST_REQUEST)
            return requestHandler.sendPostRequest(url, params);
        return null;
    }
}
