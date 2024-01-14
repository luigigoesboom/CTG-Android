package com.example.androidappctg;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView productsTextView;
    private String url = "https://ctgshop.azurewebsites.net/api/v1/Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        productsTextView = (TextView) findViewById(R.id.productsTextView);
        JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
        requestQueue.add(request);
    }

    public void prikaziProdukte(View view) {
        if (view != null) {
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    public static final String EXTRA_MESSAGE = "com.example.androidappctg.MESSAGE";

    public void addProductActivity (View view) {
        Intent intent = new Intent(this,AddProductActivity.class);
        String message = "Dodaj nov produkt.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object =response.getJSONObject(i);
                    String name = object.getString("name");
                    int cena = object.getInt("price");
                    String opis = object.getString("description");

                    data.add(name + " " + cena + " " + opis);

                } catch (JSONException e){
                    e.printStackTrace();
                    return;

                }
            }

            productsTextView.setText("");


            for (String row: data){
                String currentText = productsTextView.getText().toString();
                productsTextView.setText(currentText + "\n\n" + row);
            }

        }

    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.getMessage() != null) {
                Log.d("REST error", error.getMessage());
            } else {
                Log.d("REST error", "Error without a message");
            }
        }
    };
}

