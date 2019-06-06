package com.example.vale.fitnessapp.timer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.vale.fitnessapp.R;

/*
SoundActivity reads the users input regarding which sound they would like to have played when the
timer runs out.
 */

public class SoundActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton  rBSchoolFireAlarm, rBTomRodriguez, rBAlienAlarmDrum, rBSmokeAlarm, rBTornadoSiren;
    private Button btnReturn;
    static MediaPlayer mediaPlayer;
    static int counter = 0;
    static int id = 0;
    private AlarmCountDownTimer alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Alarm Sound");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        btnReturn = (Button)findViewById(R.id.btnReturn);
        mediaPlayer = MediaPlayer.create(this, R.raw.school_fire_alarm);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rBSchoolFireAlarm = (RadioButton)findViewById(R.id.rBSchoolFireAlarm);
        rBTomRodriguez = (RadioButton)findViewById(R.id.rBTomRodriguez);
        rBAlienAlarmDrum = (RadioButton)findViewById(R.id.rBAlienAlarmDrum);
        rBSmokeAlarm = (RadioButton)findViewById(R.id.rBSmokeAlarm);
        rBTornadoSiren = (RadioButton)findViewById(R.id.rBTornadoSiren);

        SharedPreferences sharedPref = getSharedPreferences("sound", Context.MODE_PRIVATE);
        id = sharedPref.getInt("alarm", 0);

        switch(id){
            case 0: rBSchoolFireAlarm.toggle();
                break;
            case 1: rBTomRodriguez.toggle();
                break;
            case 2: rBAlienAlarmDrum.toggle();
                break;
            case 3: rBSmokeAlarm.toggle();
                break;
            case 4: rBTornadoSiren.toggle();
                break;
        }
        btnReturn();
    }

    private void btnReturn(){
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TimerActivity.class));
            }
        });
    }

    public void rbOnclick(View view){
        int buttonId = radioGroup.getCheckedRadioButtonId();
        if(counter > 0)
            mediaPlayer.stop();
        if(buttonId == R.id.rBSchoolFireAlarm){
            id = 0;
            mediaPlayer = MediaPlayer.create(this,R.raw.school_fire_alarm);
        }else if(buttonId == R.id.rBTomRodriguez){
            id = 1;
            mediaPlayer = MediaPlayer.create(this,R.raw.tom_rodriguez);
        }else if(buttonId == R.id.rBAlienAlarmDrum){
            id = 2;
            mediaPlayer = MediaPlayer.create(this,R.raw.alien_alarm_drum);
        }else if(buttonId == R.id.rBSmokeAlarm){
            id = 3;
            mediaPlayer = MediaPlayer.create(this,R.raw.smoke_alarm);
        }else if(buttonId == R.id.rBTornadoSiren){
            id = 4;
            mediaPlayer = MediaPlayer.create(this,R.raw.tornado_siren);
        }
        mediaPlayer.start();
        alarm = new AlarmCountDownTimer(5800, 1000);
        alarm.start();
        counter++;
        SharedPreferences sharedPref = getSharedPreferences("sound", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("alarm", id);
        editor.apply();
    }

    class AlarmCountDownTimer extends CountDownTimer {

        public AlarmCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {}

        @Override
        public void onFinish() {
            if(counter == 1)
                mediaPlayer.stop();
            counter--;
        }
    }
}
