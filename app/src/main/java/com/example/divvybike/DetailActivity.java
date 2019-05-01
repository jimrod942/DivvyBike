package com.example.divvybike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.Integer;

import com.example.lib.Station;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView stationTextView = (TextView) findViewById(R.id.station_text_view);
        LinearLayout detailLinearLayout = (LinearLayout) findViewById(R.id.detail_linear_layout);
        TextView statusTextView = (TextView) findViewById(R.id.status_text_view);
        TextView bikesTextView = (TextView) findViewById(R.id.bikes_text_view);
        TextView docksTextView = (TextView) findViewById(R.id.docks_text_view);
        Button mapButton = (Button) findViewById(R.id.map_button);

        mapButton.setTransformationMethod(null);
        stationTextView.setBackgroundColor(getResources().getColor(R.color.divvy_blue));
        detailLinearLayout.setBackgroundColor(getResources().getColor(R.color.divvy_dark));

        Station selectedStation = (Station) getIntent().getSerializableExtra("STATION");

        stationTextView.setText(selectedStation.getStationName());
        statusTextView.setText(selectedStation.getStatusValue());
        bikesTextView.setText(Integer.toString(selectedStation.getAvailableBikes()));
        docksTextView.setText(Integer.toString(selectedStation.getAvailableDocks()));

        mapButton.setOnClickListener(v -> {
            
        });
    }
}
