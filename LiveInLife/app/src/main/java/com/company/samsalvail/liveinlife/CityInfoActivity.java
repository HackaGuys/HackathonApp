package com.company.samsalvail.liveinlife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityInfoActivity extends AppCompatActivity {
    private TextView city;
    private Button next;
    private String cityURL = "";
    private ArrayList<Qualities> qualities = new ArrayList<Qualities>();
    private GridLayout gridLayout;

    IconRoundCornerProgressBar bar1;
    IconRoundCornerProgressBar bar2;
    IconRoundCornerProgressBar bar3;
    IconRoundCornerProgressBar bar4;
    IconRoundCornerProgressBar bar5;
    IconRoundCornerProgressBar bar6;
    IconRoundCornerProgressBar bar7;
    IconRoundCornerProgressBar bar8;
    IconRoundCornerProgressBar bar9;
    IconRoundCornerProgressBar bar10;
    IconRoundCornerProgressBar bar11;
    IconRoundCornerProgressBar bar12;
    IconRoundCornerProgressBar bar13;
    IconRoundCornerProgressBar bar14;
    IconRoundCornerProgressBar bar15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_info);

        city = (TextView) findViewById(R.id.text_title_city);
        city.setText(getIntent().getStringExtra("city"));
        next = (Button) findViewById(R.id.button_get_listing);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        bar1 = (IconRoundCornerProgressBar) findViewById(R.id.housingScore);
        bar2 = (IconRoundCornerProgressBar) findViewById(R.id.costOfLivingScore);
        bar3 = (IconRoundCornerProgressBar) findViewById(R.id.startupsScore);
        bar4 = (IconRoundCornerProgressBar) findViewById(R.id.ventureScore);
        bar5 = (IconRoundCornerProgressBar) findViewById(R.id.travelScore);
        bar6 = (IconRoundCornerProgressBar) findViewById(R.id.commuteScore);
        bar7 = (IconRoundCornerProgressBar) findViewById(R.id.businessScore);
        bar8 = (IconRoundCornerProgressBar) findViewById(R.id.safetyScore);
        bar9 = (IconRoundCornerProgressBar) findViewById(R.id.healthScore);
        bar10 = (IconRoundCornerProgressBar) findViewById(R.id.educationScore);
        bar11 = (IconRoundCornerProgressBar) findViewById(R.id.environmentScore);
        bar12 = (IconRoundCornerProgressBar) findViewById(R.id.economyScore);
        bar13 = (IconRoundCornerProgressBar) findViewById(R.id.taxationScore);
        bar14 = (IconRoundCornerProgressBar) findViewById(R.id.internetScore);
        bar15 = (IconRoundCornerProgressBar) findViewById(R.id.cultureScore);
        setUpWeather();
    }

    private void setUpWeather() {
        TextView weather = (TextView) findViewById(R.id.text_weather);
        MainActivity example = new MainActivity();
        String response = null;
        String city = getIntent().getStringExtra("city");
        try {
            response = new RetrieveWeather().execute("http://api.openweathermap.org/data/2.5/weather?q="+ city +",ca&units=metric&appid=205a068f626698477ed5b64c4aa24c91").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(response);
            JSONObject temp = jsonRootObject.getJSONObject("main");
            String temperature = temp.getString("temp");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("weather");
            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String condition = jsonObject.optString("main");
                data += "Current Conditions: " + condition + " \nTemperature: " + temperature;
            //}
            weather.setText(data);

            new getCity().execute();

        } catch (JSONException e) {e.printStackTrace();}
    }

    public void launchListings(View view) {
        Intent i = new Intent(this, VenueListViewActivity.class);
        i.putExtra("city", getIntent().getStringExtra("city"));
        startActivity(i);
        finish();
    }

    private class getQualities extends AsyncTask<Void, Void, JSONArray> {

        protected JSONArray doInBackground(Void... arg0) {
            JSONArray Jarray = null;
            String jsonData = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(cityURL + "scores/")
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jsonData = responses.body().string();
                JSONObject Jobject = new JSONObject(jsonData);
                Jarray = Jobject.getJSONArray("categories");
                for (int i = 0; i < Jarray.length(); i++) {
                    String color = Jarray.getJSONObject(i).getString("color");
                    String name = Jarray.getJSONObject(i).getString("name");
                    double score = Jarray.getJSONObject(i).getDouble("score_out_of_10");

                    Qualities qual = new Qualities(color, name, score);
                    qualities.add(qual);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Jarray;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            //do stuff
            super.onPostExecute(result);
            for (int i = 0; i < qualities.size(); i++) {
                switch (i) {
                    case 0:
                        bar1.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 1:
                        bar2.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 2:
                        bar3.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 3:
                        bar4.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 4:
                        bar5.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 5:
                        bar6.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 6:
                        bar7.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 7:
                        bar8.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 8:
                        bar9.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 9:
                        bar10.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 10:
                        bar11.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 11:
                        bar12.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 12:
                        bar13.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 13:
                        bar14.setProgress((float) qualities.get(i).get_score());
                        break;
                    case 14:
                        bar15.setProgress((float) qualities.get(i).get_score());
                        break;
                }
            }
        }
    }

    private class getCity extends AsyncTask<Void, Void, Void> {

        String city = getIntent().getStringExtra("city");

        protected Void doInBackground(Void... arg0) {
            JSONArray Jarray = null;
            String jsonData = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.teleport.org/api/urban_areas/")
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jsonData = responses.body().string();
                JSONObject Jobject = new JSONObject(jsonData);
                JSONObject Jobject2 = Jobject.getJSONObject("_links");
                Jarray = Jobject2.getJSONArray("ua:item");
                for (int i = 0; i < Jarray.length(); i++) {
                    String name = Jarray.getJSONObject(i).getString("name");
                    if(name.equalsIgnoreCase(city)) {
                        cityURL = Jarray.getJSONObject(i).getString("href");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //do stuff
            if(cityURL != "") {
                new getQualities().execute();
            } else {
                gridLayout.setVisibility(View.GONE);
                //RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
        }
    }
}
