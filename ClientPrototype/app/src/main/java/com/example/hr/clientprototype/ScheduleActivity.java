package com.example.hr.clientprototype;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {
    private Calendar c;
    private DatePickerDialog dialog;

    private Resources res;
    private ArrayList<Schedule> schedules;
    private RecyclerView schedulerRecycleView;
    private ScheduleAdapter schedule2Adapter;

    private String docidkey,docname,spec;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        intialise();
        getdocid();
        databaseReference.child(docidkey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,String> mapSchedule=(Map<String,String>)dataSnapshot.getValue();
                Schedule temp=new Schedule();
                temp.setDay(mapSchedule.get("Day"));
                temp.setFrom(mapSchedule.get("From"));
                temp.setTo(mapSchedule.get("To"));
                schedules.add(temp);
                schedule2Adapter.notifyDataSetChanged();
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
    public void getdocid(){
        Intent i=getIntent();
        docidkey=i.getStringExtra(res.getString(R.string.docid));
        docname=i.getStringExtra("DoctorName");
        spec=i.getStringExtra("Docspec");
        //Toast.makeText(this,docidkey,Toast.LENGTH_SHORT).show();
    }
    public void intialise(){
        res=getResources();
        schedules=new ArrayList<Schedule>();
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/DocSchedule");
        schedulerRecycleView=(RecyclerView)findViewById(R.id.schedule_recyclerview);
        schedule2Adapter=new ScheduleAdapter(schedules);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        schedulerRecycleView.setLayoutManager(layoutManager);
        schedulerRecycleView.setItemAnimator(new DefaultItemAnimator());
        schedulerRecycleView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        schedulerRecycleView.setAdapter(schedule2Adapter);
        schedulerRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                schedulerRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Schedule temp1=schedules.get(position);
                //Toast.makeText(ScheduleActivity.this,temp1.getDay(),Toast.LENGTH_SHORT).show();
                final String selectedDay=temp1.getDay();
                c=Calendar.getInstance();
                int d=c.get(Calendar.DAY_OF_MONTH);
                int m=c.get(Calendar.MONTH);
                int y=c.get(Calendar.YEAR);
                dialog=new DatePickerDialog(ScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth-1);
                        int dayOfWeek=date.get(GregorianCalendar.DAY_OF_WEEK);
                        if(selectedDay.equals(res.getStringArray(R.array.days_array)[dayOfWeek-1])){
                            Intent i=new Intent(ScheduleActivity.this,PatientInfoActivity.class);
                            i.putExtra("date",""+dayOfMonth+"-"+month+"-"+year);
                            i.putExtra("docid",docidkey);
                            i.putExtra("docname",docname);
                            i.putExtra("spec",spec);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(ScheduleActivity.this,"Date selected on:"+
                                            res.getStringArray(R.array.days_array)[dayOfWeek-1]
                                    +"\nSelected week:"+selectedDay+
                                    "\nSelect Again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },d,m,y);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
            @Override
            public void onLongClick(View view, int position) {}
        }));
    }
}
