package com.example.todoappanshu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

@SuppressLint("Registered")
public class TaskActivity extends AppCompatActivity {

    //variables

    ListView listview;
    EditText Schedule2;
    FloatingActionButton floatingActionButton2;
    private FirebaseAuth mAuth;
    private ArrayList<String> taskList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        listview = findViewById(R.id.listview);
        Schedule2 = findViewById(R.id.edittext2);
        floatingActionButton2 = findViewById(R.id.fab2);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        final String scheduleText=intent.getStringExtra(ScheduleActivity.EXTRA_STRING); //Getting schedule entered in the RecyclerView
        TextView textView= findViewById(R.id.textview);
        textView.setText(scheduleText);
        arrayAdapter= new ArrayAdapter<>(getApplicationContext(), R.layout.customlist, taskList);


        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = Schedule2.getText().toString();
                if(TextUtils.isEmpty(task)){
                    Toast.makeText(TaskActivity.this,"Please enter task",Toast.LENGTH_SHORT).show();
                }
                else{
                    taskList.add(task);
                    listview.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                    //write
                    FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child(scheduleText).child("Task"+(taskList.size()-1)).setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("TAG","onComplete success");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("TAG","onFailure happened because:"+e.toString());
                        }
                    });

                }
            }
        });
        //fab over

        //read
            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()+"/"+scheduleText);
            if(taskList.isEmpty())
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && !snapshot.getValue().toString().equals("NA")) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()) {

                                Log.i("Tag","for loop new act");
                                String read =dataSnapshot.getValue().toString();
                                taskList.add(read);
                                listview.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                                Log.i("TAG", "" + read);
                            }
                        }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(TaskActivity.this, "Data not saved", Toast.LENGTH_SHORT).show();
                }
            });


    }
}
