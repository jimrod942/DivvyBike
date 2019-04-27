package com.example.divvybike;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import com.example.divvybike.MainActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(divvyJSON);
        JsonArray divvyAPIReturn = tradeElement.getAsJsonArray();
    }
}
