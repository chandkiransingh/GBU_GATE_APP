package com.ck.restapivolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Visitors extends Activity {

    private static String sessionId;
    Button add_visitor;
    private String TAG = Visitors.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON

    private static String url = "https://gateapi.vishalpandey.xyz/visitor/?access_token=";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_visitors);
        add_visitor = findViewById(R.id.add_visitor);
        sessionId = getIntent().getStringExtra("SESSION_ID");

        try {
            sessionId = sessionId.replaceAll("^\"|\"$", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();

        add_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Visitors.this,AddVisitor.class);
                intent.putExtra("SESSION_ID", sessionId);
                startActivity(intent);
            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Visitors.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url+sessionId);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    // Getting JSON Array node
                    String visit_date,card_number,name,address,mobile,number_plate,destination,purpose,intime,outtime,id;
                    // looping through All Contacts
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject JO = jsonarray.getJSONObject(i);
                        id = JO.getString("id");
                        visit_date = JO.getString("visit_date");
                        card_number = JO.getString("card_number");
                        name = JO.getString("name");
                        address = JO.getString("address");
                        mobile = JO.getString("mobile");
                        number_plate = JO.getString("number_plate");
                        destination = JO.getString("destination");
                        purpose = JO.getString("purpose");
                        intime = JO.getString("intime");
                        outtime = JO.getString("outtime");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id",id);
                        contact.put("visit_date",visit_date);
                        contact.put("card_number",card_number);
                        contact.put("name",name);
                        contact.put("address",address);
                        contact.put("mobile",mobile);
                        contact.put("number_plate",number_plate);
                        contact.put("destination",destination);
                        contact.put("purpose",purpose);
                        contact.put("intime",intime);
                        contact.put("outtime",outtime);


                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Visitors.this, contactList,
                    R.layout.list_item, new String[]{"visit_date","card_number","name","address","mobile","number_plate","destination","purpose","intime","outtime"},
                    new int[]{R.id.visit_date, R.id.card_number, R.id.name, R.id.address, R.id.mobile, R.id.number_plate, R.id.destination, R.id.purpose, R.id.intime, R.id.outtime});
            lv.setAdapter(adapter);
        }
    }
}