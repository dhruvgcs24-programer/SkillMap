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
    private TextView txtLvl1, txtLvl2, txtLvl3, txtLvl4, txtLvl5, txtLvl6, txtLvl7, txtLvl8, txtLvl9;
    private ImageView imgLvl1, imgLvl2, imgLvl3, imgLvl4, imgLvl5, imgLvl6, imgLvl7, imgLvl8, imgLvl9, tickLvl1, tickLvl2, tickLvl3, tickLvl4, tickLvl5, tickLvl6, tickLvl7, tickLvl8, tickLvl9;
    private CardView lockBadge2, activeBadge2, lockBadge3, activeBadge3, lockBadge4, activeBadge4, lockBadge5, activeBadge5,  lockBadge6, lockBadge7, lockBadge8, lockBadge9;
    private DashedPathView dashedPathView;
    private boolean isLevel2Unlocked = false;
    private boolean isLevel3Unlocked = false;
    private boolean isLevel4Unlocked = false;
    private boolean isLevel5Unlocked = false;

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
        txtLvl3 = findViewById(R.id.txtLvl3);
        txtLvl4 = findViewById(R.id.txtLvl4);
        txtLvl5 = findViewById(R.id.txtLvl5);
        txtLvl6 = findViewById(R.id.txtLvl6);
        txtLvl7 = findViewById(R.id.txtLvl7);
        txtLvl8 = findViewById(R.id.txtLvl8);
        txtLvl9 = findViewById(R.id.txtLvl9);
        
        tickLvl1 = findViewById(R.id.tickLvl1);
        tickLvl2 = findViewById(R.id.tickLvl2);
        tickLvl3 = findViewById(R.id.tickLvl3);
        tickLvl4 = findViewById(R.id.tickLvl4);
        tickLvl5 = findViewById(R.id.tickLvl5);

        lockBadge2 = findViewById(R.id.lockBadge2);
        activeBadge2 = findViewById(R.id.activeBadge2);
        lockBadge3 = findViewById(R.id.lockBadge3);
        activeBadge3 = findViewById(R.id.activeBadge3);
        lockBadge4 = findViewById(R.id.lockBadge4);
        activeBadge4 = findViewById(R.id.activeBadge4);
        lockBadge5 = findViewById(R.id.lockBadge5);
        activeBadge5 = findViewById(R.id.activeBadge5);

        overallProgressBar = findViewById(R.id.overallProgressBar);
        overallProgressText = findViewById(R.id.overallProgressText);
        overallCountText = findViewById(R.id.overallCountText);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        lvl1.setOnClickListener(v -> startActivity(new Intent(this, InternetActivity.class)));

        lvl2.setOnClickListener(v -> {
            if (isLevel2Unlocked) {
                startActivity(new Intent(this, HtmlActivity.class));
            } else {
                Toast.makeText(this, "Complete all Internet topics first", Toast.LENGTH_SHORT).show();
            }
        });

        lvl3.setOnClickListener(v -> {
            if (isLevel3Unlocked) {
                startActivity(new Intent(this, CssActivity.class));
            } else {
                Toast.makeText(this, "Complete HTML level first", Toast.LENGTH_SHORT).show();
            }
        });

        lvl4.setOnClickListener(v -> {
            if (isLevel4Unlocked) {
                startActivity(new Intent(this, JavascriptActivity.class));
            } else {
                Toast.makeText(this, "Complete CSS level first", Toast.LENGTH_SHORT).show();
            }
        });

        lvl5.setOnClickListener(v -> {
            if (isLevel5Unlocked) {
                Toast.makeText(this, "Version Control Level coming soon!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Complete JavaScript level first", Toast.LENGTH_SHORT).show();
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

        // Track level 1 completion
        int internetTopicsDone = 0;
        for (int i = 1; i <= 6; i++) {
            if (prefs.getBoolean("t" + i, false)) internetTopicsDone++;
        }
        boolean isInternetComplete = (internetTopicsDone == 6);

        // Track level 2 completion (HTML)
        boolean isHtmlComplete = prefs.getBoolean("h_complete", false);
        
        // Track level 3 completion (CSS)
        boolean isCssComplete = prefs.getBoolean("c_complete", false);

        // Track level 4 completion (JavaScript)
        boolean isJsComplete = prefs.getBoolean("j_complete", false);

        // Calculate Completed Levels
        int levelsCompleted = 0;
        if (isInternetComplete) levelsCompleted++;
        if (isHtmlComplete) levelsCompleted++;
        if (isCssComplete) levelsCompleted++;
        if (isJsComplete) levelsCompleted++;

        // Update Progress Bar
        overallProgressBar.setProgress(levelsCompleted);
        overallCountText.setText(levelsCompleted + "/9 Levels Completed");
        int percent = (int) ((levelsCompleted / 9.0) * 100);
        overallProgressText.setText("Roadmap Progress: " + percent + "%");

        isLevel2Unlocked = isInternetComplete;
        isLevel3Unlocked = isHtmlComplete;
        isLevel4Unlocked = isCssComplete;
        isLevel5Unlocked = isJsComplete;

        // Level 1 Success State
        if (isInternetComplete) {
            imgLvl1.setImageResource(R.drawable.circle_success);
            txtLvl1.setVisibility(View.GONE);
            tickLvl1.setVisibility(View.VISIBLE);
        } else {
            imgLvl1.setImageResource(R.drawable.circle_purple);
            txtLvl1.setVisibility(View.VISIBLE);
            tickLvl1.setVisibility(View.GONE);
        }

        // Level 2 (HTML) State
        if (isInternetComplete) {
            lockBadge2.setVisibility(View.GONE);
            if (isHtmlComplete) {
                activeBadge2.setVisibility(View.GONE);
                imgLvl2.setImageResource(R.drawable.circle_success);
                txtLvl2.setVisibility(View.GONE);
                if (tickLvl2 != null) tickLvl2.setVisibility(View.VISIBLE);
            } else {
                activeBadge2.setVisibility(View.VISIBLE);
                imgLvl2.setImageResource(R.drawable.circle_yellow);
                txtLvl2.setVisibility(View.VISIBLE);
                if (tickLvl2 != null) tickLvl2.setVisibility(View.GONE);
            }
            imgLvl2.setAlpha(1.0f);
        } else {
            lockBadge2.setVisibility(View.VISIBLE);
            activeBadge2.setVisibility(View.GONE);
            imgLvl2.setAlpha(0.5f);
        }
        
        // Level 3 (CSS) State
        if (isHtmlComplete) {
            lockBadge3.setVisibility(View.GONE);
            if (isCssComplete) {
                activeBadge3.setVisibility(View.GONE);
                imgLvl3.setImageResource(R.drawable.circle_success);
                txtLvl3.setVisibility(View.GONE);
                if (tickLvl3 != null) tickLvl3.setVisibility(View.VISIBLE);
            } else {
                activeBadge3.setVisibility(View.VISIBLE);
                imgLvl3.setImageResource(R.drawable.circle_yellow);
                txtLvl3.setVisibility(View.VISIBLE);
                if (tickLvl3 != null) tickLvl3.setVisibility(View.GONE);
            }
            imgLvl3.setAlpha(1.0f);
        } else {
            lockBadge3.setVisibility(View.VISIBLE);
            activeBadge3.setVisibility(View.GONE);
            imgLvl3.setAlpha(0.5f);
        }

        // Level 4 (JavaScript) State
        if (isCssComplete) {
            lockBadge4.setVisibility(View.GONE);
            if (isJsComplete) {
                activeBadge4.setVisibility(View.GONE);
                imgLvl4.setImageResource(R.drawable.circle_success);
                txtLvl4.setVisibility(View.GONE);
                if (tickLvl4 != null) tickLvl4.setVisibility(View.VISIBLE);
            } else {
                activeBadge4.setVisibility(View.VISIBLE);
                imgLvl4.setImageResource(R.drawable.circle_yellow);
                txtLvl4.setVisibility(View.VISIBLE);
                if (tickLvl4 != null) tickLvl4.setVisibility(View.GONE);
            }
            imgLvl4.setAlpha(1.0f);
        } else {
            lockBadge4.setVisibility(View.VISIBLE);
            activeBadge4.setVisibility(View.GONE);
            imgLvl4.setAlpha(0.5f);
        }

        // Level 5 (Version Control) State
        if (isJsComplete) {
            lockBadge5.setVisibility(View.GONE);
            activeBadge5.setVisibility(View.VISIBLE);
            imgLvl5.setImageResource(R.drawable.circle_yellow);
            imgLvl5.setAlpha(1.0f);
            txtLvl5.setVisibility(View.VISIBLE);
            if (tickLvl5 != null) tickLvl5.setVisibility(View.GONE);
        } else {
            lockBadge5.setVisibility(View.VISIBLE);
            activeBadge5.setVisibility(View.GONE);
            imgLvl5.setAlpha(0.5f);
        }
    }
}