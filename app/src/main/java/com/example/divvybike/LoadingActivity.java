package com.example.divvybike;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.lib.Station;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoadingActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private double currentLongitude;
    private double currentLatitude;

    /**
     * Not exactly sure what this does.
     */
    private LocationCallback locationCallback;

    private double[] currentLocation = new double[2];

    /**
     * Whether we have received locations.
     */
    private boolean receivedLocation = false;

    private FusedLocationProviderClient fusedLocationClient;

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

        ActivityCompat.requestPermissions(LoadingActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fetchLocation();

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
            stations.get(i).setDistance(currentLongitude, currentLatitude);
        }

        Collections.sort(stations, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                return (int) (o1.getDistance()*1000 - o2.getDistance()*1000);
            }
        });

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent startNewActivity = new Intent(LoadingActivity.this, MainActivity.class);
        startNewActivity.putExtra("STATIONS", (Serializable) stations);
        LoadingActivity.this.startActivity(startNewActivity);
    }

    private void fetchLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoadingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoadingActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this).setTitle("Location Permission Required")
                        .setMessage("Location permission is required to access this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoadingActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(LoadingActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLongitude = location.getLongitude();
                                currentLatitude = location.getLatitude();
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==  MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //code to execute after permission is granted.
            } else {
                //code to do instead
            }
        }
    }
    public double getCurrentLongitude() {
        return currentLongitude;
    }
    public double getCurrentLatitude() {
        return currentLatitude;
    }
}
