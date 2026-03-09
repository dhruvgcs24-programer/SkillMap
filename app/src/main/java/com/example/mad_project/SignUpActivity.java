package com.example.mad_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextInputEditText etEmail, etPassword, etFirstName, etLastName, etPhone, etBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // UI references
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etBirthDate = findViewById(R.id.etBirthDate);

        TextView tvSignUpTitle = findViewById(R.id.tvSignUpTitle);
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);


        // Gradient title effect
        tvSignUpTitle.post(() -> {
            Shader textShader = new LinearGradient(
                    0,0,
                    tvSignUpTitle.getWidth(),0,
                    new int[]{Color.parseColor("#4983F6"), Color.parseColor("#D946EF")},
                    null,
                    Shader.TileMode.CLAMP);

            tvSignUpTitle.getPaint().setShader(textShader);
        });


        // Date picker
        etBirthDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    SignUpActivity.this,
                    (view, y, m, d) -> {
                        String date = d + "/" + (m+1) + "/" + y;
                        etBirthDate.setText(date);
                    },
                    year,month,day
            );

            dialog.show();
        });


        // Register button
        btnRegister.setOnClickListener(v -> registerUser());


        // Back to login
        tvLoginLink.setOnClickListener(v -> {

            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();

        });

    }



    private void registerUser(){

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();


        if(TextUtils.isEmpty(firstName)){
            etFirstName.setError("Enter first name");
            return;
        }

        if(TextUtils.isEmpty(lastName)){
            etLastName.setError("Enter last name");
            return;
        }

        if(TextUtils.isEmpty(email)){
            etEmail.setError("Enter email");
            return;
        }

        if(password.length() < 6){
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        if(TextUtils.isEmpty(birthDate)){
            etBirthDate.setError("Select birth date");
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        String userId = mAuth.getCurrentUser().getUid();

                        saveUserToDatabase(userId, firstName,lastName,email,phone,birthDate);

                    }else{

                        Toast.makeText(
                                SignUpActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                    }

                });

    }



    private void saveUserToDatabase(String userId,
                                    String firstName,
                                    String lastName,
                                    String email,
                                    String phone,
                                    String birthDate){

        Map<String,Object> userMap = new HashMap<>();

        userMap.put("firstName",firstName);
        userMap.put("lastName",lastName);
        userMap.put("email",email);
        userMap.put("phone",phone);
        userMap.put("birthDate",birthDate);

        mDatabase.child("Users")
                .child(userId)
                .setValue(userMap)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        Toast.makeText(
                                SignUpActivity.this,
                                "Registration Successful!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(
                                SignUpActivity.this,
                                HomeActivity.class);

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                    }else{

                        Toast.makeText(
                                SignUpActivity.this,
                                "Database error",
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }
}