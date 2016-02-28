package com.company.samsalvail.liveinlife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PostingActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private EditText city, address, province, zip, price, bedrooms, sqfeet, description;
    private final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        progress = new ProgressDialog(PostingActivity.this);
        progress.setMessage("Making the Posting...");

        address = (EditText) findViewById(R.id.edit_address);
        city = (EditText) findViewById(R.id.edit_enter_city);
        province = (EditText) findViewById(R.id.edit_province);
        zip = (EditText) findViewById(R.id.edit_zip_code);
        price = (EditText) findViewById(R.id.edit_price);
        bedrooms = (EditText) findViewById(R.id.edit_bedrooms);
        sqfeet = (EditText) findViewById(R.id.edit_sqfeet);
        description = (EditText) findViewById(R.id.edit_description);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            ImageView imageView = (ImageView) findViewById(R.id.img);
            imageView.setImageURI(selectedImage);
        }
    }

    public void sendPosting(View view) {
        progress.show();
        String url = Constants.URL + "api/posts";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Automatic login after registration using registered username and password
                        Intent mainIntent = new Intent(PostingActivity.this, MainActivity.class);
                        progress.dismiss();
                        startActivity(mainIntent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("PostingActivity", "failed to make posting");
                        Toast.makeText(PostingActivity.this, "Please ensure all fields are filled out and try again.",
                                Toast.LENGTH_LONG).show();
                    }
                }
        )
        {
            @Override
            //Create the body of the request
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // Get the registration info from input fields and add them to the body of the request
                params.put("address", address.getText().toString());
                params.put("city", city.getText().toString());
                params.put("province", province.getText().toString());
                params.put("zip", zip.getText().toString());
                params.put("price", price.getText().toString());
                params.put("bedrooms", bedrooms.getText().toString());
                params.put("sqfeet", sqfeet.getText().toString());
                params.put("description", description.getText().toString());
                params.put("Content-Type","application/json");
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
    }

    public void selectPicture(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


}
