package com.example.todoappanshu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText pass;
    private Button registerbtn;
    private FirebaseAuth firebaseAuth;
    private TextView loginbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        email = findViewById(R.id.emailid);
        pass = findViewById(R.id.password);
        registerbtn = findViewById(R.id.registerbutton);
        loginbtn = findViewById(R.id.login_tv);

        registerbtn.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v == registerbtn )
            registerUser();
        else if (v == loginbtn)
            loginUser();
    }

    private void registerUser() {
        String Email = email.getText().toString().trim();
        String Pass = pass.getText().toString().trim();

        if(TextUtils.isEmpty(Email)){
            Toast.makeText(FirstActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
            return;}
        if(TextUtils.isEmpty(Pass)){
            Toast.makeText(FirstActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
            return;}

        firebaseAuth.createUserWithEmailAndPassword(Email,Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //pass intents here from this to MainActivity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(FirstActivity.this, "User registered", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(FirstActivity.this, "Could not register user", Toast.LENGTH_LONG).show();
                    }
                }
        });
    }
    private void loginUser()
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}


////////
