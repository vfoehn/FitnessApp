package com.example.vale.fitnessapp.personal_record;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vale.fitnessapp.R;

/*
ThisRecordActivity displays a personal record that has been created.
 */

public class ThisRecordActivity extends AppCompatActivity {

    private TextView textDiscipline, textQuantity, textComment;
    private Button btnDelete, btnEdit;
    static boolean hasSpecialIntent;
    static int mostCurrentIntent;
    private int positionOfItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_record);

        initialize();
        delete();
    }

    private void initialize(){
        if(hasSpecialIntent) {
            positionOfItem = getIntent().getIntExtra("POSITION_OF_SELECTED_ITEM", -1);
        }else{
            positionOfItem = mostCurrentIntent;
        }
        textDiscipline = (TextView)findViewById(R.id.textDiscipline);
        textQuantity = (TextView)findViewById(R.id.textQuantity);
        textComment = (TextView)findViewById(R.id.textComment2);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnEdit = (Button)findViewById(R.id.btnEdit);

        PersonalRecord record = PersonalRecordsActivity.records.get(positionOfItem);
        textDiscipline.setText(record.getDiscipline());
        String temp;
        if(record.getUnit() == 0){
            temp = String.valueOf(record.getQuantity()) + " kg";
        }else{
            temp = String.valueOf((int)record.getQuantity()) + " reps";
        }
        textQuantity.setText(temp);
        if(record.getComment().equals("")){
            textComment.setText("No comments have been found.");
        }else{
            textComment.setText(record.getComment());
        }
    }

    public void goEditRecordActivity(View view){
        Intent intent = new Intent(getApplicationContext(), EditRecordActivity.class);
        intent.putExtra("POSITION_OF_SELECTED_ITEM", positionOfItem);
        startActivity(intent);
    }

    private void delete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            for(int i = positionOfItem; i < PersonalRecordsActivity.numberOfRecords - 1; i++){
                String sharedPrefNameLow = "sharedRecord" + i;
                SharedPreferences sharedPrefLow = getSharedPreferences(sharedPrefNameLow, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefLow.edit();

                String sharedPrefNameHigh = "sharedRecord" + (i+1);
                SharedPreferences sharedPrefHigh = getSharedPreferences(sharedPrefNameHigh, Context.MODE_PRIVATE);

                editor.putString("discipline", sharedPrefHigh.getString("discipline" , "-1"));
                editor.putFloat("quantity", sharedPrefHigh.getFloat("quantity" , -1));
                editor.putInt("unit", sharedPrefHigh.getInt("unit" , -1));
                editor.putString("comment", sharedPrefHigh.getString("comment" , "-1"));
                editor.apply();
            }

            String sharedPrefName = "sharedRecord" + (PersonalRecordsActivity.numberOfRecords-1);
            SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            sharedPref.edit().remove("discipline").commit();
            sharedPref.edit().remove("quantity").commit();
            sharedPref.edit().remove("unit").commit();
            sharedPref.edit().remove("comment").commit();
            PersonalRecordsActivity.records.remove(PersonalRecordsActivity.numberOfRecords - 1);
            updateRecordList();

            SharedPreferences sharedPref2 = getSharedPreferences("sharedPrefNumberOfRecords", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref2.edit();
            editor.putInt("numberOfRecords", (PersonalRecordsActivity.numberOfRecords - 1));
            editor.apply();
            PersonalRecordsActivity.numberOfRecords--;
            Toast.makeText(getApplicationContext(), "Current record deleted", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), PersonalRecordsActivity.class));
            }
        });
    }

    private void updateRecordList(){
        PersonalRecordsActivity.records.clear();
        for(int i = 0; i < PersonalRecordsActivity.numberOfRecords; i++){
            String sharedPrefName = "sharedRecord" + i;
            SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            String discipline = sharedPref.getString("discipline", "-1");
            double quantity = sharedPref.getFloat("quantity" , -1);
            int unit = sharedPref.getInt("unit" , -1);
            String comment = sharedPref.getString("comment", "-1");
            PersonalRecord record = new PersonalRecord(discipline, quantity, unit, comment);
            PersonalRecordsActivity.records.add(record);
        }
    }
}
