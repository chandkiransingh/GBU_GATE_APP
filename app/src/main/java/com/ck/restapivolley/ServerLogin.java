package com.ck.restapivolley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ServerLogin extends Activity {

    String URL = "https://gateapi.vishalpandey.xyz/o/token/";
    EditText user,pass;
    Button submit;
    String id,password;
    String token;
    final HashMap<String,String> hmap = new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_server_login);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               id = user.getText().toString();
               password = pass.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(ServerLogin.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ServerLogin.this,response,Toast.LENGTH_LONG).show();
                        //Log.d("response", "onResponse: response ck "+response);
                        response = response.substring(1, response.length()-1);           //remove curly brackets
                        String[] keyValuePairs = response.split(",");              //split the string to creat key-value pairs


                        for(String pair : keyValuePairs)                        //iterate over the pairs
                        {
                            String[] entry = pair.split(":");                   //split the pairs to get key and value
                            hmap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                        }

                        token = hmap.get("\"access_token\"");
                        //Toast.makeText(ServerLogin.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: token"+token);
                        Intent intent = new Intent(getBaseContext(), Visitors.class);
                        intent.putExtra("SESSION_ID", token);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ServerLogin.this,error.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(ServerLogin.this,"Wrong Credentials", Toast.LENGTH_LONG).show();
                        Log.d("error", "onErrorResponse: error "+error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("grant_type","password");
                params.put("client_id","ZCI15L3nitQ0f04oNlhKDMvl0jngEX6OP5oxp2so");
                params.put("username", id);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
            }
        });
    }
}