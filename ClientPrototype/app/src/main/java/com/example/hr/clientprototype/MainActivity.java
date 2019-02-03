package com.example.hr.clientprototype;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerSpeciality,spinnerDoctor;
    private Resources res;
    private String stringSpeciality;

    private ArrayList<String> stringNameList,stringDoctoIdList;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialise();
        settingAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser==null){
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void settingAdapter(){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                res.getStringArray(R.array.speciality_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpeciality.setAdapter(arrayAdapter);
        spinnerSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringSpeciality=parent.getItemAtPosition(position).toString();
                if(!TextUtils.isEmpty(stringSpeciality)&&
                        !stringSpeciality.equals(res.getString(R.string.choose_speciality))) {
                    spinnerDoctor.setVisibility(View.VISIBLE);
                    stringNameList.clear();
                    stringDoctoIdList.clear();
                    stringNameList.add(res.getString(R.string.choose_doctor));
                    ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, stringNameList);
                    arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDoctor.setAdapter(arrayAdapter1);
                    setSpinnerDoctorData(stringSpeciality, arrayAdapter1);
                    spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position!=0) {
                                String s1 = stringDoctoIdList.get(position - 1);
                                Intent i =new Intent(MainActivity.this,ScheduleActivity.class);
                                i.putExtra(res.getString(R.string.docid),s1);
                                i.putExtra("DoctorName",stringNameList.get(position));
                                i.putExtra("Docspec",stringSpeciality);
                                startActivity(i);
                                //Toast.makeText(MainActivity.this, s1, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    public void setSpinnerDoctorData(String s, final ArrayAdapter<String> arrayAdapter1){
        databaseReference.child(s).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,String> user=(Map<String,String>)dataSnapshot.getValue();
                stringNameList.add(user.get("Name"));
                stringDoctoIdList.add(user.get("docid"));
                arrayAdapter1.notifyDataSetChanged();
                //Log.e("err",user.get("Name"));
                //Log.e("err",user.get("docid"));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    public void intialise(){
        spinnerSpeciality=(Spinner)findViewById(R.id.speciality_spinner_main);
        spinnerDoctor=(Spinner)findViewById(R.id.doctor_spinner);
        spinnerDoctor.setVisibility(View.INVISIBLE);
        res=getResources();
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/DocSpeciaity");
        stringNameList=new ArrayList<String>();
        stringDoctoIdList=new ArrayList<String>();
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout){
            mAuth.signOut();
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.action_appointment){
            Intent i=new Intent(MainActivity.this,AppointActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
