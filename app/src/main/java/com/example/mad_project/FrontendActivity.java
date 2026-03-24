package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FrontendActivity extends AppCompatActivity {

    private LinearLayout lvl1, lvl2;
    private TextView lockText, activeText;
    private ImageView imgLvl2;
    private DashedPathView dashedPathView;
    private boolean isLevel2Unlocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontend);

        dashedPathView = findViewById(R.id.dashedPath);
        lvl1 = findViewById(R.id.lvl1);
        lvl2 = findViewById(R.id.lvl2);
        lockText = findViewById(R.id.lockText);
        activeText = findViewById(R.id.activeText);
        imgLvl2 = findViewById(R.id.imgLvl2);

        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnShare = findViewById(R.id.btnShare);

        btnBack.setOnClickListener(v -> finish());

        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Frontend Roadmap");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm learning the Frontend roadmap in SkillMap.");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        lvl1.setOnClickListener(v ->
                startActivity(new Intent(this, InternetActivity.class)));

        lvl2.setOnClickListener(v -> {
            if (isLevel2Unlocked) {
                Toast.makeText(this, "HTML level will be added next", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Complete all Internet topics first", Toast.LENGTH_SHORT).show();
            }
        });

        lvl1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float x1 = lvl1.getX() + (lvl1.getWidth() / 2f);
                float y1 = lvl1.getY() + (lvl1.getHeight() / 2f);
                float x2 = lvl2.getX() + (lvl2.getWidth() / 2f);
                float y2 = lvl2.getY() + (lvl2.getHeight() / 2f);

                dashedPathView.setPathPoints(x1, y1, x2, y2);
                lvl1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);

        boolean completed = prefs.getBoolean("t1", false)
                && prefs.getBoolean("t2", false)
                && prefs.getBoolean("t3", false)
                && prefs.getBoolean("t4", false)
                && prefs.getBoolean("t5", false)
                && prefs.getBoolean("t6", false);

        isLevel2Unlocked = completed;

        if (completed) {
            lvl2.setEnabled(true);
            lockText.setVisibility(View.GONE);
            activeText.setVisibility(View.VISIBLE);
            imgLvl2.setAlpha(1.0f);
        } else {
            lvl2.setEnabled(true);
            lockText.setVisibility(View.VISIBLE);
            activeText.setVisibility(View.GONE);
            imgLvl2.setAlpha(0.5f);
        }
    }
}