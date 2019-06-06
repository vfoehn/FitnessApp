package com.example.vale.fitnessapp.timer;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vale.fitnessapp.R;

/*
ActiveTimerActivity runs the timer. This timer is always displayed. Depending on the current status
of the timer (i.e. is it time to work out or to rest), the screen looks different.
 */

public class ActiveTimerActivity extends AppCompatActivity {

    private TextView textViewSetsLeft, textViewStatus, textViewMin, textViewSec, textView2, textView9;
    private ImageButton pausePlay, btnNightMode;
    private Status status;
    private int setsLeft;
    private double timeLeft, timeBeforePaused;
    private boolean paused;
    private CountDownTimer currentTimer;
    private ConstraintLayout layout;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Timer");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_timer);

        initialize();
        if(TimerActivity.isNightMode){
            btnNightMode.setImageResource(R.drawable.rsz_2sun);
            layout.setBackgroundColor(Color.rgb(30, 30, 30));
            textViewSetsLeft.setTextColor(Color.rgb(220,220,220));
            textViewMin.setTextColor(Color.rgb(220,220,220));
            textViewSec.setTextColor(Color.rgb(220,220,220));
            textView2.setTextColor(Color.rgb(220,220,220));
            textView9.setTextColor(Color.rgb(220,220,220));
            textViewStatus.setTextColor(Color.rgb(220,220,220));
        }
        btnNight();
        pressPausePlay();
    }

    private void initialize(){
        pausePlay = (ImageButton)findViewById(R.id.pausePlay);
        textViewSetsLeft = (TextView)findViewById(R.id.textViewSetsLeft);
        textViewStatus = (TextView)findViewById(R.id.textViewStatus);
        textViewMin = (TextView)findViewById(R.id.textViewMin);
        textViewSec = (TextView)findViewById(R.id.textViewSec);
        textView9 = (TextView)findViewById(R.id.textView9);
        textView2 = (TextView)findViewById(R.id.textView2);
        layout = (ConstraintLayout)findViewById(R.id.layout);
        btnNightMode = (ImageButton)findViewById(R.id.btnNightMode);

        setsLeft = TimerActivity.numberOfSets;
        textViewSetsLeft.setText(String.valueOf(setsLeft));
        workTimeSetter(TimerActivity.workTime);
        textViewStatus.setText("Get Ready");
        TimerActivity.timerActive = true;

        BeforeCountDownTimer getReady = new BeforeCountDownTimer(5000, 500);
        currentTimer = getReady;
        getReady.start();
        status = Status.BEFORE_WORK;
    }

    private void btnNight(){
        btnNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimerActivity.isNightMode){
                    TimerActivity.isNightMode = false;
                    btnNightMode.setImageResource(R.drawable.rsz_2sun);
                    layout.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewSetsLeft.setTextColor(Color.rgb(102,102,102));
                    textViewMin.setTextColor(Color.rgb(102,102,102));
                    textViewSec.setTextColor(Color.rgb(102,102,102));
                    textView2.setTextColor(Color.rgb(102,102,102));
                    textView9.setTextColor(Color.rgb(102,102,102));
                    textViewStatus.setTextColor(Color.rgb(102,102,102));

                }else{
                    TimerActivity.isNightMode = true;
                    btnNightMode.setImageResource(R.drawable.rsz_moon);
                    layout.setBackgroundColor(Color.rgb(30, 30, 30));
                    textViewSetsLeft.setTextColor(Color.rgb(220,220,220));
                    textViewMin.setTextColor(Color.rgb(220,220,220));
                    textViewSec.setTextColor(Color.rgb(220,220,220));
                    textView2.setTextColor(Color.rgb(220,220,220));
                    textView9.setTextColor(Color.rgb(220,220,220));
                    textViewStatus.setTextColor(Color.rgb(220,220,220));
                }
            }
        });
    }

    private void pressPausePlay(){
        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paused){
                    paused = false;
                    pausePlay.setImageResource(android.R.drawable.ic_media_pause);
                    switch(status){
                        case BEFORE_WORK:
                            BeforeCountDownTimer getReady = new BeforeCountDownTimer((int)timeBeforePaused, 500);
                            currentTimer = getReady;
                            getReady.start();
                            status = Status.BEFORE_WORK;
                            textViewStatus.setText("Get Ready");
                            break;
                        case WORKING_TIME:
                            WorkOutCountDownTimer workOut = new WorkOutCountDownTimer((int)timeBeforePaused, 500);
                            currentTimer = workOut;
                            workOut.start();
                            status = Status.WORKING_TIME;
                            textViewStatus.setTextColor(Color.rgb(204,102,255));
                            textViewStatus.setText("Work Out");
                            break;
                        case RESTING_TIME:
                            RestCountDownTimer rest = new RestCountDownTimer((int)timeBeforePaused, 500);
                            currentTimer = rest;
                            rest.start();
                            status = Status.RESTING_TIME;
                            textViewStatus.setTextColor(Color.rgb(61,96,158));
                            textViewStatus.setText("Rest");
                            break;
                    }
                }else{
                    paused = true;
                    pausePlay.setImageResource(android.R.drawable.ic_media_play);
                    timeBeforePaused = timeLeft;
                    currentTimer.cancel();
                    textViewStatus.setText("Paused");
                    textViewStatus.setTextColor(Color.rgb(102,102,102));
                }
            }
        });
    }

    private void workOutTimer(){
        WorkOutCountDownTimer workOut = new WorkOutCountDownTimer(TimerActivity.workTime*1000, 500);
        int chosenSound = chooseSound();
        mediaPlayer = MediaPlayer.create(this, chosenSound);
        currentTimer = workOut;
        workOut.start();
        status = Status.WORKING_TIME;
        textViewStatus.setText("Work Out");
        textViewStatus.setTextColor(Color.rgb(204,102,255));
        textViewSetsLeft.setText(String.valueOf(setsLeft--));
    }

    private int chooseSound(){
        switch(SoundActivity.id){
            case 0: return R.raw.school_fire_alarm;
            case 1: return R.raw.tom_rodriguez;
            case 2: return R.raw.alien_alarm_drum;
            case 3: return R.raw.smoke_alarm;
            case 4: return R.raw.tornado_siren;
            default: return R.raw.school_fire_alarm;
        }
    }

    private void restTimer(){
        RestCountDownTimer rest = new RestCountDownTimer(TimerActivity.restTime*1000, 500);
        currentTimer = rest;
        rest.start();
        status = Status.RESTING_TIME;
        textViewStatus.setTextColor(Color.rgb(61,96,158));
        textViewStatus.setText("Rest");
        textViewSetsLeft.setText(String.valueOf(setsLeft));
    }

    private void workTimeSetter(int time) {
        if ((time / 60) < 10) {
            textViewMin.setText("0" + String.valueOf(time / 60));
        } else {
            textViewMin.setText(String.valueOf(time / 60));
        }
        if ((time % 60) < 10) {
            textViewSec.setText("0" + String.valueOf(time % 60));
        } else {
            textViewSec.setText(String.valueOf(time % 60));
        }
    }

    class BeforeCountDownTimer extends CountDownTimer{

        public BeforeCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft = millisUntilFinished;
            workTimeSetter((int)(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            workOutTimer();
        }
    }

    class WorkOutCountDownTimer extends CountDownTimer{

        public WorkOutCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft = millisUntilFinished;
            workTimeSetter((int)(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            if(setsLeft > 0) {
                AlarmCountDownTimer alarm = new AlarmCountDownTimer(5800,1000);
                mediaPlayer.start();
                alarm.start();
                restTimer();
            }else{
                AlarmCountDownTimer alarm = new AlarmCountDownTimer(5800,1000);
                mediaPlayer.start();
                alarm.start();
                startActivity(new Intent(getApplicationContext(), TimerDoneActivity.class));
            }

        }
    }

    class RestCountDownTimer extends CountDownTimer{

        public RestCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft = millisUntilFinished;
            workTimeSetter((int)(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            if(setsLeft > 0) {
                workOutTimer();
            }
        }
    }

    class AlarmCountDownTimer extends CountDownTimer{

        public AlarmCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {}

        @Override
        public void onFinish() {
            mediaPlayer.stop();
        }
    }


}

