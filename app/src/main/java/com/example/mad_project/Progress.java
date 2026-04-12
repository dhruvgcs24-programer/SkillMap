package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class Progress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Fix Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_progress);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_roadmaps) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_progress) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, EditProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });

        TextView tvPercent = findViewById(R.id.tvPercent);
        TextView tvCompleted = findViewById(R.id.tvCompletedLabel);
        ProgressBar pb1 = findViewById(R.id.pb1);
        MaterialButton btnContinue = findViewById(R.id.btnContinueLearning);

        SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);

        int internetTopicsDone = 0;
        for (int i = 1; i <= 6; i++) {
            if (prefs.getBoolean("t" + i, false)) internetTopicsDone++;
        }
        boolean isHtmlComplete = prefs.getBoolean("h_complete", false);
        boolean isCssComplete = prefs.getBoolean("c_complete", false);
        boolean isJsComplete = prefs.getBoolean("j_complete", false);
        boolean isVcComplete = prefs.getBoolean("vc1", false);
        boolean isVcsComplete = prefs.getBoolean("vcs1", false) && prefs.getBoolean("vcs2", false);
        boolean isPmComplete = prefs.getBoolean("pm1", false) && prefs.getBoolean("pm2", false) && prefs.getBoolean("pm3", false) && prefs.getBoolean("pm4", false);
        boolean isCssfComplete = prefs.getBoolean("cssf1", false);

        int frontendLevelsCompleted = 0;
        if (internetTopicsDone == 6) frontendLevelsCompleted++;
        if (isHtmlComplete) frontendLevelsCompleted++;
        if (isCssComplete) frontendLevelsCompleted++;
        if (isJsComplete) frontendLevelsCompleted++;
        if (isVcComplete) frontendLevelsCompleted++;
        if (isVcsComplete) frontendLevelsCompleted++;
        if (isPmComplete) frontendLevelsCompleted++;
        if (isCssfComplete) frontendLevelsCompleted++;

        // Frontend progress (out of 9 levels)
        int frontendPercent = (frontendLevelsCompleted * 100) / 9;

        // Overall progress (6 roadmaps total: Frontend, Backend, Android, Full Stack, DevOps, AI)
        int overallPercent = frontendPercent / 6;

        int fullyCompletedTopics = 0;
        if (frontendLevelsCompleted == 9) fullyCompletedTopics++;

        tvPercent.setText(overallPercent + "%");
        tvCompleted.setText(fullyCompletedTopics + " of 6 topics completed");
        
        // pb1 represents the Frontend Developer active roadmap card
        pb1.setProgress(frontendPercent);

        TextView tvNextTopic = findViewById(R.id.tvNextTopic);
        if (internetTopicsDone < 6) {
            tvNextTopic.setText("Next: Internet Fundamentals");
        } else if (!isHtmlComplete) {
            tvNextTopic.setText("Next: HTML Basics");
        } else if (!isCssComplete) {
            tvNextTopic.setText("Next: CSS Basics");
        } else if (!isJsComplete) {
            tvNextTopic.setText("Next: JavaScript");
        } else if (!isVcComplete) {
            tvNextTopic.setText("Next: Version Control");
        } else if (!isVcsComplete) {
            tvNextTopic.setText("Next: VCS Hosting");
        } else if (!isPmComplete) {
            tvNextTopic.setText("Next: Package Managers");
        } else if (!isCssfComplete) {
            tvNextTopic.setText("Next: CSS Frameworks");
        } else {
            tvNextTopic.setText("Next: Learn a Framework");
        }

        btnContinue.setOnClickListener(v ->
                startActivity(new Intent(Progress.this, FrontendActivity.class)));
    }
}