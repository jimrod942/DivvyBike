package com.example.divvybike;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.lib.Station;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoadingActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    public List<Station> stations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.divvy_blue));
        }

        requestQueue = Volley.newRequestQueue(this);

        new Tasks.GetJsonTask(LoadingActivity.this, requestQueue).execute();
    }

    protected void finishGetJson(final String jsonResult) {
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(jsonResult).getAsJsonObject();
        JsonArray stationBeanList = result.get("stationBeanList").getAsJsonArray();
        for (int i = 0; i < stationBeanList.size(); i++) {
            String name = stationBeanList.get(i).getAsJsonObject().get("stationName").getAsString();
            int docks = stationBeanList.get(i).getAsJsonObject().get("availableDocks").getAsInt();
            int bikes = stationBeanList.get(i).getAsJsonObject().get("availableBikes").getAsInt();
            String status = stationBeanList.get(i).getAsJsonObject().get("statusValue").getAsString();
            double latitude = stationBeanList.get(i).getAsJsonObject().get("latitude").getAsDouble();
            double longitude = stationBeanList.get(i).getAsJsonObject().get("longitude").getAsDouble();
            stations.add(new Station(name, docks, bikes, status, latitude, longitude));
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent startNewActivity = new Intent(LoadingActivity.this, MainActivity.class);
        startNewActivity.putExtra("STATIONS", (Serializable) stations);
        LoadingActivity.this.startActivity(startNewActivity);
    }
}
