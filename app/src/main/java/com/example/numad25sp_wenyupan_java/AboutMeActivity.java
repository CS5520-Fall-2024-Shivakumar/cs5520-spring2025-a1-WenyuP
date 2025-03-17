package com.example.numad25sp_wenyupan_java;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        // Find TextView and set name & email
        TextView aboutMeText = findViewById(R.id.aboutMeText);
        aboutMeText.setText("Wenyu Pan\npan.we@northeastern.edu");
    }
}
