package com.example.hr.schdueler;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendRemoveScheduleActivity extends AppCompatActivity {
    private Resources res;
    private String docidkey,dateSend,strmsg;
    private ArrayList<String> emailidlist;
    private EditText editTextMessage;

    private Button sendMessageButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_remove_schedule);
        intialise();
        getDataFromIntent();
        emailidlist.clear();
        getEmailList();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strmsg=editTextMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(strmsg)) {
                    final String[] strings=emailidlist.toArray(new String[emailidlist.size()]);
                    Intent i=new Intent(Intent.ACTION_SEND_MULTIPLE);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_BCC,strings);
                    i.putExtra(Intent.EXTRA_SUBJECT,res.getString(R.string.subject));
                    i.putExtra(Intent.EXTRA_TEXT,strmsg);
                    startActivity(Intent.createChooser(i,"Send mail..."));
                }
                //Toast.makeText(SendRemoveScheduleActivity.this, strings[0],Toast.LENGTH_SHORT).show();
//                String[] list=emaillist.split(",");
//                Intent i=new Intent(SendRemoveScheduleActivity.this,MainActivity.class);
//                startActivity(i);
            }
        });
    }
    public void getEmailList(){
        databaseReference.child(dateSend).child(docidkey)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,String> map=(Map<String,String>)dataSnapshot.getValue();
                String temp=map.get("Email");
               // Toast.makeText(SendRemoveScheduleActivity.this,temp,Toast.LENGTH_SHORT).show();
                emailidlist.add(temp);
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
    public void getDataFromIntent(){
        emailidlist=new ArrayList<String>();
        Intent i=getIntent();
        docidkey=i.getStringExtra(res.getString(R.string.docid));
        dateSend=i.getStringExtra("date");
        databaseReference= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/Appointments");
        sendMessageButton=(Button)findViewById(R.id.send_message_btn);
        editTextMessage=(EditText) findViewById(R.id.editTextMessage);
        //Toast.makeText(SendRemoveScheduleActivity.this,docidkey+" "+dateSend,Toast.LENGTH_SHORT).show();
    }
    public void intialise(){
        res=getResources();
    }
}
