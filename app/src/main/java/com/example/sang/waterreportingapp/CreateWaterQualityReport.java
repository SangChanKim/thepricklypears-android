package com.example.sang.waterreportingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sang.waterreportingapp.model.QualityCondition;

import java.util.ArrayList;

public class CreateWaterQualityReport extends AppCompatActivity {

    private String username;
    private int reportNum;
    private String date;

    private EditText etLocationName;
    private EditText etLat;
    private EditText etLng;

    private EditText etVirus;
    private EditText etContaminant;

    private Spinner qualitySpinner;

    public EditText getEtVirus() {
        return etVirus;
    }

    public EditText getEtContaminant() {
        return etContaminant;
    }

    public Spinner getQualitySpinner() {
        return qualitySpinner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_water_quality_report);



        final Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create water quality report object
                System.out.println("Creating resulting intent");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("username", getUsername());
                resultIntent.putExtra("reportNum", getReportNum());
                resultIntent.putExtra("lat", Double.parseDouble(getEtLat().getText().toString()));
                resultIntent.putExtra("long", Double.parseDouble(getEtLng().getText().toString()));
                resultIntent.putExtra("locName", getEtLocationName().getText().toString());
                resultIntent.putExtra("qcond", getQualitySpinner().getSelectedItem().toString() /*getEtType().getText().toString()*/);
                resultIntent.putExtra("virusPPM", Integer.parseInt(getEtVirus().getText().toString()));
                resultIntent.putExtra("contaminantPPM", Integer.parseInt(getEtContaminant().getText().toString()));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });


        final Button cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Go back to map view
                setResult(Activity.RESULT_OK);
                finish();
            }
        });



        final TextView tvReportNum = (TextView) findViewById(R.id.reportNumTextView);
        this.reportNum = getIntent().getExtras().getInt("reportNum");
        tvReportNum.setText("" + reportNum);

        final TextView tvUserName = (TextView) findViewById(R.id.usernameTextView);
        this.username = getIntent().getExtras().getString("username");
        tvUserName.setText(this.username);

        final TextView tvDate = (TextView) findViewById(R.id.dateTextView);
        this.date = getIntent().getExtras().getString("dateString");
        tvDate.setText(this.date.toString());

        etLat = (EditText) findViewById(R.id.latEditText);
        etLng = (EditText) findViewById(R.id.longEditText);

        etLat.setText("" + getIntent().getExtras().getDouble("lat"));
        etLng.setText("" + getIntent().getExtras().getDouble("long"));

        etLocationName = (EditText) findViewById(R.id.locationEditText);

        etVirus = (EditText) findViewById(R.id.virusPPMEditText);
        etContaminant = (EditText) findViewById(R.id.contaminantPPMEditText);

        qualitySpinner = (Spinner) findViewById(R.id.qualitySpinner);

        ArrayList<CharSequence> types = new ArrayList<CharSequence>();
        QualityCondition[] raw = QualityCondition.values();
        System.out.println("ffff");
        for (int i = 0; i < raw.length; i++) {
            System.out.println(raw[i].toString());
            types.add(raw[i].toString());
        }

        ArrayAdapter<CharSequence> a1 = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, types);

        // Specify the layout to use when the list of choices appears
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        qualitySpinner.setAdapter(a1);


    }


    public String getUsername() {
        return username;
    }

    public int getReportNum() {
        return reportNum;
    }

    public String getDate() {
        return date;
    }

    public EditText getEtLocationName() {
        return etLocationName;
    }

    public EditText getEtLat() {
        return etLat;
    }

    public EditText getEtLng() {
        return etLng;
    }

}
