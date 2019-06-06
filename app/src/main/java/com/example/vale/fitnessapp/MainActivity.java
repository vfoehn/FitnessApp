package com.example.vale.fitnessapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vale.fitnessapp.personal_record.PersonalRecordsActivity;
import com.example.vale.fitnessapp.timer.TimerActivity;

public class MainActivity extends AppCompatActivity {

    Button btnTimer, btnPersonalRecords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize(){

        btnTimer = (Button)findViewById(R.id.btnNewPersonalRecord);
        btnPersonalRecords = (Button)findViewById(R.id.btnPersonalRecords);
    }



    public void goPersonalRecordsActivity(View view){
        startActivity(new Intent(this, PersonalRecordsActivity.class));
    }

    public void goTimerActivity(View view){
        startActivity(new Intent(this, TimerActivity.class));
    }
}
