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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    DatabaseReference ref;
    EditText Schedule1;
    FloatingActionButton floatingActionButton1;
    public String ScheduleName;
    public static final String EXTRA_STRING="HELLO";
    public RecyclerViewAdaptor.OnNoteListener onNoteListener;
    private FirebaseAuth mAuth;
    private ArrayList<String> arraylist1 = new ArrayList<>();
    private RecyclerViewAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Schedule1 = findViewById(R.id.edittext1);
        floatingActionButton1 = findViewById(R.id.fab1);
        //goes to new activity on clicked
        onNoteListener = new RecyclerViewAdaptor.OnNoteListener() {
            @Override
            public void onNoteClick(int position) {

                Intent intent = new Intent(getApplicationContext(), NewActivity.class);
                intent.putExtra(EXTRA_STRING,arraylist1.get(position));
                startActivity(intent);
            }
        };
        add();
        if(arraylist1.isEmpty())
            readSchedule();
        mRecyclerView();
    }


    //to add text and insert cards in recyclerView
    public void add(){
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScheduleName = Schedule1.getText().toString();
                if(TextUtils.isEmpty(ScheduleName)){
                    Toast.makeText(MainActivity.this, "Please enter Schedule", Toast.LENGTH_SHORT).show();
                }
                else{
                    arraylist1.add(0,ScheduleName);
                    adaptor.notifyItemInserted(0);
                    //write
                    FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child(arraylist1.get(arraylist1.size()-1)).setValue("NA")
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

                    //Read

                }

            }
        });


    }

    //Setting up the recyclerview
    public void mRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adaptor = new RecyclerViewAdaptor(arraylist1, onNoteListener);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void readSchedule(){
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Log.i("Tag","for loop main act");
                        String read =dataSnapshot.getKey();
                        arraylist1.add(0,read);
                        adaptor.notifyItemInserted(0);
                        Log.i("TAG", "" + read);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
