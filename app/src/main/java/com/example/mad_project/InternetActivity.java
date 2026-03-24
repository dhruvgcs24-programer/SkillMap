package com.example.mad_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class InternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);

        setup(R.id.topic1, R.id.tick1, "t1", prefs);
        setup(R.id.topic2, R.id.tick2, "t2", prefs);
        setup(R.id.topic3, R.id.tick3, "t3", prefs);
        setup(R.id.topic4, R.id.tick4, "t4", prefs);
        setup(R.id.topic5, R.id.tick5, "t5", prefs);
        setup(R.id.topic6, R.id.tick6, "t6", prefs);
    }

    private void setup(int topicId, int tickId, String key, SharedPreferences prefs) {
        LinearLayout topic = findViewById(topicId);
        ImageView tick = findViewById(tickId);

        tick.setVisibility(prefs.getBoolean(key, false) ? View.VISIBLE : View.GONE);

        topic.setOnClickListener(v -> {
            prefs.edit().putBoolean(key, true).apply();
            tick.setVisibility(View.VISIBLE);
        });
    }
}