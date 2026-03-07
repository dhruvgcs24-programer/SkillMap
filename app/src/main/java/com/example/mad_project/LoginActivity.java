package com.example.mad_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // This matches android:id="@+id/main" in XML
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views with matching IDs
        TextView tvTitle = findViewById(R.id.tvGradientTitle);
        TextView tvSignUp = findViewById(R.id.tvSignUpPrompt);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        // Apply Figma Gradient to Title
        tvTitle.post(() -> {
            Shader textShader = new LinearGradient(0, 0, tvTitle.getWidth(), 0,
                    new int[]{
                            Color.parseColor("#4983F6"), // Blue
                            Color.parseColor("#D946EF")  // Pink/Purple
                    }, null, Shader.TileMode.CLAMP);
            tvTitle.getPaint().setShader(textShader);
            tvTitle.invalidate();
        });

        // Navigation logic
        tvSignUp.setOnClickListener(v -> {
            // Replace SignUpActivity.class with your actual SignUp class name
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Toast.makeText(this, "Attempting Login...", Toast.LENGTH_SHORT).show();
        });
    }
}