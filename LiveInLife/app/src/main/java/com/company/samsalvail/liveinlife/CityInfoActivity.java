package com.company.samsalvail.liveinlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CityInfoActivity extends AppCompatActivity {
    private TextView city;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_info);

        city = (TextView) findViewById(R.id.text_title_city);
        city.setText(getIntent().getStringExtra("city"));
        next = (Button) findViewById(R.id.button_get_listing);
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
        } catch (JSONException e) {e.printStackTrace();}
    }

    public void launchListings(View view) {
        Intent i = new Intent(this, VenueListViewActivity.class);
        i.putExtra("city", getIntent().getStringExtra("city"));
        startActivity(i);
        finish();
    }
}
