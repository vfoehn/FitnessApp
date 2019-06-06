package com.example.vale.fitnessapp.personal_record;

import android.content.Context;
import android.content.Intent;
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
EditRecordActivity reads the user's input that specifies a change in an already existing personal
record. Subsequently, this updated personal record is stored.
 */

public class EditRecordActivity extends AppCompatActivity {

    private EditText editTextDiscipline, editTextQuantity, editTextComments;
    private Button btnSave;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private int spinnerPosition;
    private int positionOfItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit Record");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        initialize();
        save();
    }

    private void initialize() {
        ThisRecordActivity.hasSpecialIntent = false;
        positionOfItem = getIntent().getIntExtra("POSITION_OF_SELECTED_ITEM", -1);
        PersonalRecord record = PersonalRecordsActivity.records.get(positionOfItem);

        editTextDiscipline = (EditText) findViewById(R.id.editTextDiscipline);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextComments = (EditText) findViewById(R.id.editTextComments);
        btnSave = (Button) findViewById(R.id.btnSave);
        spinner = (Spinner) findViewById(R.id.spinner);

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
        editTextDiscipline.setText(record.getDiscipline());
        if(record.getUnit() == 0){
            editTextQuantity.setText(String.valueOf(record.getQuantity()));
        }else{
            editTextQuantity.setText(String.valueOf((int)record.getQuantity()));
        }
        spinner.setSelection(record.getUnit());
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
                    quantity = Double.valueOf(editTextQuantity.getText().toString());
                }
                comment += editTextComments.getText().toString();

                String sharedPrefName = "sharedRecord" + positionOfItem;
                SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("discipline", discipline);
                editor.putFloat("quantity", (float)quantity);
                editor.putInt("unit", spinnerPosition);
                editor.putString("comment", comment);
                editor.apply();
                PersonalRecord record = PersonalRecordsActivity.records.get(positionOfItem);
                record.setDiscipline(discipline);
                record.setQuantity(quantity);
                record.setUnit(spinnerPosition);
                record.setComment(comment);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditRecordActivity.this, PersonalRecordsActivity.class));
            }
        });
    }
}
