package com.example.divvybike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.example.lib.Station;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

/**
 * Class for the Main Activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Fine-grained location permissions request constant.
     */
    private static final int REQUEST_FINE_LOCATION = 88;

    /**
     * Permission to access fine-grained location.
     */
    private boolean canAccessFineLocation = false;

    /**
     * Location updates.
     */
    private boolean locationEnabled = true;

    /**
     * Fused location provider client.
     */
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest locationRequest;

    /**
     * Not exactly sure what this does.
     */
    private LocationCallback locationCallback;

    private double[] currentLocation = new double[2];

    /**
     * Whether we have received locations.
     */
    private boolean receivedLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* connections for UI elements */
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);

        for (int i = 0; i < 20; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            Button stationButton = new Button(this);

         // adjust this button width at a later date!
            stationButton.setWidth(720);
            stationButton.setText("Example");

            stationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startNewActivity = new Intent(MainActivity.this, DetailActivity.class);
                    MainActivity.this.startActivity(startNewActivity);
                }
            });

            row.addView(stationButton);
            tableLayout.addView(row, i);
        }
    }
}
