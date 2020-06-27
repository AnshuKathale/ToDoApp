package com.example.todoappanshu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdaptor.OnNoteListener{
    EditText Schedule1;
    FloatingActionButton floatingActionButton1;
    public String ScheduleName;
    public static final String EXTRA_STRING="HELLO";

    private ArrayList<String> arraylist1 = new ArrayList<>();
    private RecyclerViewAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Schedule1 = findViewById(R.id.edittext1);
        floatingActionButton1 = findViewById(R.id.fab1);
        add();
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
                    //trial1
                    HashMap<String,Object> map = new HashMap<>();
                    String currentTime = Calendar.getInstance().getTime().toString();
                    map.put("Schedule",arraylist1.get(0));
                    FirebaseDatabase.getInstance().getReference().child("").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        adaptor = new RecyclerViewAdaptor(arraylist1,this);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    //goes to new activity on clicked
    public void onNoteClick(int position){
        Intent intent = new Intent(this, NewActivity.class);
        intent.putExtra(EXTRA_STRING,ScheduleName);
        startActivity(intent);

    }
}
