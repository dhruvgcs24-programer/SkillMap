package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FrontendActivity extends AppCompatActivity {

    private LinearLayout lvl1, lvl2, lvl3, lvl4, lvl5, lvl6, lvl7, lvl8, lvl9;
    private TextView txtLvl1, txtLvl2, txtLvl7, txtLvl8, txtLvl9;
    private ImageView imgLvl1, imgLvl2, imgLvl3, imgLvl4, imgLvl5, imgLvl6, imgLvl7, imgLvl8, imgLvl9, tickLvl1;
    private CardView lockBadge2, activeBadge2, lockBadge3;
    private DashedPathView dashedPathView;
    private boolean isLevel2Unlocked = false;

    // Progress Bar components
    private ProgressBar overallProgressBar;
    private TextView overallProgressText, overallCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontend);

        dashedPathView = findViewById(R.id.dashedPath);
        
        // Initialize all levels
        lvl1 = findViewById(R.id.lvl1);
        lvl2 = findViewById(R.id.lvl2);
        lvl3 = findViewById(R.id.lvl3);
        lvl4 = findViewById(R.id.lvl4);
        lvl5 = findViewById(R.id.lvl5);
        lvl6 = findViewById(R.id.lvl6);
        lvl7 = findViewById(R.id.lvl7);
        lvl8 = findViewById(R.id.lvl8);
        lvl9 = findViewById(R.id.lvl9);

        // Images
        imgLvl1 = findViewById(R.id.imgLvl1);
        imgLvl2 = findViewById(R.id.imgLvl2);
        imgLvl3 = findViewById(R.id.imgLvl3);
        imgLvl4 = findViewById(R.id.imgLvl4);
        imgLvl5 = findViewById(R.id.imgLvl5);
        imgLvl6 = findViewById(R.id.imgLvl6);
        imgLvl7 = findViewById(R.id.imgLvl7);
        imgLvl8 = findViewById(R.id.imgLvl8);
        imgLvl9 = findViewById(R.id.imgLvl9);

        // Texts
        txtLvl1 = findViewById(R.id.txtLvl1);
        txtLvl2 = findViewById(R.id.txtLvl2);
        txtLvl7 = findViewById(R.id.txtLvl7);
        txtLvl8 = findViewById(R.id.txtLvl8);
        txtLvl9 = findViewById(R.id.txtLvl9);
        
        tickLvl1 = findViewById(R.id.tickLvl1);
        lockBadge2 = findViewById(R.id.lockBadge2);
        activeBadge2 = findViewById(R.id.activeBadge2);
        lockBadge3 = findViewById(R.id.lockBadge3);

        overallProgressBar = findViewById(R.id.overallProgressBar);
        overallProgressText = findViewById(R.id.overallProgressText);
        overallCountText = findViewById(R.id.overallCountText);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        lvl1.setOnClickListener(v -> startActivity(new Intent(this, InternetActivity.class)));

        lvl2.setOnClickListener(v -> {
            if (isLevel2Unlocked) {
                Toast.makeText(this, "HTML level will be added next", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Complete all Internet topics first", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup paths once laid out
        lvl1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setupDashedPaths();
                lvl1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setupDashedPaths() {
        dashedPathView.clearPoints();
        addPoint(lvl1);
        addPoint(lvl2);
        addPoint(lvl3);
        addPoint(lvl4);
        addPoint(lvl5);
        addPoint(lvl6);
        addPoint(lvl7);
        addPoint(lvl8);
        addPoint(lvl9);
    }

    private void addPoint(View view) {
        float x = view.getX() + (view.getWidth() / 2f);
        float y = view.getY() + (view.getHeight() / 2f);
        dashedPathView.addPathPoint(x, y);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);

        // Track topic completion inside Level 1 (Internet)
        int internetTopicsDone = 0;
        if (prefs.getBoolean("t1", false)) internetTopicsDone++;
        if (prefs.getBoolean("t2", false)) internetTopicsDone++;
        if (prefs.getBoolean("t3", false)) internetTopicsDone++;
        if (prefs.getBoolean("t4", false)) internetTopicsDone++;
        if (prefs.getBoolean("t5", false)) internetTopicsDone++;
        if (prefs.getBoolean("t6", false)) internetTopicsDone++;

        // A level is considered complete ONLY if all its topics are finished
        int levelsCompleted = 0;
        boolean isInternetComplete = (internetTopicsDone == 6);
        if (isInternetComplete) levelsCompleted++;

        // Add logic for other levels here as they are implemented...
        // if (prefs.getBoolean("html_complete", false)) levelsCompleted++;

        // Update Progress Bar based on FULL levels
        overallProgressBar.setProgress(levelsCompleted);
        overallCountText.setText(levelsCompleted + "/9 Levels Completed");
        int percent = (int) ((levelsCompleted / 9.0) * 100);
        overallProgressText.setText("Roadmap Progress: " + percent + "%");

        isLevel2Unlocked = isInternetComplete;

        if (isInternetComplete) {
            lockBadge2.setVisibility(View.GONE);
            activeBadge2.setVisibility(View.VISIBLE);
            imgLvl2.setAlpha(1.0f);
            
            // Show success state for Level 1
            imgLvl1.setImageResource(R.drawable.circle_success);
            txtLvl1.setVisibility(View.GONE);
            tickLvl1.setVisibility(View.VISIBLE);
        } else {
            lockBadge2.setVisibility(View.VISIBLE);
            activeBadge2.setVisibility(View.GONE);
            imgLvl2.setAlpha(0.5f);

            // Reset Level 1 to initial state
            imgLvl1.setImageResource(R.drawable.circle_purple);
            txtLvl1.setVisibility(View.VISIBLE);
            tickLvl1.setVisibility(View.GONE);
        }
    }
}
