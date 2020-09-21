package com.example.vlog;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.Selector;

public class MainActivity extends AppCompatActivity {


    ActionBar actionBar;

    TextView next;
    EditText EnteredName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Identification");
        EnteredName = findViewById(R.id.Name);
        next=findViewById(R.id.nextName); next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EnteredName.getText().toString().length()==0)
                {
                    Toast.makeText(MainActivity.this, "Please Emter Yout Name", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Bundle bundle =new Bundle();
                    bundle.putString("name",EnteredName.getText().toString());
                    Intent intent = new Intent(MainActivity.this,SelectField.class);
                    intent.putExtra("name",EnteredName.getText().toString());
                    startActivity(intent);
                }

            }
        });

    }
}