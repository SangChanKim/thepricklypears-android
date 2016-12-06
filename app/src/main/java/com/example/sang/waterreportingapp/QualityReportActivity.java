package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sang.waterreportingapp.model.Location;
import com.example.sang.waterreportingapp.model.QualityCondition;
import com.example.sang.waterreportingapp.model.WaterQualityReport;
import com.example.sang.waterreportingapp.model.WaterSourceReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QualityReportActivity extends AppCompatActivity {

    private int currWaterSourceReportNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_report);

        ListView qualityReportListView = (ListView) findViewById(R.id.qualityReportListView);
        ArrayList<String> listItems=new ArrayList<String>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        qualityReportListView.setAdapter(adapter);

        // loading reports
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final ArrayList<WaterQualityReport> waterQualityReports = new ArrayList<>();

        db.child("waterQualityReports").child("maxReportNum").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Location location = new Location(lat, longitude, locationTitle);
                    int virusPPM = Integer.parseInt(map.get("virusPPM"));
                    int contaminantPPM = Integer.parseInt(map.get("contaminantPPM"));

                    String waterConditionStr = map.get("qualityCondition");
                    QualityCondition[] conditions = QualityCondition.values();
                    QualityCondition realCondition = conditions[0];
                    for (QualityCondition condition: conditions) {
                        if (condition.getCondition().equals(waterConditionStr)) {
                            realCondition = condition;
                        }
                    }


                    long timestamp = Long.parseLong(map.get("dateCreated"));
                    Date date = new Date(timestamp);

                    WaterQualityReport report = new WaterQualityReport(user, reportNumber, date,
                            location, realCondition,
                            virusPPM, contaminantPPM);
                    adapter.add("Report #: " + report.getReportNumber()
                            + "\nDate: " + report.getDate()
                            + "\nLocation: " + report.getLocation()
                            + "\nQuality Condition: " + report.getQualityCondition()
                            + "\nVirus PPM: " + report.getVirusPPM()
                            + "\nContaminant PPM: " + report.getContaminantPPM()
                            + "\nUser: " + report.getUsername());
                    waterQualityReports.add(report);
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
                Intent homeIntent = new Intent(QualityReportActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("username", getIntent().getExtras().getString("username"));
                homeIntent.putExtra("password", getIntent().getExtras().getString("password"));
                QualityReportActivity.this.startActivity(homeIntent);
            }
        });
    }
}
