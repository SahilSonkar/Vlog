package com.example.vlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SelectField extends AppCompatActivity {


    TextView back , next , Finance , Educational , politics , travelling ,prev=null;
    int F=0,E=0,P=0,T=0;
    String Fname="";

    ProgressDialog pd;
    DatabaseReference databaseReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_field);
        Fname=getIntent().getStringExtra("name");
        pd = new ProgressDialog(this);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(Fname);
        back=findViewById(R.id.back);
        next=findViewById(R.id.next);
        Finance=findViewById(R.id.Finance);
        Educational =findViewById(R.id.Educational);
        politics=findViewById(R.id.politics);
        travelling = findViewById(R.id.traveling);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectField.this,MainActivity.class));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(F==1 || P==1 || E==1 || T==1)
                {
                    pd.setTitle("Processing..");
                    pd.show();
                    HashMap<String, String> hashMap = new HashMap<>();
                    if(F==1)
                        hashMap.put("Finance","1");
                    if(P==1)
                        hashMap.put("Politics","1");
                    if(T==1)
                        hashMap.put("Traveling","1");
                    if(E==1)
                        hashMap.put("Educational","1");
                    databaseReference.setValue(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(SelectField.this,DashBoard.class);
                                        intent.putExtra("Fname",Fname);
                                        startActivity( intent);
                                        Toast.makeText(SelectField.this, "Saved", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();}
                                    else{
                                        Toast.makeText(SelectField.this, "Failed", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();}
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(SelectField.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(SelectField.this, "You have to select atleast one", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Finance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(F==0)
                {
                    F=1;
                    Finance.setTextColor(Color.RED);
                }
                else
                {
                    F=0;
                    Finance.setTextColor(Color.BLUE);
                }

            }
        });
        Educational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(E==0)
                {
                    E=1;
                    Educational.setTextColor(Color.RED);
                }
                else
                {
                    E=0;
                    Educational.setTextColor(Color.BLUE);
                }


            }
        });
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(P==0)
                {
                    P=1;
                    politics.setTextColor(Color.RED);
                }
                else
                {
                    P=0;
                    politics.setTextColor(Color.BLUE);
                }


            }
        });
        travelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(T==0)
                {
                    T=1;
                    travelling.setTextColor(Color.RED);
                }
                else
                {
                    T=0;
                    travelling.setTextColor(Color.BLUE);
                }


            }
        });
    }
}