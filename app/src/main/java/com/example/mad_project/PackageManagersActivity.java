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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

public class PackageManagersActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_package_managers);

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

        setup(R.id.topic1, R.id.check1, "pm1", "npm", R.id.expandable1);
        setup(R.id.topic2, R.id.check2, "pm2", "pnpm", R.id.expandable2);
        setup(R.id.topic3, R.id.check3, "pm3", "Yarn", R.id.expandable3);
        setup(R.id.topic4, R.id.check4, "pm4", "Bun", R.id.expandable4);

        // Mark All Logic
        checkMarkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingFromMarkAll) return;
            isUpdatingFromMarkAll = true;
            for (int i = 0; i < topicCheckBoxes.size(); i++) {
                CheckBox cb = topicCheckBoxes.get(i);
                cb.setChecked(isChecked);
                prefs.edit().putBoolean("pm" + (i + 1), isChecked).apply();
            }
            updateProgress();
            isUpdatingFromMarkAll = false;
        });

        findViewById(R.id.btnMarkAll).setOnClickListener(v -> checkMarkAll.toggle());

        // Topic 1 links (npm)
        findViewById(R.id.linkNpm1).setOnClickListener(v -> openUrl("https://www.npmjs.com/"));
        findViewById(R.id.linkNpm2).setOnClickListener(v -> openUrl("https://docs.npmjs.com/"));
        findViewById(R.id.linkNpm3).setOnClickListener(v -> openUrl("https://github.com/workshopper/how-to-npm"));
        findViewById(R.id.linkNpm4).setOnClickListener(v -> openUrl("https://peterxjang.com/blog/modern-javascript-explained-for-dinosaurs.html"));
        findViewById(R.id.linkNpm5).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=2V1UUhBJ62Y"));

        // Topic 2 links (pnpm)
        findViewById(R.id.linkPnpm1).setOnClickListener(v -> openUrl("https://pnpm.io/"));
        findViewById(R.id.linkPnpm2).setOnClickListener(v -> openUrl("https://blog.bitsrc.io/pnpm-javascript-package-manager-4b5abd59dc9"));
        findViewById(R.id.linkPnpm3).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=MvbReZDSKHI"));

        // Topic 3 links (Yarn)
        findViewById(R.id.linkYarn1).setOnClickListener(v -> openUrl("https://classic.yarnpkg.com/en/docs/getting-started"));
        findViewById(R.id.linkYarn2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=g9_6KmiBISk"));

        // Topic 4 links (Bun)
        findViewById(R.id.linkBun1).setOnClickListener(v -> openUrl("https://bun.com/"));
        findViewById(R.id.linkBun2).setOnClickListener(v -> openUrl("http://bun.com/docs"));
        findViewById(R.id.linkBun3).setOnClickListener(v -> openUrl("https://github.com/oven-sh/bun"));
        findViewById(R.id.linkBun4).setOnClickListener(v -> openUrl("https://kinsta.com/blog/bun-sh/"));
        findViewById(R.id.linkBun5).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=dWqNgzZwVJQ"));
        findViewById(R.id.linkBun6).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=U4JVw8K19uY"));

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
        if (prefs.getBoolean("pm1", false)) count++;
        if (prefs.getBoolean("pm2", false)) count++;
        if (prefs.getBoolean("pm3", false)) count++;
        if (prefs.getBoolean("pm4", false)) count++;

        progressBar.setProgress(count);
        progressCount.setText(count + "/4 Completed");
        int percent = (int) ((count / 4.0) * 100);
        progressText.setText("Progress: " + percent + "%");
    }
}
