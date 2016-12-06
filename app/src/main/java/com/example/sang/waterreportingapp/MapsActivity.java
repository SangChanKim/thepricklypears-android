package com.example.sang.waterreportingapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.sang.waterreportingapp.model.Location;
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

    private int currWaterSourceReportNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                    System.out.println(report.getLocation().getLatitude() + ", " + report.getLocation().getLatitude());
                    LatLng loc = new LatLng(report.getLocation().getLatitude(), report.getLocation().getLongitude());
                    MarkerOptions markerOpt = new MarkerOptions().position(loc).title("Report " + report.getReportNumber());
                    markerOpt.snippet(formatMarkerAdditionalInfo(report));

                    mMap.addMarker(markerOpt);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }


    public void onCreateWaterSourceReportButton() {

    }

    private String formatMarkerAdditionalInfo(WaterSourceReport report) {
        String userString = "Made by " + report.getUsername();
        String locationString = "Loc: " + report.getLocation().getLatitude() + ", " + report.getLocation().getLongitude();
        String waterConditionString = report.getWaterCondition().toString();
        String waterTypeString = report.getWaterType().toString();

        return userString + ", " + waterConditionString + ", " + waterTypeString;

    }

}
