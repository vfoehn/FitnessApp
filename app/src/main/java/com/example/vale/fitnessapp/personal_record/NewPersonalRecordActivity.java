package com.example.vale.fitnessapp.personal_record;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vale.fitnessapp.R;

/*
NewPersonalRecordActivity reads the user's input that specifies a new personal record. Subsequently,
this personal record is added and stored with the list of previous records.
 */

public class NewPersonalRecordActivity extends AppCompatActivity {

    private EditText editTextDiscipline, editTextQuantity, editTextComments;
    private Button btnSave;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("New Personal Record");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_personal_record);

        initialize();
        save();
    }

    private void initialize(){
        editTextDiscipline = (EditText)findViewById(R.id.editTextDiscipline);
        editTextQuantity = (EditText)findViewById(R.id.editTextQuantity);
        editTextComments = (EditText)findViewById(R.id.editTextComments);
        btnSave = (Button)findViewById(R.id.btnSave);
        spinner = (Spinner)findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.unit_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void save(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discipline = "";
                double quantity;
                String comment = "";

                if(editTextDiscipline.getText().toString() == ""){
                    Toast.makeText(getApplicationContext(), "Everything apart from the comments needs to be filled in.", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    discipline += editTextDiscipline.getText().toString();
                }
                if(editTextQuantity.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Everything apart from the comments needs to be filled in.", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    try {
                        quantity = Double.valueOf(editTextQuantity.getText().toString());
                    }catch(NumberFormatException e){
                        Toast.makeText(getApplicationContext(), "Quantity needs to be filled out with a number.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                comment += editTextComments.getText().toString();
                int equivalentPosition = checkForPriorEquivalent(discipline);
                if(equivalentPosition != -1){
                    String sharedPrefName = "sharedRecord" + equivalentPosition;
                    SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putFloat("quantity", (float)quantity);
                    editor.putInt("unit", spinnerPosition);
                    editor.putString("comment", comment);
                    editor.apply();
                    PersonalRecord record = PersonalRecordsActivity.records.get(equivalentPosition);
                    record.setQuantity(quantity);
                    record.setUnit(spinnerPosition);
                    record.setComment(comment);
                    Toast.makeText(getApplicationContext(), "A record for this dicipline already exists and has now been overwritten.", Toast.LENGTH_LONG).show();
                    return;
                }
                String sharedPrefName = "sharedRecord" + PersonalRecordsActivity.numberOfRecords;
                SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("discipline", discipline);
                editor.putFloat("quantity", (float)quantity);
                editor.putInt("unit", spinnerPosition);
                editor.putString("comment", comment);
                editor.apply();

                updateNumberOfRecords();
                updateRecordsList();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    // Check if a record with the same description has already saved.
    private int checkForPriorEquivalent(String discipline){
        for(int i = 0; i < PersonalRecordsActivity.numberOfRecords; i++){
            String discipline_in_list = PersonalRecordsActivity.records.get(i).getDiscipline();
            if(discipline_in_list.equals(discipline))
                return i;
        }
        return -1;
    }

    private void updateRecordsList(){
        PersonalRecordsActivity.records.clear();
        for(int i = 0; i < PersonalRecordsActivity.numberOfRecords; i++){
            String sharedPrefName = "sharedRecord" + i;
            SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            String discipline = sharedPref.getString("discipline", "-1");
            double quantity = sharedPref.getFloat("quantity", -1);
            int unit = sharedPref.getInt("unit", -1);
            String comment = sharedPref.getString("comment" , "-1");
            PersonalRecord record = new PersonalRecord(discipline,quantity,unit,comment);
            PersonalRecordsActivity.records.add(record);
        }
    }

    private void updateNumberOfRecords(){
        SharedPreferences sharedPref = getSharedPreferences("sharedPrefNumberOfRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("numberOfRecords", PersonalRecordsActivity.numberOfRecords + 1);
        editor.apply();
        PersonalRecordsActivity.numberOfRecords++;
    }
}
