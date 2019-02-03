package com.example.hr.schdueler;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class Schedule2Activity extends AppCompatActivity {
    private Resources res;
    private ArrayList<Schedule> schedules;
    private RecyclerView schedulerRecycleView2;
    private Schedule2Adapter schedule2Adapter;

    private String docidkey;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule2);
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
        //Toast.makeText(this,docidkey,Toast.LENGTH_SHORT).show();
    }
    public void intialise(){
        res=getResources();
        schedules=new ArrayList<Schedule>();
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/DocSchedule");
        schedulerRecycleView2=(RecyclerView)findViewById(R.id.schedule2_recyclerview);
        schedule2Adapter=new Schedule2Adapter(schedules);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        schedulerRecycleView2.setLayoutManager(layoutManager);
        schedulerRecycleView2.setItemAnimator(new DefaultItemAnimator());
        schedulerRecycleView2.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        schedulerRecycleView2.setAdapter(schedule2Adapter);
    }
}
