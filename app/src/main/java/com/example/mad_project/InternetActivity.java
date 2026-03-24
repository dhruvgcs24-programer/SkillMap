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

public class InternetActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private ProgressBar progressBar;
    private TextView progressText, progressCount;
    private ScrollView mainScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        progressCount = findViewById(R.id.progressCount);
        mainScrollView = findViewById(R.id.mainScrollView);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        setup(R.id.topic1, R.id.check1, "t1", "How does the internet work?", true);
        setup(R.id.topic2, R.id.check2, "t2", "What is HTTP?", false);
        setup(R.id.topic3, R.id.check3, "t3", "What is Domain Name?", false);
        setup(R.id.topic4, R.id.check4, "t4", "What is Hosting?", false);
        setup(R.id.topic5, R.id.check5, "t5", "DNS and how it works?", false);
        setup(R.id.topic6, R.id.check6, "t6", "Browsers and how they work?", false);

        // Click listeners for links in Topic 1
        findViewById(R.id.linkArticle).setOnClickListener(v -> openUrl("https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/How_does_the_Internet_work"));
        findViewById(R.id.linkVideo).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=7_LPdttKXPc"));

        updateProgress();
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

    private void setup(int topicId, int checkId, String key, String topicName, boolean hasDescription) {
        LinearLayout topic = findViewById(topicId);
        CheckBox checkBox = findViewById(checkId);

        boolean done = prefs.getBoolean(key, false);
        checkBox.setChecked(done);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(key, isChecked).apply();
            updateProgress();
        });

        topic.setOnClickListener(v -> {
            if (hasDescription && topicId == R.id.topic1) {
                View expandable = findViewById(R.id.expandable1);
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

    private void updateProgress() {
        int count = 0;
        if (prefs.getBoolean("t1", false)) count++;
        if (prefs.getBoolean("t2", false)) count++;
        if (prefs.getBoolean("t3", false)) count++;
        if (prefs.getBoolean("t4", false)) count++;
        if (prefs.getBoolean("t5", false)) count++;
        if (prefs.getBoolean("t6", false)) count++;

        progressBar.setProgress(count);
        progressCount.setText(count + "/6 Completed");
        int percent = (int) ((count / 6.0) * 100);
        progressText.setText("Progress: " + percent + "%");
    }
}