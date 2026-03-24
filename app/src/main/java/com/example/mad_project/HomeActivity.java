package com.example.mad_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView tvWelcomeUser;
    private ImageView ivHeaderProfile;
    private ValueEventListener userListener; // To manage the listener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        MaterialCardView cardFrontend;
        cardFrontend = findViewById(R.id.cardFrontend);
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        ivHeaderProfile = findViewById(R.id.ivHeaderProfile);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        loadUserData();
        setupCardListeners();

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                return true;
            } else if (id == R.id.nav_progress) {
                startActivity(new Intent(HomeActivity.this, Progress.class));
                return true;
            } else return id == R.id.nav_roadmaps;
        });

        cardFrontend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FrontendActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();

            userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("firstName").getValue(String.class);
                        tvWelcomeUser.setText("Hi, " + name + "! Ready to learn?");

                        // Base64 Decoding
                        String base64String = snapshot.child("profileImageUrl").getValue(String.class);
                        if (base64String != null && !base64String.isEmpty()) {
                            try {
                                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ivHeaderProfile.setImageBitmap(decodedByte);
                                ivHeaderProfile.clearColorFilter(); // Remove any icon tints
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Prevent error toast if we are logging out
                    if (mAuth.getCurrentUser() != null) {
                        Toast.makeText(HomeActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            mDatabase.child("Users").child(uid).addValueEventListener(userListener);
        }
    }

    private void setupCardListeners() {
        findViewById(R.id.cardFrontend).setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, FrontendActivity.class)));

        findViewById(R.id.cardBackend).setOnClickListener(v -> openRoadmap("Backend"));
        findViewById(R.id.cardAndroid).setOnClickListener(v -> openRoadmap("Android"));
        findViewById(R.id.cardFullStack).setOnClickListener(v -> openRoadmap("Full Stack"));
        findViewById(R.id.cardDevOps).setOnClickListener(v -> openRoadmap("DevOps"));
        findViewById(R.id.cardAI).setOnClickListener(v -> openRoadmap("AI / ML"));

        findViewById(R.id.btnPython).setOnClickListener(v -> openRoadmap("Python"));
        findViewById(R.id.btnSQL).setOnClickListener(v -> openRoadmap("SQL"));
        findViewById(R.id.btnJS).setOnClickListener(v -> openRoadmap("JavaScript"));
        findViewById(R.id.btnJava).setOnClickListener(v -> openRoadmap("Java"));
    }

    private void openRoadmap(String roadmapName) {
        Toast.makeText(this, "Opening " + roadmapName + " Roadmap...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detach listener to prevent memory leaks or permission errors
        if (userListener != null && mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            mDatabase.child("Users").child(uid).removeEventListener(userListener);
        }
    }
}