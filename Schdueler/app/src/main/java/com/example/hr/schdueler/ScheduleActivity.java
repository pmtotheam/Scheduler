package com.example.hr.schdueler;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity implements ScheduleDialogFragment.OnInputListner{
    private Resources res;
    private ArrayList<Schedule> schedules;
    private Button buttonSave;

    private RecyclerView schedulerRecyclerView;
    private ScheduleAdapter mScheduleAdapter;
    private String stringName, stringPhone, stringSpeciality;

    private DatabaseReference mDatabase,databaseReferenceDoctorId,databaseReferenceDoctorSchedule,temp;
    private DatabaseReference databaseReferenceDocSpeciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        intialise();
        getStringFromAdd();
//        Toast.makeText(this,stringName+" "+stringPhone+" "+stringSpeciality,Toast.LENGTH_SHORT)
//                .show();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataOnFirebase();
                startActivity(new Intent(ScheduleActivity.this,MainActivity.class));
                finish();
            }
        });
    }
    public void postDataOnFirebase(){
        databaseReferenceDoctorId=mDatabase.child(res.getString(R.string.docid));
        databaseReferenceDoctorSchedule=mDatabase.child(res.getString(R.string.docschedule));
        databaseReferenceDocSpeciality=mDatabase.child(res.getString(R.string.docspeciality));
        Map<String,String> docinfo=new HashMap<>();
        docinfo.put("Name",stringName);
        docinfo.put("Phone",stringPhone);
        docinfo.put("Speciality",stringSpeciality);
        temp=databaseReferenceDoctorId.push();
        String pushedKey=temp.getKey();
        temp.setValue(docinfo);
        docinfo.remove("Phone");
        docinfo.remove("Speciality");
        docinfo.put("docid",pushedKey);
        databaseReferenceDocSpeciality.child(stringSpeciality).push().setValue(docinfo);
        for(Schedule schedule:schedules) {
            Map<String, String> docschedule = new HashMap<>();
            docschedule.put("Day",schedule.getDay());
            docschedule.put("From",schedule.getFrom()+schedule.getAmpmFrom());
            docschedule.put("To",schedule.getTo()+schedule.getAmpmTo());
            databaseReferenceDoctorSchedule.child(pushedKey).push().setValue(docschedule);
        }
        //Toast.makeText(this,pushedKey,Toast.LENGTH_SHORT).show();
    }
    private void intialise(){
        mDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/");
        res=getResources();
        schedules=new ArrayList<Schedule>();
        buttonSave=(Button)findViewById(R.id.save_schedule_button);
        schedulerRecyclerView=(RecyclerView)findViewById(R.id.schedule_recyclerview);
        mScheduleAdapter=new ScheduleAdapter(schedules);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        schedulerRecyclerView.setLayoutManager(layoutManager);
        schedulerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        schedulerRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        schedulerRecyclerView.setAdapter(mScheduleAdapter);
    }
    private void getStringFromAdd(){
        Intent i = getIntent();
        stringName=i.getStringExtra(res.getString(R.string.dockey));
        stringPhone=i.getStringExtra(res.getString(R.string.phnkey));
        stringSpeciality=i.getStringExtra(res.getString(R.string.sclkey));
    }

    @Override
    public void sendInput(Schedule schedule) {
        schedules.add(schedule);
        mScheduleAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_add){
            ScheduleDialogFragment myDialog=new ScheduleDialogFragment();
            myDialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.Schedule_Fragment_FullScreen);
            myDialog.show(getFragmentManager(),"ScheduleDialogFragment");
        }
        return super.onOptionsItemSelected(item);
    }
}
