package com.example.numad25sp_wenyupan_java;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class AddLinkPageActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone;
    private Button buttonSave, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_link_page);

        editTextName = findViewById(R.id.editTextTitle);
        editTextPhone = findViewById(R.id.editTextUrl);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Pre-fill data if editing an existing contact
        Intent intent = getIntent();
        if (intent.hasExtra("contactName") && intent.hasExtra("contactPhone")) {
            editTextName.setText(intent.getStringExtra("contactName"));
            editTextPhone.setText(intent.getStringExtra("contactPhone"));
        }

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("contactName", name);
                resultIntent.putExtra("contactPhone", phone);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Snackbar.make(v, "Please enter both Name and Phone", Snackbar.LENGTH_LONG).show();
            }
        });

        buttonCancel.setOnClickListener(v -> finish());
    }
}
