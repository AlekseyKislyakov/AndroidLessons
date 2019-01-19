package com.example.a01_1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Exercise2 extends AppCompatActivity {
    public class Student {
        public int id;
        public String name;
        public String surname;
        public String grade;
        public String birthdayYear;
    }
    public Map<Integer, Student>  hashMap= new HashMap<Integer, Student>();
    //public TreeSet<String> students = new TreeSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText editText = findViewById(R.id.editText2);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText editText = findViewById(R.id.editText2);
                    String parts[] = editText.getText().toString().split(" ");
                    if(parts.length != 4){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Вы что-то неправильно ввели))", Toast.LENGTH_LONG);
                        toast.show();
                        editText.getText().clear();
                    }
                    else{
                        Student temp = new Student();
                        temp.id = (int)System.currentTimeMillis()%9999;
                        temp.name = parts[0];
                        temp.surname = parts[1];
                        temp.grade = parts[2];
                        temp.birthdayYear = parts[3];
                        hashMap.put(temp.id,temp);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Запись добавлена", Toast.LENGTH_LONG);
                        toast.show();
                        editText.getText().clear();
                    }

                    return true;
                }
                return false;
            }
        });


    }
    public void viewStudent(View view){
        TextView tw = findViewById(R.id.textView3);
        String temp = "";
        for(Map.Entry<Integer, Student> entry : hashMap.entrySet()) {
            Integer key = entry.getKey();
            Student value = entry.getValue();
            temp += value.id + "\t" + value.name + "\t" +
                    value.surname + "\t" + value.grade + "\t" + value.birthdayYear + "\n";
        }
        tw.setText(temp);
        tw.setMovementMethod(new ScrollingMovementMethod());
    }



}
