package com.company.samsalvail.liveinlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements EnterCityDialog.EnterCityDialogListener {
    private Button getVenue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getVenue = (Button) findViewById(R.id.button_get_venue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchPosting(View view) {
        startActivity(new Intent(this, PostingActivity.class));
    }

    public void getVenues(View view) {
        FragmentManager fm = getSupportFragmentManager();
        EnterCityDialog enterCityDialog = EnterCityDialog.newInstance("Enter the City");
        enterCityDialog.show(fm, "fragment_enter_city");
        // startActivity(new Intent(this, VenueListViewActivity.class));
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        Intent i = new Intent(this, CityInfoActivity.class);
        i.putExtra("city", inputText.trim());
        startActivity(i);
    }
}
