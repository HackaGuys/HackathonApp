package com.company.samsalvail.liveinlife;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RetrieveWeather extends AsyncTask<String, Integer, String> {
    private Exception exception;
    @Override
    protected String doInBackground(String... params) {
        String response;
        try {
            RetrieveWeather example = new RetrieveWeather();
            response = null;
            try {
                response = example.run(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
        return response;
    }
    OkHttpClient client = new OkHttpClient();
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}