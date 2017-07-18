package com.uclk.shani.youseelk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uclk.shani.youseelk.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavePlanAs extends AppCompatActivity {

    EditText pname;
    String planName;
    DatabaseReference rootRef;
    private  String[] arrayLocs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_plan_as);

        arrayLocs = getIntent().getStringArrayExtra("ADDED_LOCATIONS");
        rootRef = FirebaseDatabase.getInstance().getReference();

        pname = (EditText) findViewById(R.id.etPlanName);
        Button savePname = (Button) findViewById(R.id.btnSavePlanName);

        savePname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planName = pname.getText().toString();
                saveLocation();
                finish();
            }
        });


    }

    public void saveLocation(){
        List locList = new ArrayList<String>(Arrays.asList(arrayLocs));
        DatabaseReference locations = rootRef.child("locations");
        locations.child(planName).setValue(locList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"locations added",Toast.LENGTH_LONG).show();
            }
        });
    }
}
