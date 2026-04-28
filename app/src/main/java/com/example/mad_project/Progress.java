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

        // ── Bottom Navigation (shared helper) ─────────────────────────────────
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavigationHelper.setup(this, bottomNav, R.id.nav_progress);

        // ── Views ─────────────────────────────────────────────────────────────
        TextView tvPercent    = findViewById(R.id.tvPercent);
        TextView tvCompleted  = findViewById(R.id.tvCompletedLabel);
        TextView tvNextTopic  = findViewById(R.id.tvNextTopic);
        ProgressBar pb1       = findViewById(R.id.pb1);
        MaterialButton btnContinue = findViewById(R.id.btnContinueLearning);

        // ── Progress calculation (single source of truth) ─────────────────────
        SharedPreferences prefs = getSharedPreferences(ProgressPrefs.PREFS_NAME, MODE_PRIVATE);
        ProgressCalculator calc = new ProgressCalculator(prefs);

        int frontendPercent        = (calc.levelsCompleted * 100) / 9;
        int overallPercent         = frontendPercent / 6;
        int fullyCompletedTopics   = (calc.levelsCompleted == 9) ? 1 : 0;

        tvPercent.setText(overallPercent + "%");
        tvCompleted.setText(fullyCompletedTopics + " of 6 topics completed");
        pb1.setProgress(frontendPercent);

        // ── Next topic recommendation ─────────────────────────────────────────
        if (!calc.isInternetComplete) {
            tvNextTopic.setText("Next: Internet Fundamentals");
        } else if (!calc.isHtmlComplete) {
            tvNextTopic.setText("Next: HTML Basics");
        } else if (!calc.isCssComplete) {
            tvNextTopic.setText("Next: CSS Basics");
        } else if (!calc.isJsComplete) {
            tvNextTopic.setText("Next: JavaScript");
        } else if (!calc.isVcComplete) {
            tvNextTopic.setText("Next: Version Control");
        } else if (!calc.isVcsComplete) {
            tvNextTopic.setText("Next: VCS Hosting");
        } else if (!calc.isPmComplete) {
            tvNextTopic.setText("Next: Package Managers");
        } else if (!calc.isCssfComplete) {
            tvNextTopic.setText("Next: CSS Frameworks");
        } else {
            tvNextTopic.setText("Next: Learn a Framework");
        }

        btnContinue.setOnClickListener(v ->
                startActivity(new Intent(this, FrontendActivity.class)));
    }
}