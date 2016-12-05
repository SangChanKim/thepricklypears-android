package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cancelIntent = new Intent(HomeScreenActivity.this, MainScreenActivity.class);

                HomeScreenActivity.this.startActivity(cancelIntent);
            }
        });
    }
}
