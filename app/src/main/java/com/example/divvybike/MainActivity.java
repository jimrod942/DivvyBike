package com.example.divvybike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
