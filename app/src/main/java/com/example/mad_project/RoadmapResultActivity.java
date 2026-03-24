package com.example.mad_project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RoadmapResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_result);

        TextView tvRoadmap = findViewById(R.id.tvRoadmapResult);

        String roadmap = getIntent().getStringExtra("roadmap_text");
        if (roadmap == null || roadmap.isEmpty()) {
            roadmap = "No roadmap available.";
        }

        tvRoadmap.setText(roadmap);
    }
}