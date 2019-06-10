package com.ck.restapivolley;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AddVisitor extends Activity {

    private static String sessionId;
    String URL = "https://gateapi.vishalpandey.xyz/visitor/?access_token=";
    EditText card_number,name,address,mobile,number_plate,destination,purpose,intime,outtime;
    Button submitv,inndate,inntime,outtdate,outttime;
    String Scard_number,Sname,Saddress,Smobile,Snumber_plate,Sdestination,Spurpose,Sintime,Souttime;
    private static final int innDate_id = 0;
    private static final int innTime_id = 1;
    private static final int outDate_id = 2;
    private static final int outTime_id = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_visitor);
        inndate = findViewById(R.id.inndate);
        inntime = findViewById(R.id.inntime);
        outtdate = findViewById(R.id.outtdate);
        outttime = findViewById(R.id.outttime);
        sessionId = getIntent().getStringExtra("SESSION_ID");

        Log.d("sessionId", "onCreate: sessionId :" + sessionId);
        card_number = findViewById(R.id.card_number);
        address = findViewById(R.id.address);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        number_plate = findViewById(R.id.number_plate);
        destination = findViewById(R.id.destination);
        purpose = findViewById(R.id.purpose);
        intime = findViewById(R.id.intime);
        outtime = findViewById(R.id.outtime);
        submitv = findViewById(R.id.submitv);

        inndate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Date dialog
                showDialog(innDate_id);
            }
        });
        inntime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show time dialog
                showDialog(innTime_id);
            }
        });


        outtdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Date dialog
                showDialog(outDate_id);
            }
        });
        outttime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show time dialog
                showDialog(outTime_id);
            }
        });

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
                Sintime = intime.getText().toString();
                Souttime = outtime.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(AddVisitor.this);
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + sessionId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(ServerLogin.this,response,Toast.LENGTH_LONG).show();
                                //Log.d("response", "onResponse: response ck "+response);
                                Toast.makeText(AddVisitor.this, "Visitor Added Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getBaseContext(), Visitors.class);
                                intent.putExtra("SESSION_ID", sessionId);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(ServerLogin.this,error.toString(), Toast.LENGTH_LONG).show();
                                Toast.makeText(AddVisitor.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                                Log.d("error", "onErrorResponse: error " + error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("card_number", Scard_number);
                        params.put("name", Sname);
                        params.put("address", Saddress);
                        params.put("mobile", Smobile);
                        params.put("number_plate", Snumber_plate);
                        params.put("destination", Sdestination);
                        params.put("purpose", Spurpose);
                        params.put("intime", Sintime);
                        params.put("outtime", Souttime);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        switch (id) {
            case innDate_id:

                // Open the datepicker dialog
                return new DatePickerDialog(AddVisitor.this, date_listenerin, year,
                        month, day);
            case innTime_id:

                // Open the timepicker dialog
                return new TimePickerDialog(AddVisitor.this, time_listenerin, hour,
                        minute, false);

            case outDate_id:

                // Open the datepicker dialog
                return new DatePickerDialog(AddVisitor.this, date_listenerout, year,
                        month, day);
            case outTime_id:

                // Open the timepicker dialog
                return new TimePickerDialog(AddVisitor.this, time_listenerout, hour,
                        minute, false);
        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listenerin = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            String date1 = String.valueOf(year) + "-" + String.valueOf(month)
                    + "-" + String.valueOf(day);
            intime.setText(date1);
        }
    };

    TimePickerDialog.OnTimeSetListener time_listenerin = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            intime.append("T"+time1+":00Z");
        }
    };



    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listenerout = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            String date1 = String.valueOf(year) + "-" + String.valueOf(month)
                    + "-" + String.valueOf(day);
            outtime.setText(date1);
        }
    };

    TimePickerDialog.OnTimeSetListener time_listenerout = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            outtime.append("T"+time1+":00Z");
        }
    };
}