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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword;
    private Button buttonRegister,btnSignUp;
    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleSignInClient;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intialise();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklogin();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this,
                new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void checklogin() {
        final String email,password;
        email=editTextEmail.getText().toString();
        password=editTextPassword.getText().toString();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            progressDialog.setMessage("Checking Login...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent i=new Intent(LoginActivity.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Unable to Sign in",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void intialise(){
        mAuth=FirebaseAuth.getInstance();
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        buttonRegister=(Button)findViewById(R.id.buttonRegister);
        progressDialog=new ProgressDialog(this);
        btnSignUp=(Button)findViewById(R.id.buttonloginsignup);
        databaseRef= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://scheduler-901fc.firebaseio.com/ClientId");
    }
}
