package com.company.samsalvail.liveinlife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VenueListViewActivity extends AppCompatActivity implements EnterCityDialog.EnterCityDialogListener {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<HashMap<String, String>> listDataGroup;
    private HashMap<String, List<String>> listDataChild;
    private ProgressDialog progress;
    private Button findMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list_view);

        progress = new ProgressDialog(VenueListViewActivity.this);
        progress.setMessage("Fetching data...");

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listDataGroup = new ArrayList<>();
        listDataChild = new HashMap<>();
        findMore = (Button) findViewById(R.id.button_find_more);
        // preparing list data
        getListData();
    }

    public void getListData() {
        String url = Constants.URL + "api/posts?city=" + getIntent().getStringExtra("city");
        progress.show();
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) response.get(i);
                                final HashMap<String, String> map = new HashMap<>();
                                String address = item.get("address").toString();
                                String city = item.get("city").toString();
                                String province = item.get("province").toString();
                                String price = item.get("price").toString();
                                String lat, lng;
                                lat = lng = "";

                                map.put("address", address);
                                map.put("city", city);
                                map.put("province", province);
                                map.put("price", price);
                                listDataGroup.add(map);

                                Geocoder gc = new Geocoder(getApplicationContext());
                                if (gc.isPresent()) {
                                    List<Address> list = null;
                                    try {
                                        list = gc.getFromLocationName(address + ", " + city + ", " + province, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Address addr;
                                    if (list.size() != 0) {
                                        addr = list.get(0);
                                        lat = Double.toString(addr.getLatitude());
                                        lng = Double.toString(addr.getLongitude());

                                        if (lat != null && lng != null) {
                                            String url = Constants.VENUES_SEARCH + "?" + "client_id=" + Constants.CLIENT_ID +
                                                    "&client_secret=" + Constants.CLIENT_SECRET + "&v=" + Constants.VERSION +
                                                    "&ll=" + lat + "," + lng;
                                            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {

                                                            try {
                                                                JSONObject resp = (JSONObject) response.get("response");
                                                                JSONArray venues = resp.getJSONArray("venues");
                                                                List<String> venueList = new ArrayList<>();
                                                                for (int i = 0; i < 10; i++) {
                                                                    JSONObject venue = (JSONObject) venues.get(i);
                                                                    JSONObject location = (JSONObject) venue.get("location");
                                                                    JSONArray categories = (JSONArray) venue.get("categories");
                                                                    JSONObject category = categories.getJSONObject(0);
                                                                    venueList.add(venue.get("name").toString() + ": " +
                                                                            category.get("name").toString() + "\n" +
                                                                            location.get("distance").toString() + "m away");
                                                                }
                                                                progress.dismiss();
                                                                listDataChild.put(Integer.toString(listDataGroup.indexOf(map)), venueList);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            progress.dismiss();
                                                            Log.d("ListViewAct", "Error fetching children");
                                                        }
                                                    }
                                            );
                                            Volley.newRequestQueue(getApplicationContext()).add(getRequest);

                                        }
                                    }
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                progress.dismiss();
                            }
                        }

                        listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataGroup, listDataChild, expListView);

                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v,
                                                        int groupPosition, long id) {
                                setListViewHeight(parent, groupPosition);
                                return false;
                            }
                        });

                        // Listview Group expanded listener
                        /*
                        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            @Override
                            public void onGroupExpand(final int groupPosition) {
                                TextView weather = (TextView) findViewById(R.id.text_weather);
                                MainActivity example = new MainActivity();
                                String response = null;
                                String city = listDataGroup.get(groupPosition).get("city");
                                try {
                                    response = new RetrieveWeather().execute("http://api.openweathermap.org/data/2.5/weather?q="+ city +",ca&units=metric&appid=205a068f626698477ed5b64c4aa24c91").get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                ​
                                String data = "";
                                try {
                                    JSONObject  jsonRootObject = new JSONObject(response);
                                    ​
                                    JSONObject temp  = jsonRootObject.getJSONObject("main");
                                    String temperature = temp.getString("temp");
                                    ​
                                    //JSONObject city  = jsonRootObject.getJSONObject("name");
                                    String cityName = jsonRootObject.getString("name");
                                    ​
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = jsonRootObject.optJSONArray("weather");
                                    ​
                                    //Iterate the jsonArray and print the info of JSONObjects
                                    for(int i=0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        ​
                                        int id = Integer.parseInt(jsonObject.optString("id"));
                                        String condition = jsonObject.optString("main");
                                        ​
                                        data += "City = " + cityName + " \nCurrent Conditions = "+ condition +" \nTemperature = " + temperature +"";
                                    }
                                    weather.setText(data);
                                    ​
                                } catch (JSONException e) {e.printStackTrace();}
                            }
                        });
                        */
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                    }
                }
        );
        Volley.newRequestQueue(this).add(getRequest);

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
        finish();
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
