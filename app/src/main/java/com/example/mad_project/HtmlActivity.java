package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class HtmlActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private CheckBox checkHtmlComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);
        checkHtmlComplete = findViewById(R.id.checkHtmlComplete);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        // Single Checkbox Logic for HTML Level Completion
        // We will use "h_complete" as the key to indicate the entire HTML level is done
        boolean isDone = prefs.getBoolean("h_complete", false);
        checkHtmlComplete.setChecked(isDone);

        checkHtmlComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("h_complete", isChecked).apply();
            if (isChecked) {
                Toast.makeText(this, "HTML level marked as completed!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnCompleteHtml).setOnClickListener(v -> checkHtmlComplete.toggle());

        // Learning Resources Click Listeners
        findViewById(R.id.linkHtmlRoadmap).setOnClickListener(v -> Toast.makeText(HtmlActivity.this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
        findViewById(R.id.linkHtmlCourse).setOnClickListener(v -> openUrl("https://www.freecodecamp.org/learn/2022/responsive-web-design/"));
        findViewById(R.id.linkHtmlVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=mJgBOIoGihA"));
        findViewById(R.id.linkHtmlVideo2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=pQN-pnXPaVg"));
        findViewById(R.id.linkHtmlFeed).setOnClickListener(v -> openUrl("https://dev.to/t/html"));
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
}