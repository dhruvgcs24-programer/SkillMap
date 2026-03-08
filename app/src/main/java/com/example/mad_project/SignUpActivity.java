package com.example.mad_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText etEmail, etPassword, etFirstName, etLastName, etPhone, etBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://mad-project-d7e96-default-rtdb.firebaseio.com/").getReference();

        // Initialize UI components with correct IDs
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etBirthDate = findViewById(R.id.etBirthDate);

        TextView tvSignUpTitle = findViewById(R.id.tvSignUpTitle);
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);

        // Gradient Title effect
        tvSignUpTitle.post(() -> {
            Shader textShader = new LinearGradient(0, 0, tvSignUpTitle.getWidth(), 0,
                    new int[]{Color.parseColor("#4983F6"), Color.parseColor("#D946EF")},
                    null, Shader.TileMode.CLAMP);
            tvSignUpTitle.getPaint().setShader(textShader);
            tvSignUpTitle.invalidate();
        });

        // Set up the Date Picker Dialog
        etBirthDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignUpActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String date = String.format("%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year1);
                        etBirthDate.setText(date);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        btnRegister.setOnClickListener(v -> registerUser());
        tvLoginLink.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError("Please select your birth date");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        saveUserDetails(userId, email);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserDetails(String userId, String email) {
        // 1. Collect all data from the form
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("firstName", etFirstName.getText().toString().trim());
        userMap.put("lastName", etLastName.getText().toString().trim());
        userMap.put("phone", etPhone.getText().toString().trim());
        userMap.put("birthDate", etBirthDate.getText().toString().trim());

        // 2. Save to "Users" node in Realtime Database
        mDatabase.child("Users").child(userId).setValue(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                        // 3. AUTOMATIC LOGIN: Navigate to HomeActivity immediately
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);

                        // Clear the activity stack so they can't go "back" to the Sign Up page
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        finish(); // Closes the SignUpActivity
                    } else {
                        Toast.makeText(SignUpActivity.this, "Database Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
