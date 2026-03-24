package com.example.mad_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InternetActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);

        setup(R.id.topic1, R.id.tick1, "t1", "How does the internet work?");
        setup(R.id.topic2, R.id.tick2, "t2", "What is HTTP?");
        setup(R.id.topic3, R.id.tick3, "t3", "What is Domain Name?");
        setup(R.id.topic4, R.id.tick4, "t4", "What is Hosting?");
        setup(R.id.topic5, R.id.tick5, "t5", "DNS and how it works?");
        setup(R.id.topic6, R.id.tick6, "t6", "Browsers and how they work?");
    }

    private void setup(int topicId, int tickId, String key, String topicName) {
        LinearLayout topic = findViewById(topicId);
        ImageView tick = findViewById(tickId);

        boolean done = prefs.getBoolean(key, false);
        tick.setVisibility(done ? View.VISIBLE : View.GONE);

        topic.setOnClickListener(v -> {
            boolean alreadyDone = prefs.getBoolean(key, false);

            if (!alreadyDone) {
                prefs.edit().putBoolean(key, true).apply();
                tick.setVisibility(View.VISIBLE);
                Toast.makeText(this, topicName + " marked as completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, topicName + " already completed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}