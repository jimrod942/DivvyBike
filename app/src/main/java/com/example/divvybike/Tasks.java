package com.example.divvybike;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class Tasks {
    static class GetJsonTask extends AsyncTask<Bitmap, Integer, Integer> {

        private WeakReference<LoadingActivity> activityReference;

        private RequestQueue requestQueue;

        GetJsonTask(final LoadingActivity context, final RequestQueue setRequestQueue) {
            activityReference = new WeakReference<>(context);
            requestQueue = setRequestQueue;
        }

        protected Integer doInBackground(final Bitmap... currentBitmap) {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Prepare our API request
            String requestURL = Uri.parse("https://feeds.divvybikes.com/stations/stations.json")
                    .buildUpon()
                    .build()
                    .toString();

            /*
             * Make the API request.
             */
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET, requestURL,
                    this::handleApiResponse, this::handleApiError) {
            };
            requestQueue.add(stringRequest);

            /* doInBackground can't return void, otherwise we would. */
            return 0;
        }

        /**
         * Processes a response from the image recognition API.
         * @param response The JSON text of the response.
         */
        void handleApiResponse(final String response) {
            // On success, clear the progress bar and call finishProcessImage
            LoadingActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.finishGetJson(response);
        }

        /**
         * Handles an error encountered when trying to use the image recognition API.
         * @param error The error that caused the request to fail.
         */
        void handleApiError(final VolleyError error) {
            // On failure just clear the progress bar
            NetworkResponse networkResponse = error.networkResponse;
            LoadingActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
    }

    static class GetJsonTaskOnRefresh extends AsyncTask<Bitmap, Integer, Integer> {

        private WeakReference<MainActivity> activityReference;

        private RequestQueue requestQueue;

        GetJsonTaskOnRefresh(final MainActivity context, final RequestQueue setRequestQueue) {
            activityReference = new WeakReference<>(context);
            requestQueue = setRequestQueue;
        }

        protected Integer doInBackground(final Bitmap... currentBitmap) {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Prepare our API request
            String requestURL = Uri.parse("https://feeds.divvybikes.com/stations/stations.json")
                    .buildUpon()
                    .build()
                    .toString();

            /*
             * Make the API request.
             */
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET, requestURL,
                    this::handleApiResponse, this::handleApiError) {
            };
            requestQueue.add(stringRequest);

            /* doInBackground can't return void, otherwise we would. */
            return 0;
        }

        /**
         * Processes a response from the image recognition API.
         * @param response The JSON text of the response.
         */
        void handleApiResponse(final String response) {
            // On success, clear the progress bar and call finishProcessImage
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.finishGetJsonRefresh(response);
        }

        /**
         * Handles an error encountered when trying to use the image recognition API.
         * @param error The error that caused the request to fail.
         */
        void handleApiError(final VolleyError error) {
            // On failure just clear the progress bar
            NetworkResponse networkResponse = error.networkResponse;
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
    }
}
