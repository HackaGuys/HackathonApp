package com.company.samsalvail.liveinlife;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class VenueListViewActivity extends AppCompatActivity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<HashMap<String, String>> listDataGroup;
    private HashMap<String, List<String>> listDataChild;
    private ProgressDialog progress;

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
        // preparing list data
        getListData();
    }

    public void getListData() {
        String url = Constants.URL + "api/posts";
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
                                String lat, lng;
                                lat = lng = "";

                                map.put("address", address);
                                map.put("city", city);
                                map.put("province", province);
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
                                                                    JSONObject category = (JSONObject) categories.getJSONObject(0);
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

                        listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataGroup, listDataChild);

                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                        // Listview Group expanded listener
                        /*
                        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            @Override
                            public void onGroupExpand(final int groupPosition) {
                                String lat = listDataGroup.get(groupPosition).get("lat");
                                String lng = listDataGroup.get(groupPosition).get("lng");

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
                                                        for (int i = 0; i < 2; i++) {
                                                            JSONObject venue = (JSONObject) venues.get(i);
                                                            venueList.add(venue.get("name").toString());
                                                        }

                                                        listDataChild.put(Integer.toString(groupPosition), venueList);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("ListViewAct", "Error fetching children");
                                                }
                                            }
                                    );
                                    Volley.newRequestQueue(getApplicationContext()).add(getRequest);
                                }
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

}
