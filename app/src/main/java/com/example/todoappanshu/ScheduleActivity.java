package com.example.todoappanshu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ScheduleActivity extends AppCompatActivity {

    //variables

    DatabaseReference ref;
    EditText Schedule1;
    FloatingActionButton floatingActionButton1;
    public String ScheduleName;
    public static final String EXTRA_STRING="HELLO";
    public RecyclerViewAdaptor.OnNoteListener onNoteListener;
    private FirebaseAuth mAuth;
    private ArrayList<String> scheduleList = new ArrayList<>();
    private RecyclerViewAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Schedule1 = findViewById(R.id.edittext1);
        floatingActionButton1 = findViewById(R.id.fab1);
        mAuth = FirebaseAuth.getInstance();


        //Goes to Task Activity

        onNoteListener = new RecyclerViewAdaptor.OnNoteListener() {
            @Override
            public void onNoteClick(int position) {

                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                intent.putExtra(EXTRA_STRING, scheduleList.get(position));
                startActivity(intent);
            }
        };
        add();
        if(scheduleList.isEmpty())
            readSchedule();
        mRecyclerView();
    }


    //To add text and insert cards in recyclerView
    public void add(){
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScheduleName = Schedule1.getText().toString();
                if(TextUtils.isEmpty(ScheduleName)){
                    Toast.makeText(ScheduleActivity.this, "Please enter Schedule", Toast.LENGTH_SHORT).show();
                }
                else{
                    scheduleList.add(0,ScheduleName);
                    adaptor.notifyItemInserted(0);

                    //Write into database

                    FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child(scheduleList.get(scheduleList.size()-1)).setValue("NA")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
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


    }

    //Setting up the recyclerview
    public void mRecyclerView(){


        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        //Recyclerview divider

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        adaptor = new RecyclerViewAdaptor(scheduleList, onNoteListener);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void readSchedule(){
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Log.i("Tag","for loop main act");
                        String read =dataSnapshot.getKey();
                        scheduleList.add(0,read);
                        adaptor.notifyItemInserted(0);
                        Log.i("TAG", "" + read);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ScheduleActivity.this, "Data not saved", Toast.LENGTH_SHORT).show();
            }
        });


    }



}
