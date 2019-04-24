package com.example.divvybike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView stationTextView = (TextView) findViewById(R.id.station_text_view);
        LinearLayout detailLinearLayout = (LinearLayout) findViewById(R.id.detail_linear_layout);

        stationTextView.setBackgroundColor(getResources().getColor(R.color.divvy_blue));
        detailLinearLayout.setBackgroundColor(getResources().getColor(R.color.divvy_dark));
    }
}
