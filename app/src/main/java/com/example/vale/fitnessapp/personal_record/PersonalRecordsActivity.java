package com.example.vale.fitnessapp.personal_record;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vale.fitnessapp.R;

import java.util.LinkedList;

/*
PersonalRecordsActivity serves as the main activity for everything that has to do with personal
records. It loads the stored records into the list 'records'. It then displays all personal records
in a scrollable list.
 */

public class PersonalRecordsActivity extends AppCompatActivity {

    static int  numberOfRecords;
    static LinkedList<PersonalRecord> records = new LinkedList<PersonalRecord>();
    private ListView listViewPersonalRecords;
    private TextView textViewNoRecords;
    private String[] disciplineArray, commentArray;
    private double[] quantityArray;
    private int[] unitArray;
    private ImageButton btnAdd;
    private Button btnDeleteAllRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Personal Records");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_records);

        initialize();
    }

    private void initialize(){
        btnAdd = (ImageButton)findViewById(R.id.btnNewPersonalRecord);
        btnDeleteAllRecords = (Button)findViewById(R.id.btnDeleteAllRecords);
        listViewPersonalRecords = (ListView)findViewById(R.id.listViewPersonalRecords);
        textViewNoRecords = (TextView)findViewById(R.id.textViewNoRecords);

        checkNumberOfRecords();
        initializeRecordList();
        disciplineArray = new String[numberOfRecords];
        quantityArray = new double[numberOfRecords];
        unitArray = new int[numberOfRecords];
        commentArray = new String[numberOfRecords];

        if(numberOfRecords == 0){
            textViewNoRecords.setText("There are currently no records.");
        }else {
            constructList();
            listeningList();
            listeningButton();
        }
    }

    private void checkNumberOfRecords(){
        SharedPreferences sharedPref = getSharedPreferences("sharedPrefNumberOfRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int temp = sharedPref.getInt("numberOfRecords", -1);
        if(temp == -1){
            editor.putInt("numberOfRecords", 0);
            editor.apply();
            numberOfRecords = 0;
        }else{
            numberOfRecords = temp;
        }
    }

    private void initializeRecordList(){
        // Load content from phone storage into the list of records.
        records.clear();
        for(int i = 0; i < numberOfRecords; i++){
            String sharedPrefName = "sharedRecord" + i;
            SharedPreferences sharedPref = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            String discipline = sharedPref.getString("discipline", "-1");
            double quantity = sharedPref.getFloat("quantity", -1);
            int unit = sharedPref.getInt("unit", -1);
            String comment = sharedPref.getString("comment" , "-1");
            PersonalRecord record = new PersonalRecord(discipline,quantity,unit,comment);
            records.add(record);
        }
    }

    private void listeningButton(){
        btnDeleteAllRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalRecordsActivity.this, PopDeleteAllRecords.class));
            }
        });
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void goNewPersonalRecordActivity(View view){
        startActivity(new Intent(getApplicationContext(), NewPersonalRecordActivity.class));
    }

    private void listeningList(){
        listViewPersonalRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int recordNum = numberOfRecords - position - 1;
                Intent intent = new Intent(view.getContext(), ThisRecordActivity.class);
                intent.putExtra("POSITION_OF_SELECTED_ITEM", recordNum);
                ThisRecordActivity.hasSpecialIntent = true;
                ThisRecordActivity.mostCurrentIntent = position;
                startActivity(intent);
            }
        });
    }

    private void constructList(){
        turnListIntoArray();
        CustomAdapter customAdapter = new CustomAdapter();
        listViewPersonalRecords.setAdapter(customAdapter);
    }

    private void turnListIntoArray(){
        for(int i = 0; i < numberOfRecords; i++){
            PersonalRecord record = records.get(numberOfRecords - i - 1);
            disciplineArray[i] = record.getDiscipline();
            quantityArray[i] = record.getQuantity();
            unitArray[i] = record.getUnit();
            commentArray[i] = record.getComment();
        }
    }

    // CustomAdapter is used to construct a listView from the list of records.
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return numberOfRecords;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.personal_records_listview_layout, null);
            TextView textDiscipline = (TextView)view.findViewById(R.id.textDiscipline);
            TextView textQuantity = (TextView)view.findViewById(R.id.textQuantity);

            textDiscipline.setText(String.valueOf(disciplineArray[i]));
            String temp;
            if(unitArray[i] == 0){
                temp = String.valueOf(quantityArray[i]) + " kg";
            }else{
                temp = String.valueOf((int)quantityArray[i]) + " reps";
            }
            textQuantity.setText(temp);
            return view;
        }
    }
}

