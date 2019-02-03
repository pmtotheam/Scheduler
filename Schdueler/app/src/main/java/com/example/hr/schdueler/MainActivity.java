package com.example.hr.schdueler;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerSpeciality,spinnerDoctor,spinnerSpeciality2,spinnerDoctor2;
    private Resources res;
    private String stringSpeciality,stringSpeciality2;
    private TextView t1,t2;

    private ArrayList<String> stringNameList,stringDoctoIdList;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Calendar c;
    private DatePickerDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        intialise();
        settingAdapter();
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
                if(stringSpeciality.equals(res.getString(R.string.choose_speciality))){
                    t2.setVisibility(View.VISIBLE);
                    spinnerDoctor.setVisibility(View.GONE);
                    spinnerSpeciality2.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(stringSpeciality)&&
                        !stringSpeciality.equals(res.getString(R.string.choose_speciality))) {
                    spinnerDoctor.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.GONE);
                    spinnerSpeciality2.setVisibility(View.GONE);
                    spinnerDoctor2.setVisibility(View.GONE);
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
                                Intent i =new Intent(MainActivity.this,Schedule2Activity.class);
                                i.putExtra(res.getString(R.string.docid),s1);
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
        spinnerSpeciality2.setAdapter(arrayAdapter);
        settingOnitemSelectedListner();
    }
    public void settingOnitemSelectedListner(){
        spinnerSpeciality2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringSpeciality2=parent.getItemAtPosition(position).toString();
                if(stringSpeciality2.equals(res.getString(R.string.choose_speciality))){
                    t1.setVisibility(View.VISIBLE);
                    spinnerDoctor2.setVisibility(View.GONE);
                    spinnerSpeciality.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(stringSpeciality2)&&
                        !stringSpeciality2.equals(res.getString(R.string.choose_speciality))) {
                    spinnerDoctor2.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.GONE);
                    spinnerSpeciality.setVisibility(View.GONE);
                    spinnerDoctor.setVisibility(View.GONE);
                    stringNameList.clear();
                    stringDoctoIdList.clear();
                    stringNameList.add(res.getString(R.string.choose_doctor));
                    ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, stringNameList);
                    arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDoctor2.setAdapter(arrayAdapter1);
                    setSpinnerDoctorData(stringSpeciality2, arrayAdapter1);
                    spinnerDoctor2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position!=0) {
                                final String s1 = stringDoctoIdList.get(position - 1);
                                c=Calendar.getInstance();
                                int d=c.get(Calendar.DAY_OF_MONTH);
                                int m=c.get(Calendar.MONTH);
                                int y=c.get(Calendar.YEAR);
                                dialog=new DatePickerDialog(MainActivity.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String s2=""+dayOfMonth+"-"+month+"-"+year;
                                        Intent i=new Intent(MainActivity.this,SendRemoveScheduleActivity.class);
                                        i.putExtra(res.getString(R.string.docid),s1);
                                        i.putExtra("date",s2);
                                        startActivity(i);
                                    }
                                },d,m,y);
                                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                                dialog.show();
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
        t1=(TextView)findViewById(R.id.search_doctor_main);
        t2=(TextView)findViewById(R.id.send_notification_main);
        spinnerSpeciality=(Spinner)findViewById(R.id.speciality_spinner_main);
        spinnerDoctor=(Spinner)findViewById(R.id.doctor_spinner);
        spinnerDoctor.setVisibility(View.GONE);
        res=getResources();
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/DocSpeciaity");
        stringNameList=new ArrayList<String>();
        stringDoctoIdList=new ArrayList<String>();
        spinnerSpeciality2=(Spinner)findViewById(R.id.speciality_spinner_main2);
        spinnerDoctor2=(Spinner)findViewById(R.id.doctor_spinner2);
        spinnerDoctor2.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser==null){
            Intent i=new Intent(MainActivity.this,RegisterActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_add){
            startActivity(new Intent(MainActivity.this,AddActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
