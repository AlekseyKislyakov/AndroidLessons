package com.example.a01_1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.TreeSet;

public class Exercise1 extends AppCompatActivity {
    public TreeSet<String> students = new TreeSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addStudent(View view){
        EditText editText = findViewById(R.id.editText);
        students.add(editText.getText().toString());
        editText.getText().clear();
    }

    public void viewStudent(View view){
        TextView tw = findViewById(R.id.textView);
        String temp = "";
        for (String str: students) {
            temp += str + "\n";
        }
        tw.setText(temp);
        tw.setMovementMethod(new ScrollingMovementMethod());
    }

}
