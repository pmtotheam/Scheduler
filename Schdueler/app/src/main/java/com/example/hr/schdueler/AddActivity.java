package com.example.hr.schdueler;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private EditText editTextName,editTextPhone;
    private Spinner specialitySpinner;
    private Button buttonSchedule;

    private String stringName, stringPhone, stringSpeciality;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        intialise();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                res.getStringArray(R.array.speciality_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialitySpinner.setAdapter(arrayAdapter);
        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringSpeciality=parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(),stringSpeciality,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        buttonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passdData();
            }
        });

    }
    private void passdData(){
        stringName=editTextName.getText().toString().trim();
        stringPhone=editTextPhone.getText().toString().trim();
        if(!TextUtils.isEmpty(stringName)&&!TextUtils.isEmpty(stringPhone)&&
                !TextUtils.isEmpty(stringSpeciality)&&
                !stringSpeciality.equals(res.getString(R.string.choose_speciality))){
            Intent i = new Intent(AddActivity.this,ScheduleActivity.class);
            i.putExtra(res.getString(R.string.dockey),stringName);
            i.putExtra(res.getString(R.string.phnkey),stringPhone);
            i.putExtra(res.getString(R.string.sclkey),stringSpeciality);
            startActivity(i);
        }
        else{
            Toast.makeText(this,"Incorrect data",Toast.LENGTH_SHORT).show();
        }
    }
    private void intialise(){
        editTextName=(EditText)findViewById(R.id.name_editText);
        editTextPhone=(EditText)findViewById(R.id.phone_editText);
        specialitySpinner=(Spinner)findViewById(R.id.speciality_spinner);
        res=getResources();
        buttonSchedule=(Button)findViewById(R.id.schedule_button);
    }
}
