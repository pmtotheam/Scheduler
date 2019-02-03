package com.example.hr.clientprototype;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class AppointActivity extends AppCompatActivity {
    private Resources res;
    private ArrayList<Appointment> appointments;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AppointAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);
        intialse();
        databaseReference.child(mAuth.getCurrentUser().getUid()).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Map<String,String> mapappoint=(Map<String,String>)dataSnapshot.getValue();
                        Appointment appointment=new Appointment(mapappoint.get("Doctor Name"),
                                mapappoint.get("Speciality"),mapappoint.get("Date of Appointment"));
                        appointments.add(appointment);
                        //set notify change
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                }
        );
    }
    public void intialse(){
        res=getResources();
        appointments=new ArrayList<Appointment>();
        databaseReference= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/ChildAppoint");
        mAuth=FirebaseAuth.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.appoint_recyclerview);
        adapter=new AppointAdapter(appointments);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
    public class AppointAdapter extends RecyclerView.Adapter<AppointAdapter.AppointViewholder>{
        private ArrayList<Appointment> appointment1s;
        public AppointAdapter(ArrayList<Appointment> appointment1s) {this.appointment1s = appointment1s;}
        public class AppointViewholder extends RecyclerView.ViewHolder{
            public TextView dayHolderView,docnameHolderView,specHolderView;

            public AppointViewholder(View itemView) {
                super(itemView);
                dayHolderView=(TextView)itemView.findViewById(R.id.textView_date_appoint);
                docnameHolderView=(TextView)itemView.findViewById(R.id.textView_doctor_appoint);
                specHolderView=(TextView)itemView.findViewById(R.id.textView_appoint_doctor_speciality);
            }
        }
        @Override
        public AppointViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.appoint_list_row,
                    parent,false);
            return new AppointViewholder(view);
        }
        @Override
        public void onBindViewHolder(AppointViewholder holder, int position) {
            Appointment appointment=appointment1s.get(position);
            holder.dayHolderView.setText(appointment.getDate());
            holder.specHolderView.setText(appointment.getSpecality());
            holder.docnameHolderView.setText(appointment.getDocname());
        }
        @Override
        public int getItemCount() {
            return appointment1s.size();
        }
    }
}
