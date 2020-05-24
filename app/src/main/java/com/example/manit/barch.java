package com.example.manit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class barch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barch);
       getSupportActionBar().setTitle("B.Arch");
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
