package com.example.numad25sp_wenyupan_java;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutMeButton = findViewById(R.id.aboutMeButton);
        aboutMeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
            startActivity(intent);
        });

        Button quickCalcButton = findViewById(R.id.quickCalcButton);
        quickCalcButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });

        Button linkCollectorButton = findViewById(R.id.linkCollectorButton);
        linkCollectorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LinkCollectorActivity.class);
            startActivity(intent);
        });

        // 🔹 New button to open PrimeDirectiveActivity
        Button primeDirectiveButton = findViewById(R.id.primeDirectiveButton);
        primeDirectiveButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PrimeDirectiveActivity.class);
            startActivity(intent);
        });
    }
}
