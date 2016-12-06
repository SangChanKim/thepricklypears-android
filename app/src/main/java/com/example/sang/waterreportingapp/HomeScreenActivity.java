package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreenActivity extends AppCompatActivity {

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final Button updateProfile = (Button) findViewById(R.id.updateProfileButton);
        updateProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent updateProfileIntent = new Intent(HomeScreenActivity.this, UpdateProfileActivity.class);
                updateProfileIntent.putExtra("username", getIntent().getExtras().getString("username"));
                updateProfileIntent.putExtra("password", getIntent().getExtras().getString("password"));
                HomeScreenActivity.this.startActivity(updateProfileIntent);
            }
        });

        final Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewMapIntent = new Intent(HomeScreenActivity.this, MapsActivity.class);
                viewMapIntent.putExtra("username", getIntent().getExtras().getString("username"));
                HomeScreenActivity.this.startActivity(viewMapIntent);
            }
        });

        final Button qualityReportButton = (Button) findViewById(R.id.qualityReportButton);
        qualityReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if this user is a manager
                DatabaseReference ref = db.child("users").child(getIntent().getExtras().getString("username")).child("userType");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getValue().toString().equals("Manager")) {
                            Intent viewQualityReportIntent = new Intent(HomeScreenActivity.this, QualityReportActivity.class);
                            HomeScreenActivity.this.startActivity(viewQualityReportIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                    }
                });
            }
        });

        final Button allReportsButton = (Button) findViewById(R.id.allReportsButton);
        allReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewAllReportsIntent = new Intent(HomeScreenActivity.this, AllReportsActivity.class);
                HomeScreenActivity.this.startActivity(viewAllReportsIntent);
            }
        });


        final Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent logoutIntent = new Intent(HomeScreenActivity.this, MainScreenActivity.class);
                HomeScreenActivity.this.startActivity(logoutIntent);
            }
        });
    }
}
