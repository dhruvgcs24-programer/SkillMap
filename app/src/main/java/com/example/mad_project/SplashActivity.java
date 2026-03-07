package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // 1. System Bar Padding (Already in your code)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Animate the Logo
        ImageView logo = findViewById(R.id.ivSplashLogo); // Make sure this ID is in your XML

        // Start hidden and small
        logo.setAlpha(0f);
        logo.setScaleX(0.5f);
        logo.setScaleY(0.5f);

        // Animate to full size with a "Bounce" (Overshoot)
        logo.animate()
                .alpha(1f)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(1000)
                .setInterpolator(new OvershootInterpolator())
                .start();

        // 3. Move to LoginActivity after 2.5 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);

            // This adds a smooth fade transition between screens
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish(); // Destroys Splash so user can't "Go Back" to it
        }, 2500);
    }
}