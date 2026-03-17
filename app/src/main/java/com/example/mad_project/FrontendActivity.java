package com.example.mad_project;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FrontendActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_frontend);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize level images to set state programmatically
        ImageView imgLvl1 = findViewById(R.id.imgLvl1);
        ImageView imgLvl2 = findViewById(R.id.imgLvl2);

        // Level 1 is finished (show purple circle with tick)
        imgLvl1.setImageResource(R.drawable.circle_purple);

        // Level 2 is active (show yellow circle)
        imgLvl2.setImageResource(R.drawable.circle_yellow);
    }
}