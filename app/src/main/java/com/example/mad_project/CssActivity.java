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

public class CssActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private CheckBox checkCssComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_css);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);
        checkCssComplete = findViewById(R.id.checkCssComplete);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        // Single Checkbox Logic for CSS Level Completion
        boolean isDone = prefs.getBoolean("c_complete", false);
        checkCssComplete.setChecked(isDone);

        checkCssComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("c_complete", isChecked).apply();
            if (isChecked) {
                Toast.makeText(this, "CSS level marked as completed!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnCompleteCss).setOnClickListener(v -> checkCssComplete.toggle());

        // Learning Resources Click Listeners
        findViewById(R.id.linkCssRoadmap).setOnClickListener(v -> 
            Toast.makeText(CssActivity.this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
        
        findViewById(R.id.linkCssCourse).setOnClickListener(v -> 
            openUrl("https://www.freecodecamp.org/learn/2022/responsive-web-design/"));
        
        findViewById(R.id.linkCssArticle).setOnClickListener(v -> 
            openUrl("https://web.dev/learn/css/"));
            
        findViewById(R.id.linkCssVideo1).setOnClickListener(v -> 
            openUrl("https://www.youtube.com/watch?v=n4R2E7O-Ngo"));
            
        findViewById(R.id.linkCssVideo2).setOnClickListener(v -> 
            openUrl("https://www.youtube.com/watch?v=G3e-cpL7ofc"));
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