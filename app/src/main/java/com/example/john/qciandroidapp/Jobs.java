package com.example.john.qciandroidapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;



public class Jobs extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobs);
        this.setTitle("Jobs.Sheridan");
    }
    public void prev(View view) {
        finish();
    }

}
