package com.example.vale.fitnessapp.personal_record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vale.fitnessapp.R;

/*
PopDeleteAllRecords provides a pop-up message that asks the user whether they are sure that they
want to delete all records.
 */

public class PopDeleteAllRecords extends Activity {

    private Button btnYes, btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_delete_all_records);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.6), (int)(height * 0.2));

        btnYes = (Button)findViewById(R.id.btnYes);
        btnNo = (Button)findViewById(R.id.btnNo);
        btnNo();
        btnYes();
    }

    private void btnNo(){
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void btnYes(){
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < PersonalRecordsActivity.numberOfRecords; i++) {
                    String sharedPrefName = "sharedRecord" + i;
                    SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                    sharedPref.edit().remove("discipline").commit();
                    sharedPref.edit().remove("quantity").commit();
                    sharedPref.edit().remove("unit").commit();
                    sharedPref.edit().remove("comment").commit();
                }
                PersonalRecordsActivity.records.clear();
                SharedPreferences sharedPref2 = getSharedPreferences("sharedPrefNumberOfRecords", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref2.edit();
                editor.putInt("numberOfRecords", 0);
                editor.apply();
                PersonalRecordsActivity.numberOfRecords = 0;
                Toast.makeText(getApplicationContext(), "All records deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PopDeleteAllRecords.this, PersonalRecordsActivity.class));
            }
        });
    }
}
