package com.example.hr.clientprototype;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword,editTextName,editTextPhone;
    private Button btnSignup;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intialise();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }
    private void startRegister() {
        final String name,email,password,phone;
        name=editTextName.getText().toString().trim();
        email=editTextEmail.getText().toString().trim();
        password=editTextPassword.getText().toString();
        phone=editTextPhone.getText().toString().trim();
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)
                &&!TextUtils.isEmpty(phone)){
            progressDialog.setMessage("Signing Up...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                String patientid=mAuth.getCurrentUser().getUid();
                                DatabaseReference dbref=databaseReference.child(patientid);
                                dbref.child("Name").setValue(name);
                                dbref.child("Email").setValue(email);
                                dbref.child("Phone").setValue(phone);
                                Intent i=new Intent(RegisterActivity.this,MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"Unable to Sign up",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    public void intialise(){
        mAuth=FirebaseAuth.getInstance();
        editTextEmail=(EditText)findViewById(R.id.editTextRegisterEmail);
        editTextName=(EditText)findViewById(R.id.editTextRegisterName);
        editTextPassword=(EditText)findViewById(R.id.editTextRegisterPassword);
        editTextPhone=(EditText)findViewById(R.id.editTextRegisterPhone);
        btnSignup=(Button)findViewById(R.id.buttonRegisterSignup);
        progressDialog=new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/ClientId");
    }
}
