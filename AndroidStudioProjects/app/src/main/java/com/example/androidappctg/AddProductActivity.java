package com.example.androidappctg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import android.os.Bundle;

public class AddProductActivity extends AppCompatActivity {


    private TextView status;
    private EditText name;
    private EditText price;
    private EditText rating;
    private EditText stock;
    private EditText category;
    private EditText description;
    private EditText brand;
    private RequestQueue requestQueue;
    private String url = "https://ctgshop.azurewebsites.net/api/v1/Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        requestQueue = Volley.newRequestQueue(this);
        status = (TextView) findViewById(R.id.status);
        name = (EditText) findViewById(R.id.teName);
        price = (EditText) findViewById(R.id.tePrice);
        rating = (EditText) findViewById(R.id.teRating);
        stock = (EditText) findViewById(R.id.teStock);
        category = (EditText) findViewById(R.id.teCategory);
        description = (EditText) findViewById(R.id.teDescription);
        brand = (EditText) findViewById(R.id.teBrand);
    }
    public void submitProduct(View view) {
        this.status.setText("Posting to " + url);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name.getText().toString());
            jsonBody.put("description", description.getText().toString());

            // Parse numeric values from EditText and convert them to the correct type
            double priceValue = Double.parseDouble(price.getText().toString());
            float ratingValue = Float.parseFloat(rating.getText().toString());
            int stockValue = Integer.parseInt(stock.getText().toString());
            int categoryValue = Integer.parseInt(category.getText().toString());
            int brandValue = Integer.parseInt(brand.getText().toString());

            // Add numeric values to the JSON object
            jsonBody.put("price", priceValue);
            jsonBody.put("rating", ratingValue);
            jsonBody.put("stock", stockValue);
            jsonBody.put("category", categoryValue);
            jsonBody.put("brand", brandValue);

            final String mRequestBody = jsonBody.toString();

            status.setText(mRequestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        status.setText(responseString);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}