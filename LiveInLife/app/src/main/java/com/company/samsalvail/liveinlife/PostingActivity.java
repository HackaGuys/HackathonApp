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
    private EditText city, address, province;
    private final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        progress = new ProgressDialog(PostingActivity.this);
        progress.setMessage("Making the Posting...");

        address = (EditText) findViewById(R.id.edit_address);
        city = (EditText) findViewById(R.id.edit_city);
        province = (EditText) findViewById(R.id.edit_province);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            /*
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            */
            ImageView imageView = (ImageView) findViewById(R.id.img);
            imageView.setImageURI(selectedImage);
            // imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void sendPosting(View view) {
        progress.show();
        String url = Constants.URL + "";
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
                        Toast.makeText(PostingActivity.this, "failed to make posting",
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
