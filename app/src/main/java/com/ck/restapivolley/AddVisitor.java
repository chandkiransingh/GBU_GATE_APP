package com.ck.restapivolley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

public class AddVisitor extends AppCompatActivity {

    String sessionId;
    String URL = "https://gateapi.vishalpandey.xyz/visitor/?access_token=";
    EditText card_number,name,address,mobile,number_plate,destination,purpose,outtime;
    Button submitv;
    String Scard_number,Sname,Saddress,Smobile,Snumber_plate,Sdestination,Spurpose,Souttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_visitor);
        sessionId = getIntent().getStringExtra("SESSION_ID");
        Log.d("sessionId", "onCreate: sessionId :"+sessionId);
        card_number = findViewById(R.id.card_number);
        address = findViewById(R.id.address);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        number_plate = findViewById(R.id.number_plate);
        destination = findViewById(R.id.destination);
        purpose = findViewById(R.id.purpose);
        outtime = findViewById(R.id.outtime);
        submitv = findViewById(R.id.submitv);

        submitv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Scard_number = card_number.getText().toString();
                Sname = name.getText().toString();
                Saddress = address.getText().toString();
                Smobile = mobile.getText().toString();
                Snumber_plate = number_plate.getText().toString();
                Sdestination = destination.getText().toString();
                Spurpose = purpose.getText().toString();
                Souttime = outtime.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(AddVisitor.this);
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+sessionId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(ServerLogin.this,response,Toast.LENGTH_LONG).show();
                                //Log.d("response", "onResponse: response ck "+response);
                                //Toast.makeText(ServerLogin.this,"Login Successful",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getBaseContext(), Visitors.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(ServerLogin.this,error.toString(), Toast.LENGTH_LONG).show();
                                Toast.makeText(AddVisitor.this,"Wrong Credentials", Toast.LENGTH_LONG).show();
                                Log.d("error", "onErrorResponse: error "+error.toString());
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("card_number",Scard_number);
                        params.put("name",Sname);
                        params.put("address", Saddress);
                        params.put("mobile", Smobile);
                        params.put("number_plate", Snumber_plate);
                        params.put("destination", Sdestination);
                        params.put("purpose", Spurpose);
                        params.put("outtime", Souttime);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

    }
}
