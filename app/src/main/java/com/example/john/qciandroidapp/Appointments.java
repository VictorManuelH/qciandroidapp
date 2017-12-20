package com.example.john.qciandroidapp;

import android.content.Intent;
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

    public void bookClick(View view) {
        Intent intent = new Intent(this, BookApt2.class);
        startActivity(intent);
    }

    public void viewClick(View view) {
        Intent intent = new Intent(this, CheckApt.class);
        startActivity(intent);
    }
}


