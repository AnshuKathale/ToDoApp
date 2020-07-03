package com.example.todoappanshu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private EditText pass;
    private Button loginbutton;
    private FirebaseAuth firebaseAuth;
    private TextView registerbtn;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailid);
        pass = findViewById(R.id.password);
        loginbutton = findViewById(R.id.loginbutton);
        registerbtn = findViewById(R.id.register_tv);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "YOU ARE LOGGED IN", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext() , ScheduleActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "PLEASE LOGIN", Toast.LENGTH_SHORT).show();
                }
            }
        };
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Pass = pass.getText().toString().trim();

                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(LoginActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
                    return;}
                if(TextUtils.isEmpty(Pass)){
                    Toast.makeText(LoginActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
                    return;}

                firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Error , Please Login Again", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, ScheduleActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    public void onClick(View v) {
        if (v == registerbtn)
            registerUser();
    }

    private void registerUser() {
        Intent intent = new Intent(getApplicationContext() , RegisterActivity.class);
        startActivity(intent);
    }
}

