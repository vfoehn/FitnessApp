package com.example.vale.fitnessapp.timer;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vale.fitnessapp.R;

/*
TimerDoneActivity displays the fact that the workout is done. It gives the user the option of
repeating the timer with the same specifications.
 */

public class TimerDoneActivity extends AppCompatActivity {

    private Button btnRepeat;
    private ImageButton btnNightMode;
    private ConstraintLayout layout;
    private TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_done);

        TimerActivity.timerActive = false;
        btnRepeat = (Button)findViewById(R.id.btnRepeat);
        btnNightMode = (ImageButton)findViewById(R.id.btnNightMode);
        layout = (ConstraintLayout)findViewById(R.id.layout);
        textView5 = (TextView)findViewById(R.id.textView5);

        if(TimerActivity.isNightMode){
            btnNightMode.setImageResource(R.drawable.rsz_2sun);
            layout.setBackgroundColor(Color.rgb(30, 30, 30));
            textView5.setTextColor(Color.rgb(220,220,220));
        }
        btnNightMode();
        btnRepeat();
    }

    private void btnNightMode(){
        btnNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimerActivity.isNightMode){
                    TimerActivity.isNightMode = false;
                    btnNightMode.setImageResource(R.drawable.rsz_2sun);
                    layout.setBackgroundColor(Color.rgb(255, 255, 255));
                    textView5.setTextColor(Color.rgb(102,102,102));
                }else{
                    TimerActivity.isNightMode = true;
                    btnNightMode.setImageResource(R.drawable.rsz_moon);
                    layout.setBackgroundColor(Color.rgb(30, 30, 30));
                    textView5.setTextColor(Color.rgb(220,220,220));
                }
            }
        });
    }

    private void btnRepeat(){
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActiveTimerActivity.class));
            }
        });
    }
}
