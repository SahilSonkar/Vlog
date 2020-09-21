package com.example.vlog;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {



    String Fname="";
    ArrayList<Helper> arrayList;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        arrayList = new ArrayList<Helper>();
        recyclerView = findViewById(R.id.recyclerView);

        Fname=getIntent().getStringExtra("Fname");
        arrayList.clear();
        databaseReference=FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    String Title=""+ds.child("Title").getValue();
                    String Content = ""+ds.child("Content").getValue();
                    String uri = ""+ds.child("PostImageUri").getValue();
                    Helper helper=new Helper(uri,Title,Content);
                    arrayList.add(helper);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                RecyclerList recyclerList = new RecyclerList(arrayList ,getApplicationContext());
                recyclerView.setAdapter(recyclerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this,Post.class);
                intent.putExtra("name",Fname);
               startActivity(intent);
            }
        });
    }
}