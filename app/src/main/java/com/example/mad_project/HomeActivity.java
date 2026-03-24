package com.example.mad_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView tvWelcomeUser;
    private ImageView ivHeaderProfile;
    private ValueEventListener userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        ivHeaderProfile = findViewById(R.id.ivHeaderProfile);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        loadUserData();
        setupCardListeners();

        bottomNav.setSelectedItemId(R.id.nav_roadmaps);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_roadmaps) {
                return true;
            } else if (id == R.id.nav_progress) {
                startActivity(new Intent(HomeActivity.this, Progress.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid();

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("firstName").getValue(String.class);
                    if (name == null || name.trim().isEmpty()) {
                        name = "Explorer";
                    }

                    tvWelcomeUser.setText("Hi, " + name + "! Ready to learn?");

                    String base64String = snapshot.child("profileImageUrl").getValue(String.class);
                    if (base64String != null && !base64String.isEmpty()) {
                        try {
                            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            if (decodedByte != null) {
                                ivHeaderProfile.setImageBitmap(decodedByte);
                                ivHeaderProfile.clearColorFilter();
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (mAuth.getCurrentUser() != null) {
                    Toast.makeText(HomeActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mDatabase.child("Users").child(uid).addValueEventListener(userListener);
    }

    private void setupCardListeners() {
        findViewById(R.id.cardFrontend).setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, FrontendActivity.class)));

        findViewById(R.id.cardBackend).setOnClickListener(v -> showComingSoon("Backend"));
        findViewById(R.id.cardAndroid).setOnClickListener(v -> showComingSoon("Android"));
        findViewById(R.id.cardFullStack).setOnClickListener(v -> showComingSoon("Full Stack"));
        findViewById(R.id.cardDevOps).setOnClickListener(v -> showComingSoon("DevOps"));
        findViewById(R.id.cardAI).setOnClickListener(v -> showComingSoon("AI / ML"));

        findViewById(R.id.btnPython).setOnClickListener(v -> showComingSoon("Python"));
        findViewById(R.id.btnSQL).setOnClickListener(v -> showComingSoon("SQL"));
        findViewById(R.id.btnJS).setOnClickListener(v -> showComingSoon("JavaScript"));
        findViewById(R.id.btnJava).setOnClickListener(v -> showComingSoon("Java"));
    }

    private void showComingSoon(String roadmapName) {
        Toast.makeText(this, roadmapName + " roadmap will be added next", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userListener != null && mAuth != null && mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            mDatabase.child("Users").child(uid).removeEventListener(userListener);
        }
    }
}