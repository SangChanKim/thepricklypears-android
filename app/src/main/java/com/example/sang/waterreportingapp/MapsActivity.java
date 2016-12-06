package com.example.sang.waterreportingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sang.waterreportingapp.model.Location;
import com.example.sang.waterreportingapp.model.QualityCondition;
import com.example.sang.waterreportingapp.model.WaterQualityReport;
import com.example.sang.waterreportingapp.model.WaterSourceReport;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mCurrMarker;

    private ArrayList<WaterSourceReport> waterSourceReports;
    private ArrayList<WaterQualityReport> waterQualityReports;

    private ArrayList<WaterSourceReport> newlyCreatedSrcReports;
    private ArrayList<WaterQualityReport> newlyCreatedQualityReports;

    private int currWaterSourceReportNum;
    private int currWaterQualityReportNum;


    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        date = new Date();
        newlyCreatedSrcReports = new ArrayList<>();
        newlyCreatedQualityReports = new ArrayList<>();


        Button createSrcReportButton = (Button) findViewById(R.id.button2);
        createSrcReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("a");
                onCreateWaterSourceReportButton();
            }
        });


        Button createQualityReportButton = (Button) findViewById(R.id.button4);
        createQualityReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("b");
                onCreateWaterQualityReportButton();
            }
        });

        Button homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent homeIntent = new Intent(MapsActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("username", getIntent().getExtras().getString("username"));
                homeIntent.putExtra("password", getIntent().getExtras().getString("password"));
                MapsActivity.this.startActivity(homeIntent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (mCurrMarker != null) {
                    mCurrMarker.remove();
                }
                // Creating a marker
                MarkerOptions opt = new MarkerOptions();

                // Setting the position for the marker
                opt.position(latLng);

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mCurrMarker = mMap.addMarker(opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            }
        });




        // Load up all our water source reports onto the map
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        waterSourceReports = new ArrayList<>();
        waterQualityReports = new ArrayList<>();


        db.child("waterSourceReports").child("maxReportNum").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot);
                long max = (long) dataSnapshot.getValue();
                currWaterSourceReportNum = (int) max;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        db.child("waterQualityReports").child("maxReportNum").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot);
                long max = (long) dataSnapshot.getValue();
                currWaterQualityReportNum = (int) max;
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });


        db.child("waterSourceReports").child("reports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> listOfReports = (HashMap<String, Object>)dataSnapshot.getValue();
                for (String key: listOfReports.keySet()) {
                    Map<String, String> map = (HashMap<String, String>) listOfReports.get(key);
                    String user = map.get("user");
                    int reportNumber = Integer.parseInt(map.get("reportNumber"));
                    double lat = Double.parseDouble(map.get("lat"));
                    double longitude = Double.parseDouble(map.get("long"));
                    String locationTitle = map.get("locationTitle");

                    String waterTypeStr = map.get("waterType");
                    WaterType[] types = WaterType.values();
                    WaterType realType = types[0];
                    for (WaterType type: types) {
                        if (type.getType().equals(waterTypeStr)) {
                            realType = type;
                        }
                    }

                    String waterConditionStr = map.get("waterCondition");
                    WaterCondition[] conditions = WaterCondition.values();
                    WaterCondition realCondition = conditions[0];
                    for (WaterCondition condition: conditions) {
                        if (condition.getCondition().equals(waterConditionStr)) {
                            realCondition = condition;
                        }
                    }

                    long timestamp = Long.parseLong(map.get("dateCreated"));
                    Date date = new Date(timestamp);

                    WaterSourceReport report = new WaterSourceReport(user,
                            reportNumber,
                            date,
                            new Location(lat, longitude, locationTitle),
                            realType,
                            realCondition);
                    waterSourceReports.add(report);
                }

                // Populate map
                for (WaterSourceReport report: waterSourceReports) {
                    //System.out.println(report.getLocation().getLatitude() + ", " + report.getLocation().getLatitude());
                    LatLng loc = new LatLng(report.getLocation().getLatitude(), report.getLocation().getLongitude());
                    MarkerOptions markerOpt = new MarkerOptions().position(loc).title(report.getLocation().getTitle());
                    markerOpt.snippet(formatMarkerAdditionalInfo(report));

                    mMap.addMarker(markerOpt);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        db.child("waterQualityReports").child("reports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> listOfReports = (HashMap<String, Object>)dataSnapshot.getValue();
                for (String key: listOfReports.keySet()) {
                    Map<String, String> map = (HashMap<String, String>) listOfReports.get(key);
                    String user = map.get("user");
                    int reportNumber = Integer.parseInt(map.get("reportNumber"));
                    double lat = Double.parseDouble(map.get("lat"));
                    double longitude = Double.parseDouble(map.get("long"));
                    String locationTitle = map.get("locationTitle");

                    String qualityConditionStr = map.get("qualityCondition");
                    QualityCondition[] types = QualityCondition.values();
                    QualityCondition realType = types[0];
                    for (QualityCondition type: types) {
                        if (type.getCondition().equals(qualityConditionStr)) {
                            realType = type;
                        }
                    }

                    int virus = Integer.parseInt(map.get("virusPPM"));
                    int contaminant = Integer.parseInt(map.get("contaminantPPM"));

                    long timestamp = Long.parseLong(map.get("dateCreated"));
                    Date date = new Date(timestamp);

                    WaterQualityReport report = new WaterQualityReport(user,
                            reportNumber,
                            date,
                            new Location(lat, longitude, locationTitle),
                            realType,
                            virus,
                            contaminant);
                    waterQualityReports.add(report);
                }


                // Populate map
                for (WaterQualityReport report: waterQualityReports) {
                    //System.out.println(report.getLocation().getLatitude() + ", " + report.getLocation().getLatitude());
                    LatLng loc = new LatLng(report.getLocation().getLatitude(), report.getLocation().getLongitude());
                    MarkerOptions markerOpt = new MarkerOptions().position(loc).title(report.getLocation().getTitle());
                    markerOpt.snippet(formatMarkerAdditionalInfo(report));

                    mMap.addMarker(markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {



                    String type = data.getExtras().getString("type");
                    WaterType t;
                    String cond = data.getExtras().getString("cond");
                    WaterCondition c;

                    if (type.equals("Bottled")) {
                        t = WaterType.BOTTLED;
                    } else if (type.equals("Well")) {
                        t = WaterType.WELL;
                    } else if (type.equals("Spring")) {
                        t = WaterType.SPRING;
                    } else if (type.equals("Stream")) {
                        t = WaterType.STREAM;
                    } else if (type.equals("Lake")) {
                        t = WaterType.LAKE;
                    } else {
                        t = WaterType.OTHER;
                    }

                    if (cond.equals("Waste")) {
                        c = WaterCondition.WASTE;
                    } else if (cond.equals("Treatable-Clear")) {
                        c = WaterCondition.TREATABLE_CLEAR;
                    } else if (cond.equals("Treatable-Muddy")) {
                        c = WaterCondition.TREATABLE_MUDDY;
                    } else {
                        c = WaterCondition.POTABLE;
                    }


                    WaterSourceReport report = new WaterSourceReport(
                            data.getExtras().getString("username"),
                            data.getExtras().getInt("reportNum"),
                            date,
                            new Location(
                                    data.getExtras().getDouble("lat"),
                                    data.getExtras().getDouble("long"),
                                    data.getExtras().getString("locName")),
                            t,
                            c);

                    waterSourceReports.add(report);
                    newlyCreatedSrcReports.add(report);

                    // Update map

                    LatLng loc = new LatLng(report.getLocation().getLatitude(), report.getLocation().getLongitude());
                    MarkerOptions markerOpt = new MarkerOptions().position(loc).title(report.getLocation().getTitle());
                    markerOpt.snippet(formatMarkerAdditionalInfo(report));

                    mMap.addMarker(markerOpt);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    if (mCurrMarker != null) {
                        mCurrMarker.remove();
                    }
                    mCurrMarker = null;

                    // Persist
                    Map<String, String> reportDataMap = new HashMap<>();
                    reportDataMap.put("reportNumber", "" + report.getReportNumber());
                    reportDataMap.put("waterCondition", report.getWaterCondition().toString());
                    reportDataMap.put("user", report.getUsername());
                    reportDataMap.put("dateCreated", "" + report.getDate().getTime());
                    reportDataMap.put("waterType", report.getWaterType().toString());
                    reportDataMap.put("locationTitle", report.getLocation().getTitle());
                    reportDataMap.put("lat", "" + report.getLocation().getLatitude());
                    reportDataMap.put("long","" + report.getLocation().getLongitude());

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("waterSourceReports").child("reports").push().setValue(reportDataMap);
                    currWaterSourceReportNum++;
                    db.child("waterSourceReports").child("maxReportNum").setValue(currWaterSourceReportNum);

                }
                break;
            }
            case (1): {
                if (resultCode == Activity.RESULT_OK) {

                    String qcond = data.getExtras().getString("qcond");
                    QualityCondition c;


                    if (qcond.equals("Safe")) {
                        c = QualityCondition.SAFE;
                    } else if (qcond.equals("Treatable")) {
                        c = QualityCondition.TREATABLE;
                    } else {
                        c = QualityCondition.UNSAFE;
                    }


                    WaterQualityReport newQualityReport = new WaterQualityReport(
                            data.getExtras().getString("username"),
                            data.getExtras().getInt("reportNum"),
                            date,
                            new Location(
                                    data.getExtras().getDouble("lat"),
                                    data.getExtras().getDouble("long"),
                                    data.getExtras().getString("locName")),
                            c,
                            data.getExtras().getInt("virusPPM"),
                            data.getExtras().getInt("contaminantPPM")
                            );

                    waterQualityReports.add(newQualityReport);
                    newlyCreatedQualityReports.add(newQualityReport);

                    // Update map

                    LatLng loc = new LatLng(newQualityReport.getLocation().getLatitude(), newQualityReport.getLocation().getLongitude());
                    MarkerOptions markerOpt = new MarkerOptions().position(loc).title(newQualityReport.getLocation().getTitle());
                    markerOpt.snippet(formatMarkerAdditionalInfo(newQualityReport));

                    mMap.addMarker(markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    if (mCurrMarker != null) {
                        mCurrMarker.remove();
                    }
                    mCurrMarker = null;


                    Map<String, String> reportDataMap = new HashMap<>();
                    reportDataMap.put("reportNumber", "" + newQualityReport.getReportNumber());
                    reportDataMap.put("qualityCondition", newQualityReport.getQualityCondition().toString());
                    reportDataMap.put("user", newQualityReport.getUsername());
                    reportDataMap.put("dateCreated", "" + newQualityReport.getDate().getTime());
                    reportDataMap.put("virusPPM", "" + newQualityReport.getVirusPPM());
                    reportDataMap.put("contaminantPPM", "" + newQualityReport.getContaminantPPM());
                    reportDataMap.put("locationTitle", newQualityReport.getLocation().getTitle());
                    reportDataMap.put("lat", "" + newQualityReport.getLocation().getLatitude());
                    reportDataMap.put("long","" + newQualityReport.getLocation().getLongitude());

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("waterQualityReports").child("reports").push().setValue(reportDataMap);
                    currWaterQualityReportNum++;
                    db.child("waterQualityReports").child("maxReportNum").setValue(currWaterQualityReportNum);
                }
                break;
            }

        }
    }


    public void onCreateWaterSourceReportButton() {
        //if (mCurrMarker != null) {
            Intent intent = new Intent(MapsActivity.this, CreateWaterSourceReport.class);
            intent.putExtra("username", getIntent().getExtras().getString("username"));
            intent.putExtra("reportNum", currWaterSourceReportNum + 1);
            intent.putExtra("dateString", date.toString());
            intent.putExtra("password", getIntent().getExtras().getString("passsword"));

            if (mCurrMarker != null) {
                intent.putExtra("lat", mCurrMarker.getPosition().latitude);
                intent.putExtra("long", mCurrMarker.getPosition().longitude);
            } else {
                intent.putExtra("lat", 0.0);
                intent.putExtra("long", 0.0);
            }

            MapsActivity.this.startActivityForResult(intent, 0);
        //}
    }

    public void onCreateWaterQualityReportButton() {


        Intent intent = new Intent(MapsActivity.this, CreateWaterQualityReport.class);
        intent.putExtra("username", getIntent().getExtras().getString("username"));
        intent.putExtra("reportNum", currWaterSourceReportNum + 1);
        intent.putExtra("dateString", date.toString());
        intent.putExtra("password", getIntent().getExtras().getString("passsword"));

        if (mCurrMarker != null) {
            intent.putExtra("lat", mCurrMarker.getPosition().latitude);
            intent.putExtra("long", mCurrMarker.getPosition().longitude);
        } else {
            intent.putExtra("lat", 0.0);
            intent.putExtra("long", 0.0);
        }

        MapsActivity.this.startActivityForResult(intent, 1);


    }


    private String formatMarkerAdditionalInfo(WaterSourceReport report) {
        String userString = "Made by " + report.getUsername();
        String locationString = "Loc: " + report.getLocation().getLatitude() + ", " + report.getLocation().getLongitude();
        String waterConditionString = report.getWaterCondition().toString();
        String waterTypeString = report.getWaterType().toString();

        return userString + ", " + waterConditionString + ", " + waterTypeString;

    }

    private String formatMarkerAdditionalInfo(WaterQualityReport report) {
        String userString = "Made by " + report.getUsername();
        String locationString = "Loc: " + report.getLocation().getLatitude() + ", " + report.getLocation().getLongitude();
        String waterConditionString = report.getQualityCondition().toString();
        String virus = "Virus PPM: " + report.getVirusPPM();
        String contaminant = "Contaminant PPM: " + report.getContaminantPPM();

        return userString + ", " + waterConditionString + ", " + virus + ", " + contaminant;

    }


}
