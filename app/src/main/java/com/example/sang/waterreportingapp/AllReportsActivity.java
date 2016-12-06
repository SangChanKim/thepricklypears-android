package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);

        ListView waterReportsListView = (ListView) findViewById(R.id.waterReportListView);
        ArrayList<String> listItems=new ArrayList<String>();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        waterReportsListView.setAdapter(adapter);
        adapter.add("Water report 1");
        adapter.add("Water report 2");

        Button homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent homeIntent = new Intent(AllReportsActivity.this, HomeScreenActivity.class);
                AllReportsActivity.this.startActivity(homeIntent);
            }
        });
    }
}
