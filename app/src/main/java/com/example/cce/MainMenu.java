package com.example.cce;

import static android.Manifest.permission.CALL_PHONE;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.widget.ImageButton;


import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    ImageButton sos,profile,check,alarm;


    Button sos_call;
    private static final Location TODO = null;
    private FusedLocationProviderClient client;
    DataBaseHandler myDB;
    private static final int REQUEST_CHECK_CODE = 8989;
    private LocationSettingsRequest.Builder builder;
    String x = "", y = "";
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Intent mIntent;
    LocationManager mLocationManager;
    public static int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sos = (ImageButton) findViewById(R.id.sos);
        profile = (ImageButton) findViewById(R.id.profile);
        check = (ImageButton) findViewById(R.id.check);
        alarm = (ImageButton) findViewById(R.id.alarm);


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this,Check.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Profile.class);
                startActivity(intent);
            }
        });
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Call.class);
                startActivity(intent);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MainDisplay.class);
                startActivity(intent);
            }
        });

        sos_call = (Button) findViewById(R.id.sos_call);
        myDB = new DataBaseHandler(this);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGPS();
        } else {
            startTrack();
        }

        sos_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                danger();
            }
        });

    }
        private void danger () {

            loadData(114);

        }


        private void loadData ( int call){

            ArrayList<String> thelist = new ArrayList<>();
            Cursor data = myDB.getListContents();

            if (data.getCount() == 0) {
                Toast.makeText(MainMenu.this, "no content to show", Toast.LENGTH_SHORT).show();
            } else {
                String msg = "I need Help my current location is :" +"\n"+"Latitude : " + x + " Longitude : " + y;
                String number = "";

                while (data.moveToNext()) {
                    thelist.add(data.getString(1));
                    number = number + data.getString(1) + (data.isLast() ? "" : ";");
                    call(call);
                }
                if (!thelist.isEmpty()) {
                    sendSMS(number, msg, true);
                }
            }
        }
        private void sendSMS (String number, String msg,boolean b){

            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            SmsManager sms = SmsManager.getDefault();
            for (String s : loadContact()) {
                sms.sendTextMessage(s, null, msg, pi, null);
            }

        }
        private void call ( int call){
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + call));
            if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(i);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        }

        private void startTrack () {
            if (ActivityCompat.checkSelfPermission(MainMenu.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainMenu.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location locationGPS) {
                                // GPS location can be null if GPS is switched off
                                if (locationGPS != null) {
                                    double lat = locationGPS.getLatitude();
                                    double lon = locationGPS.getLongitude();
                                    x = String.valueOf(lat);
                                    y = String.valueOf(lon);
                                } else {
                                    Toast.makeText(MainMenu.this, "UNABLE TO FIND LOCATION", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MapDemoActivity", "Error trying to get last GPS location");
                                e.printStackTrace();
                            }
                        });
            }
        }
        private void onGPS () {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }

        private ArrayList<String> loadContact () {
            ArrayList<String> theList = new ArrayList<>();
            Cursor data = myDB.getListContents();
            if (data.getCount() == 0) {
                Toast.makeText(MainMenu.this, "There is no content", Toast.LENGTH_SHORT).show();
            } else {
                while (data.moveToNext()) {
                    theList.add(data.getString(1));
                }
            }
            return theList;
        }

        public boolean onKeyDown ( int keyCode, KeyEvent event){
            if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
                i++;
                if (i == 2) {
                    //do something
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "114"));
                    startActivity(intent);
                    i = 0;
                }

            }
            return super.onKeyDown(keyCode, event);
        }


    }