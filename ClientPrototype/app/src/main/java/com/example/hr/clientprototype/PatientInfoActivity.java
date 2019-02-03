package com.example.hr.clientprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PatientInfoActivity extends AppCompatActivity {
    private EditText editTextPatientName,editTextPhone,editTextDoA;
    private String docidkey,date,spec,docname;
    private Button buttonSetAppointment;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef,databasechildappoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        getExtraStringFromIntent();
        intialise();
        buttonSetAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataOnFirebase();
                Toast.makeText(PatientInfoActivity.this,"Appointment Set",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PatientInfoActivity.this,MainActivity.class));
                finish();
            }
        });
    }
    public void postDataOnFirebase(){
        String patientname=editTextPatientName.getText().toString().trim();
        String phone=editTextPhone.getText().toString().trim();
        String dateofappoint=editTextDoA.getText().toString().trim();
        if(!TextUtils.isEmpty(patientname)&&!TextUtils.isEmpty(phone)&&
                !TextUtils.isEmpty(dateofappoint)) {
            Map<String, String> map = new HashMap<>();
            map.put("Patient Name", patientname);
            map.put("Email", phone);
            map.put("Date of Appointment", dateofappoint);
            //map.put("clientid",mAuth.getCurrentUser().getUid());
            databaseRef.child(date).child(docidkey).push().setValue(map);
            map.remove("Patient Name");
            map.remove("Email");
            map.put("Speciality",spec);
            map.put("Doctor Name",docname);
            databasechildappoint.child(mAuth.getCurrentUser().getUid()).push().setValue(map);
        }
        else{
            Toast.makeText(PatientInfoActivity.this,"Incorrect Data provided",Toast.LENGTH_SHORT).show();
        }
    }
    private void intialise() {
        editTextPatientName=(EditText) findViewById(R.id.editTextPatientname);
        editTextPhone=(EditText)findViewById(R.id.editTextPatientphone);
        editTextDoA=(EditText)findViewById(R.id.editTextDateOfAppointment);
        editTextDoA.setText(date);
        databaseRef= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/Appointments");
        databasechildappoint= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/ChildAppoint");
        buttonSetAppointment=(Button)findViewById(R.id.set_appointment_button);
        mAuth=FirebaseAuth.getInstance();
        editTextPhone.setText(mAuth.getCurrentUser().getEmail());

    }

    public void getExtraStringFromIntent(){
        Intent i=getIntent();
        date=i.getStringExtra("date");
        docidkey=i.getStringExtra("docid");
        spec=i.getStringExtra("spec");
        docname=i.getStringExtra("docname");

        //Toast.makeText(this,date,Toast.LENGTH_SHORT).show();'
    }

}
