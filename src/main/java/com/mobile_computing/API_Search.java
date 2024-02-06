package com.mobile_computing;

import org.json.*;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.RequestQueue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.content.Intent;

public class API_Search extends Activity {


    private final String API_URL = "http://192.168.56.1:8080/api/s";
    private final String LOG_TAG = "MOBILE COMPUTING";

    private RecyclerView m_recView;
    private RecyclerView.Adapter m_adapter;
    private RecyclerView.LayoutManager m_layout;
    private RequestQueue requestQ;

    // This is run when the intent is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Setup the different views
        m_recView = (RecyclerView) findViewById(R.id.recycler_view);
        m_layout  = new LinearLayoutManager(this);
        m_adapter = new DatumAdapter(this);

        m_recView.setHasFixedSize(true);
        m_recView.setLayoutManager(m_layout);
        m_recView.setAdapter(m_adapter);



        final Context self = this;
        ((DatumAdapter) m_adapter).setOnItemClickListener(new DatumAdapter.DatumClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                String msg = "Item with ID: " + ((DatumAdapter) m_adapter).getItem(position).id();

                //Create new selected Item on click
                Datum selectedItem = ((DatumAdapter) m_adapter).getItem(position);
                Toast toast = Toast.makeText(self, msg, Toast.LENGTH_SHORT);
                toast.show();
                //OPENS THE NEW WINDOW ONCE CLICKED
                Intent intent = new Intent(API_Search.this, ResultDisplayActivity.class);
                intent.putExtra("title", selectedItem.title());
                intent.putExtra("text", selectedItem.text());
                intent.putExtra("date", selectedItem.date());
                intent.putExtra("id",selectedItem.id());
                intent.putExtra("imageURL", selectedItem.imageUrl());

                startActivity(intent);
            }
        });

        // Setup the button
        final Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(LOG_TAG, "Search button clicked");
                ((DatumAdapter) m_adapter).clear();
                search();
            }
        });

        // Setup the request queue for network requests (using Volley)
        requestQ = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        //search();
    }

    // Initiate a search
    private void search() {
        // Make a request

        final EditText keywordEditText = (EditText) findViewById(R.id.keyWordEditText);
        String keyword = keywordEditText.getText().toString();
        Log.d(LOG_TAG, "Initiating search for keyword: " + keyword);
        Log.d(LOG_TAG, "Request URL: " + API_URL + " " + keywordEditText.getText().toString());
        final Context self = this;
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keywordEditText.getWindowToken(), 0);
        StringRequest res = new StringRequest(Request.Method.GET, API_URL + "/" + keyword,
                new Response.Listener<String>() {
                    private ImageLoader imgLoad = VolleySingleton.getInstance(self).getImageLoader();

                    // On response
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("NetworkResponse", "Response from server: " + response);
                            // Get the response and convert it to JSON
                            JSONArray resultJsonArray = (new JSONObject(response)).getJSONArray("results");
                            // Iterate over all of the JSON responses
                            JSONObject obj = null;
                            for (int i = 0; i < resultJsonArray.length(); ++i) {
                                // Get the properties
                                obj = resultJsonArray.getJSONObject(i);
                                Log.d(LOG_TAG,obj.getString("image"));
                                Datum datum = new Datum(obj.getInt("id"), obj.getString("title"),
                                        obj.getString("date"), obj.getString("text"), obj.getString("image"));

                                // Add the item to the view
                                ((DatumAdapter) m_adapter).addItem(datum, m_adapter.getItemCount());
                            }

                        } catch (Exception e) {
                            // Print to console on error
                            //Toast.makeText(self, ""+e, LENGTH_SHORT).show();
                            Log.d(LOG_TAG,e+"");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            // Print to console on Error
            @Override
            public void onErrorResponse(VolleyError err) {

                Log.e("NetworkError", "Error during request: " + err.toString());
                err.printStackTrace();
            }
        }
        );

        // Start the request
        requestQ.add(res);
    }

}