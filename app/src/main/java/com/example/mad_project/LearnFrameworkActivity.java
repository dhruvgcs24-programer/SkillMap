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

public class LearnFrameworkActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_learn_framework);

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

        setup(R.id.topic1, R.id.check1, "fw1", "React", R.id.expandable1);
        setup(R.id.topic2, R.id.check2, "fw2", "Vue.js", R.id.expandable2);
        setup(R.id.topic3, R.id.check3, "fw3", "Angular", R.id.expandable3);
        setup(R.id.topic4, R.id.check4, "fw4", "Svelte", R.id.expandable4);
        setup(R.id.topic5, R.id.check5, "fw5", "SolidJS", R.id.expandable5);

        // Mark All Logic
        checkMarkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingFromMarkAll) return;
            isUpdatingFromMarkAll = true;
            for (int i = 0; i < topicCheckBoxes.size(); i++) {
                CheckBox cb = topicCheckBoxes.get(i);
                cb.setChecked(isChecked);
                prefs.edit().putBoolean("fw" + (i + 1), isChecked).apply();
            }
            updateProgress();
            isUpdatingFromMarkAll = false;
        });

        findViewById(R.id.btnMarkAll).setOnClickListener(v -> checkMarkAll.toggle());

        // Topic 1 links (React)
        findViewById(R.id.link1_1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=Bvwq_S0n2pk"));
        findViewById(R.id.link1_2).setOnClickListener(v -> openUrl("https://react.dev/"));

        // Topic 2 links (Vue.js)
        findViewById(R.id.link2_1).setOnClickListener(v -> openUrl("https://vuejs.org/"));
        findViewById(R.id.link2_2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=VeNfHj6MhgA"));

        // Topic 3 links (Angular)
        findViewById(R.id.link3_1).setOnClickListener(v -> openUrl("https://angular.dev/tutorials/learn-angular"));
        findViewById(R.id.link3_2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=3qBXWUpoPHo"));

        // Topic 4 links (Svelte)
        findViewById(R.id.link4_1).setOnClickListener(v -> openUrl("https://www.youtube.com/playlist?list=PL4cUxeGkcC9hlbrVO_2QFVqVPhlZmz7tO"));
        findViewById(R.id.link4_2).setOnClickListener(v -> openUrl("https://svelte.dev/"));
        findViewById(R.id.link4_3).setOnClickListener(v -> openUrl("https://thenewstack.io/svelte-and-the-future-of-front-end-development/"));

        // Topic 5 links (SolidJS)
        findViewById(R.id.link5_1).setOnClickListener(v -> openUrl("https://www.solidjs.com/"));
        findViewById(R.id.link5_2).setOnClickListener(v -> openUrl("https://www.solidjs.com/tutorial/introduction_basics"));
        findViewById(R.id.link5_3).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=hw3Bx5vxKl0"));

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
        for (int i = 1; i <= 5; i++) {
            if (prefs.getBoolean("fw" + i, false)) count++;
        }

        progressBar.setProgress(count);
        progressCount.setText(count + "/5 Completed");
        int percent = (int) ((count / 5.0) * 100);
        progressText.setText("Progress: " + percent + "%");
    }
}
