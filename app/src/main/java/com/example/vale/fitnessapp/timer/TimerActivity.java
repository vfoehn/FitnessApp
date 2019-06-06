package com.example.vale.fitnessapp.timer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vale.fitnessapp.R;

/*
TimerActivity reads the input from the user regarding the timer settings. It will then pass this
information to ActiveTimerActivity where the timer will be run.
 */

public class TimerActivity extends AppCompatActivity {

    private Button btnSub1, btnSub2, btnSub3, btnAdd1, btnAdd2, btnAdd3, btnStart;
    private ImageButton btnNightMode, btnSound;
    private TextView textViewSets, textView8, textView13;
    private EditText textViewSet, textViewWorkIntervalMin, textViewWorkIntervalSec, textViewRestIntervalMin, textViewRestIntervalSec;
    static int workTime, numberOfSets, restTime;
    static boolean timerActive, isNightMode;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Timer");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initialize();
        if(isNightMode){
            btnNightMode.setImageResource(R.drawable.rsz_2sun);
            layout.setBackgroundColor(Color.rgb(30, 30, 30));
            textViewSets.setTextColor(Color.rgb(102,102,102));
            textView8.setTextColor(Color.rgb(102,102,102));
            textView13.setTextColor(Color.rgb(102,102,102));
        }
        btnNight();
        btnSet();
        constantSet();
        btnWork();
        constantWork();
        btnRest();
        constantRest();
        btnStart();
        btnSound();

        // Load previously selected sound from the phone's storage
        SharedPreferences sharedPref = getSharedPreferences("sound", Context.MODE_PRIVATE);
        SoundActivity.id = sharedPref.getInt("alarm", 0);
    }

    private void initialize(){
        btnNightMode = (ImageButton)findViewById(R.id.btnNightMode);
        btnSound = (ImageButton)findViewById(R.id.btnSound);
        layout = (ConstraintLayout)findViewById(R.id.layout);
        btnSub1 = (Button)findViewById(R.id.btnSub1);
        btnSub2 = (Button)findViewById(R.id.btnSub2);
        btnSub3 = (Button)findViewById(R.id.btnSub3);
        btnAdd1 = (Button)findViewById(R.id.btnAdd1);
        btnAdd2 = (Button)findViewById(R.id.btnAdd2);
        btnAdd3 = (Button)findViewById(R.id.btnAdd3);
        btnStart = (Button)findViewById(R.id.btnStart);

        textViewSet = (EditText) findViewById(R.id.textViewSet);
        textViewWorkIntervalMin = (EditText)findViewById(R.id.textViewWorkIntervalMin);
        textViewWorkIntervalSec = (EditText)findViewById(R.id.textViewWorkIntervalSec);
        textViewRestIntervalMin = (EditText)findViewById(R.id.textViewRestIntervalMin);
        textViewRestIntervalSec = (EditText)findViewById(R.id.textViewRestIntervalSec);
        textViewSets = (TextView)findViewById(R.id.textViewSets);
        textView8 = (TextView)findViewById(R.id.textView8);
        textView13 = (TextView)findViewById(R.id.textView13);

        isNightMode = false;
        numberOfSets = 1;
        textViewSet.setText(String.valueOf(numberOfSets));
        workTime = 60;
        workTimeSetter(workTime);
        restTime = 30;
        restTimeSetter(restTime);

        if(SoundActivity.counter > 0){
            SoundActivity.mediaPlayer.stop();
        }
    }

    private void btnNight(){
        btnNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNightMode){
                    isNightMode = true;
                    btnNightMode.setImageResource(R.drawable.rsz_moon);
                    layout.setBackgroundColor(Color.rgb(30, 30, 30));
                    textViewSets.setTextColor(Color.rgb(102,102,102));
                    textView8.setTextColor(Color.rgb(102,102,102));
                    textView13.setTextColor(Color.rgb(102,102,102));
                }else{
                    isNightMode = false;
                    btnNightMode.setImageResource(R.drawable.rsz_2sun);
                    layout.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewSets.setTextColor(Color.rgb(102,102,102));
                    textView8.setTextColor(Color.rgb(102,102,102));
                    textView13.setTextColor(Color.rgb(102,102,102));
                }
            }
        });
    }

    private void btnStart(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewSet.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext() ,"Number of sets field cannot be empty.", Toast.LENGTH_LONG).show();
                }else if(getNumber(textViewSet) <= 0){
                    Toast.makeText(getApplicationContext() ,"Choose at least 1 set.", Toast.LENGTH_LONG).show();
                }
                else if(getNumber(textViewWorkIntervalMin) >= 60 || getNumber(textViewWorkIntervalSec) >= 60 || getNumber(textViewRestIntervalMin) >= 60 || getNumber(textViewRestIntervalSec) >= 60){
                    Toast.makeText(getApplicationContext() ,"Make sure the intervals do not exceed 60 minutes and a minute consists of 60 seconds at most.", Toast.LENGTH_LONG).show();
                }else {
                    numberOfSets = Integer.valueOf(textViewSet.getText().toString());
                    workTime = Integer.valueOf(textViewWorkIntervalMin.getText().toString()) * 60 + Integer.valueOf(textViewWorkIntervalSec.getText().toString());
                    restTime = Integer.valueOf(textViewRestIntervalMin.getText().toString()) * 60 + Integer.valueOf(textViewRestIntervalSec.getText().toString());

                    startActivity(new Intent(getApplicationContext(), ActiveTimerActivity.class));
                }
            }
        });
    }

    private int getNumber(EditText text){
        return Integer.valueOf(text.getText().toString());
    }

    private void btnSound(){
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SoundActivity.class));
            }
        });
    }

    private void btnSet(){
        btnSub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfSets > 1){
                    textViewSet.setText(String.valueOf(--numberOfSets));
                }
            }
        });

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSet.setText(String.valueOf(++numberOfSets));
            }
        });
    }

    private void constantSet(){
        textViewSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(!input.equals("")){
                    numberOfSets = Integer.valueOf(s.toString());
                }else{
                    numberOfSets = 0;
                }

            }
        });
    }

    private void btnWork(){
        final int STEP = 30;

        btnSub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workTime > STEP) {
                    workTime -= STEP;
                    workTimeSetter(workTime);
                }else{
                    workTime = 1;
                    workTimeSetter(workTime);
                }
            }
        });

        btnAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int SEC_IN_HOUR = 3600;
                if(workTime < SEC_IN_HOUR - STEP) {
                    workTime += STEP;
                    workTimeSetter(workTime);
                }else{
                    workTime = SEC_IN_HOUR - 1;
                    workTimeSetter(workTime);
                }
            }
        });
    }

    private void constantWork(){
        textViewWorkIntervalMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(!input.equals("")){
                    workTime = Integer.valueOf(input) * 60 + Integer.valueOf(textViewWorkIntervalSec.getText().toString());
                }
            }
        });

        textViewWorkIntervalSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(!input.equals("")){
                    workTime = Integer.valueOf(textViewWorkIntervalMin.getText().toString()) * 60 + Integer.valueOf(input);
                }
            }
        });
    }

    private void btnRest(){
        final int STEP = 10;

        btnSub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(restTime > STEP) {
                    restTime -= STEP;
                    restTimeSetter(restTime);
                }else{
                    restTime = 1;
                    restTimeSetter(restTime);
                }
            }
        });

        btnAdd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int SEC_IN_HOUR = 3600;
                if(restTime < SEC_IN_HOUR - STEP) {
                    restTime += STEP;
                    restTimeSetter(restTime);
                }else{
                    restTime = SEC_IN_HOUR - 1;
                    restTimeSetter(restTime);
                }
            }
        });
    }

    private void constantRest(){
        textViewRestIntervalMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(!input.equals("")){
                    restTime = Integer.valueOf(input) * 60 + Integer.valueOf(textViewRestIntervalSec.getText().toString());
                }
            }
        });

        textViewRestIntervalSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(!input.equals("")){
                    restTime = Integer.valueOf(textViewRestIntervalMin.getText().toString()) * 60 + Integer.valueOf(input);
                }
            }
        });
    }

    private void workTimeSetter(int time){
        if((time / 60) < 10){
            textViewWorkIntervalMin.setText("0" + String.valueOf(time / 60));
        }else{
            textViewWorkIntervalMin.setText(String.valueOf(time / 60));
        }
        if((time % 60) < 10){
            textViewWorkIntervalSec.setText("0" + String.valueOf(time % 60));
        }else{
            textViewWorkIntervalSec.setText(String.valueOf(time % 60));
        }
    }

    private void restTimeSetter(int time){
        if((time / 60) < 10){
            textViewRestIntervalMin.setText("0" + String.valueOf(time / 60));
        }else{
            textViewRestIntervalMin.setText(String.valueOf(time / 60));
        }
        if((time % 60) < 10){
            textViewRestIntervalSec.setText("0" + String.valueOf(time % 60));
        }else{
            textViewRestIntervalSec.setText(String.valueOf(time % 60));
        }
    }
}
