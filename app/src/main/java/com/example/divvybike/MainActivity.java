package com.example.divvybike;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.content.Intent;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.example.lib.Station;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

/**
 * Class for the Main Activity.
 */
public class MainActivity extends AppCompatActivity {


    private RequestQueue requestQueue;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static final int NUMBER_OF_ROWS = 20;

    private static final int BUTTON_WIDTH = 720;

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

    public List<Station> stations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        stations = (ArrayList<Station>) getIntent().getSerializableExtra("STATIONS");

        // Requests location permissions.
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        /*
         * UI connections and variable initializations.
         */
        ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /*
         * fetched location of user onClick for refresh button.
         */
        refreshButton.setOnClickListener(v -> {
            new Tasks.GetJsonTaskOnRefresh(MainActivity.this, requestQueue).execute();
            fetchLocation();
            System.out.println();
        });

        /*
         * Fills the table layout with the rows dynamically
         */
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            Button stationButton = new Button(this);
            stationButton.setWidth(screenWidth);
            stationButton.setHeight(140);
            stationButton.setTransformationMethod(null);
            stationButton.setText(stations.get(i).getStationName());
            stationButton.setTextSize(18);

            stationButton.setOnClickListener(v -> {
                Intent startNewActivity = new Intent(MainActivity.this, DetailActivity.class);
                MainActivity.this.startActivity(startNewActivity);
            });

            row.addView(stationButton);
            tableLayout.addView(row, i);
            System.out.println(i);
        }

    }

    /**
     * function for parsing json.
     * @param jsonResult a string of the resultant json.
     */
    protected void finishGetJsonRefresh(final String jsonResult) {
        stations.clear();
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
    }

    private void fetchLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this).setTitle("Location Permission Required")
                        .setMessage("Location permission is required to access this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
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
                ActivityCompat.requestPermissions(MainActivity.this,
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
