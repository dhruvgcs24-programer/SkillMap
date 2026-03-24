package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class Progress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvPercent = findViewById(R.id.tvPercent);
        TextView tvCompleted = findViewById(R.id.tvCompletedLabel);
        ProgressBar pb1 = findViewById(R.id.pb1);
        MaterialButton btnContinue = findViewById(R.id.btnContinueLearning);

        SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);

        int completed = 0;
        if (prefs.getBoolean("t1", false)) completed++;
        if (prefs.getBoolean("t2", false)) completed++;
        if (prefs.getBoolean("t3", false)) completed++;
        if (prefs.getBoolean("t4", false)) completed++;
        if (prefs.getBoolean("t5", false)) completed++;
        if (prefs.getBoolean("t6", false)) completed++;

        int percent = (completed * 100) / 6;

        tvPercent.setText(percent + "%");
        tvCompleted.setText(completed + " of 6 topics completed");
        pb1.setProgress(percent);

        btnContinue.setOnClickListener(v ->
                startActivity(new Intent(Progress.this, FrontendActivity.class)));
    }
}