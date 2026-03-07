package com.example.mad_project;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up); // Ensure this matches your XML filename

        // 1. Keep your existing Window Insets logic (This handles the blurs behind status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Initialize the views from your XML
        TextView tvSignUpTitle = findViewById(R.id.tvSignUpTitle);
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);

        // 3. Apply the Figma Gradient Effect to the "Sign Up" Title
        tvSignUpTitle.post(() -> {
            Shader textShader = new LinearGradient(0, 0, tvSignUpTitle.getWidth(), 0,
                    new int[]{
                            Color.parseColor("#4983F6"), // Figma Blue
                            Color.parseColor("#D946EF")  // Figma Purple/Pink
                    }, null, Shader.TileMode.CLAMP);
            tvSignUpTitle.getPaint().setShader(textShader);
            tvSignUpTitle.invalidate();
        });

        // 4. Set up the "Login" link to go back to the previous screen
        tvLoginLink.setOnClickListener(v -> {
            // This simply closes the Sign Up screen and returns to LoginActivity
            finish();
        });

        // 5. Handle the Register Button click
        btnRegister.setOnClickListener(v -> {
            // Add your registration logic here later
            android.widget.Toast.makeText(this, "Registration Started", android.widget.Toast.LENGTH_SHORT).show();
        });
    }
}