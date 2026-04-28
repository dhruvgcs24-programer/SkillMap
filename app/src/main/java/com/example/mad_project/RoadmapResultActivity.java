package com.example.mad_project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Displays the AI-generated roadmap text.
 *
 * WHY THIS EXISTS:
 * CreateRoadmapActivity was calling setContentView() twice — once for the
 * form layout and again for the result layout — and re-launching itself.
 * That is an anti-pattern. This Activity owns only the result screen,
 * giving each screen its own clean back-stack entry.
 *
 * LAUNCHED FROM: CreateRoadmapActivity
 * RECEIVES:      Intent extra → CreateRoadmapActivity.EXTRA_ROADMAP_TEXT (String)
 *
 * HOW TO EXTEND:
 * Add a Share button, save-to-Firebase feature, or export as PDF here.
 * All roadmap result logic lives in this one place.
 */
public class RoadmapResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_result);

        String roadmapText = getIntent().getStringExtra(CreateRoadmapActivity.EXTRA_ROADMAP_TEXT);

        TextView tvRoadmap = findViewById(R.id.tvRoadmapResult);
        tvRoadmap.setText(
                (roadmapText != null && !roadmapText.isEmpty())
                        ? roadmapText
                        : "No roadmap available."
        );
    }
}
