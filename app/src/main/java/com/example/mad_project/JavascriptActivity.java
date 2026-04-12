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

public class JavascriptActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private CheckBox checkJavascriptComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_javascript);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);
        checkJavascriptComplete = findViewById(R.id.checkJavascriptComplete);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        // Single Checkbox Logic for JavaScript Level Completion
        boolean isDone = prefs.getBoolean("j_complete", false);
        checkJavascriptComplete.setChecked(isDone);

        checkJavascriptComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("j_complete", isChecked).apply();
            if (isChecked) {
                Toast.makeText(this, "JavaScript level marked as completed!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnCompleteJavascript).setOnClickListener(v -> checkJavascriptComplete.toggle());

        // Learning Resources Click Listeners
        findViewById(R.id.linkJavascriptRoadmap).setOnClickListener(v -> 
            Toast.makeText(JavascriptActivity.this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
        
        findViewById(R.id.linkJavascriptArticle1).setOnClickListener(v -> 
            openUrl("https://javascript.info/"));
        
        findViewById(R.id.linkJavascriptArticle2).setOnClickListener(v -> 
            openUrl("https://javascript30.com/"));
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
