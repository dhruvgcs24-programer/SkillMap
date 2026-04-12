package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

public class VersionControlActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private ProgressBar progressBar;
    private TextView progressText, progressCount;
    private ScrollView mainScrollView;
    private CheckBox checkMarkAll;
    private List<CheckBox> topicCheckBoxes = new ArrayList<>();
    private boolean isUpdatingFromMarkAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_control);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        progressCount = findViewById(R.id.progressCount);
        mainScrollView = findViewById(R.id.mainScrollView);
        checkMarkAll = findViewById(R.id.checkMarkAll);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        setup(R.id.topic1, R.id.check1, "vc1", "Git", R.id.expandable1);

        // Mark All Logic
        checkMarkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingFromMarkAll) return;
            isUpdatingFromMarkAll = true;
            for (int i = 0; i < topicCheckBoxes.size(); i++) {
                CheckBox cb = topicCheckBoxes.get(i);
                cb.setChecked(isChecked);
                prefs.edit().putBoolean("vc" + (i + 1), isChecked).apply();
            }
            updateProgress();
            isUpdatingFromMarkAll = false;
        });

        findViewById(R.id.btnMarkAll).setOnClickListener(v -> checkMarkAll.toggle());

        // Topic 1 links
        findViewById(R.id.linkRoadmap).setOnClickListener(v -> Toast.makeText(this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
        findViewById(R.id.linkArticle1).setOnClickListener(v -> openUrl("https://cs.fyi/guide/git-cheatsheet"));
        findViewById(R.id.linkArticle2).setOnClickListener(v -> openUrl("https://thenewstack.io/tutorial-git-for-absolutely-everyone/"));
        findViewById(R.id.linkVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=SWYqp7iY_Tc"));

        updateProgress();
        syncMarkAllState();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void toggleTheme() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }

    private void setup(int topicId, int checkId, String key, String topicName, int expandableId) {
        LinearLayout topic = findViewById(topicId);
        CheckBox checkBox = findViewById(checkId);
        topicCheckBoxes.add(checkBox);

        boolean done = prefs.getBoolean(key, false);
        checkBox.setChecked(done);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isUpdatingFromMarkAll) {
                prefs.edit().putBoolean(key, isChecked).apply();
                updateProgress();
                syncMarkAllState();
            }
        });

        topic.setOnClickListener(v -> {
            if (expandableId != 0) {
                View expandable = findViewById(expandableId);
                if (expandable.getVisibility() == View.GONE) {
                    expandable.setVisibility(View.VISIBLE);
                    mainScrollView.post(() -> {
                        mainScrollView.smoothScrollTo(0, topic.getTop());
                    });
                } else {
                    expandable.setVisibility(View.GONE);
                }
            } else {
                checkBox.toggle();
            }
        });
    }

    private void syncMarkAllState() {
        boolean allChecked = true;
        for (CheckBox cb : topicCheckBoxes) {
            if (!cb.isChecked()) {
                allChecked = false;
                break;
            }
        }
        isUpdatingFromMarkAll = true;
        checkMarkAll.setChecked(allChecked);
        isUpdatingFromMarkAll = false;
    }

    private void updateProgress() {
        int count = 0;
        if (prefs.getBoolean("vc1", false)) count++;

        progressBar.setProgress(count);
        progressCount.setText(count + "/1 Completed");
        int percent = (int) ((count / 1.0) * 100);
        progressText.setText("Progress: " + percent + "%");
    }
}
