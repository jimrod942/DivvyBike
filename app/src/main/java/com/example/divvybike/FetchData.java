package com.example.divvybike;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import com.example.divvybike.MainActivity;
import com.example.lib.Station;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class FetchData extends AsyncTask<Void, Void, Void> {
    String divvyJSON = "";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://feeds.divvybikes.com/stations/stations.json");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                divvyJSON = divvyJSON + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        List<Station> locations = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(divvyJSON);
        JsonObject divvyAPIReturn = tradeElement.getAsJsonObject();
        JsonArray allLocations = divvyAPIReturn.get("stationBeanList").getAsJsonArray();
        for (int i = 0; i < allLocations.size(); i++) {
            String name = allLocations.get(i).getAsJsonObject().get("stationName").getAsString();
            int docks = allLocations.get(i).getAsJsonObject().get("availableDocks").getAsInt();
            int bikes = allLocations.get(i).getAsJsonObject().get("availableBikes").getAsInt();
            String status = allLocations.get(i).getAsJsonObject().get("statusValue").getAsString();
            double latitude = allLocations.get(i).getAsJsonObject().get("latitude").getAsDouble();
            double longitude = allLocations.get(i).getAsJsonObject().get("longitude").getAsDouble();
            locations.add(new Station(name, docks, bikes, status, latitude, longitude));
        }

    }
}
