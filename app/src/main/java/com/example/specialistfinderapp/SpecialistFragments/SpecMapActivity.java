package com.example.specialistfinderapp.SpecialistFragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.specialistfinderapp.R;

public class SpecMapActivity extends AppCompatActivity {
    Button accessMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_map);

        accessMap = findViewById(R.id.accessMapp);
        accessMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpecMapActivity.this, SpecHomeFragment.class);
                startActivity(intent);
                return;
            }
        });
    }
}
