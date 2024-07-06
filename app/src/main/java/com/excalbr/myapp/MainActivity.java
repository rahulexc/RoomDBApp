package com.excalbr.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText nameEdit;
    EditText ageEdit;
    Button saveButton,getDataButton;

    PersonDatabase personDB;

    List<Person> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdit = findViewById(R.id.nameEdit);
        ageEdit = findViewById(R.id.ageEdit);

        saveButton = findViewById(R.id.saveButton);
        getDataButton = findViewById(R.id.getDataButton);

        RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        personDB = Room.databaseBuilder(getApplicationContext(),PersonDatabase.class,
                "PersonDB1").addCallback(myCallBack).build();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String age = ageEdit.getText().toString();

                Person p1 = new Person(name,age);

                addPersonInBackground(p1);





            }
        });

        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPersonListInBackground();

            }
        });



    }

    public void addPersonInBackground(Person person){


    }

    public void getPersonListInBackground(){

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                // background task

                personList = personDB.getPersonDAO().getAllPerson();

                // on finishing task
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder sb  =new StringBuilder();
                        for(Person p : personList){
                            sb.append(p.getName()+" : "+p.getAge());
                            sb.append("\n");
                        }
                        String finalData = sb.toString();
                        Toast.makeText(MainActivity.this, ""+finalData, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}