package com.sourcey.materiallogindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sourcey.materiallogindemo.DatabaseHelper.Database;

public class report_found extends AppCompatActivity {

    Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_found);

        db = new Database(this);


//        TextView tq = findViewById(R.id.withText);
//        db.insertData(tq);


    }
}
