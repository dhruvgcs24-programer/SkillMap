package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FrontendActivity extends AppCompatActivity {

    private LinearLayout lvl1, lvl2;
    private TextView txtLvl1, lockText, activeText;
    private ImageView imgLvl2;
    private DashedPathView dashedPathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontend);

        dashedPathView = findViewById(R.id.dashedPath);
        lvl1 = findViewById(R.id.lvl1);
        lvl2 = findViewById(R.id.lvl2);
        txtLvl1 = findViewById(R.id.txtLvl1);
        lockText = findViewById(R.id.lockText);
        activeText = findViewById(R.id.activeText);
        imgLvl2 = findViewById(R.id.imgLvl2);

        lvl1.setOnClickListener(v -> startActivity(new Intent(this, InternetActivity.class)));

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

        boolean completed = prefs.getBoolean("t1", false) && prefs.getBoolean("t2", false) &&
                prefs.getBoolean("t3", false) && prefs.getBoolean("t4", false) &&
                prefs.getBoolean("t5", false) && prefs.getBoolean("t6", false);

        if (completed) {
            // Level 2 - ACTIVE
            lvl2.setEnabled(true);
            lockText.setVisibility(View.GONE);
            activeText.setVisibility(View.VISIBLE);
            imgLvl2.setAlpha(1.0f);
        } else {
            // Level 2 - LOCKED
            lvl2.setEnabled(false);
            lockText.setVisibility(View.VISIBLE);
            activeText.setVisibility(View.GONE);
            imgLvl2.setAlpha(0.5f);
        }
    }
}