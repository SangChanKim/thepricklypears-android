package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sang.waterreportingapp.model.Location;
import com.example.sang.waterreportingapp.model.WaterSourceReport;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
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

public class AllReportsActivity extends AppCompatActivity {

    private int currWaterSourceReportNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);

        ListView waterReportsListView = (ListView) findViewById(R.id.waterReportListView);
        ArrayList<String> listItems=new ArrayList<String>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        waterReportsListView.setAdapter(adapter);

        // loading reports
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final ArrayList<WaterSourceReport> waterSourceReports = new ArrayList<>();

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
                    adapter.add("Report #: " + report.getReportNumber()
                            + "\nDate: " + report.getDate()
                            + "\nLocation: " + report.getLocation()
                            + "\nWater Condition: " + report.getWaterCondition()
                            + "\nWater Type: " + report.getWaterType()
                            + "\nUser: " + report.getUsername());
                    waterSourceReports.add(report);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        Button homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent homeIntent = new Intent(AllReportsActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("username", getIntent().getExtras().getString("username"));
                homeIntent.putExtra("password", getIntent().getExtras().getString("password"));
                AllReportsActivity.this.startActivity(homeIntent);
            }
        });
    }
}
