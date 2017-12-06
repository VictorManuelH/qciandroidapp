package com.example.john.qciandroidapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Appointments extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointments);
        this.setTitle("Appointments");
    }
    public void prev(View view) {
        finish();
    }

}


