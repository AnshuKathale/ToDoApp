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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("Registered")
public class NewActivity extends AppCompatActivity {
    ListView listview;
    EditText Schedule2;
    FloatingActionButton floatingActionButton2;

    private ArrayList<String> arraylist2 = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        listview = findViewById(R.id.listview);
        Schedule2 = findViewById(R.id.edittext2);
        floatingActionButton2 = findViewById(R.id.fab2);

        Intent intent = getIntent();
        String text=intent.getStringExtra(MainActivity.EXTRA_STRING); //Getting schedule entered in the RecyclerView
        TextView textView= findViewById(R.id.textview);
        textView.setText(text);
        arrayAdapter= new ArrayAdapter<>(getApplicationContext(), R.layout.customlist, arraylist2);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = Schedule2.getText().toString();
                if(TextUtils.isEmpty(task)){
                    Toast.makeText(NewActivity.this,"",Toast.LENGTH_SHORT).show();
                }
                else{
                    arraylist2.add(task);
                    listview.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                    //trial1 new act
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("Task",arraylist2.get(0));
                    FirebaseDatabase.getInstance().getReference().child("Task").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
}
