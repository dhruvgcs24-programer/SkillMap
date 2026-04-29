package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
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
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        setupCardListeners();
        NavigationHelper.setup(this, bottomNav, R.id.nav_roadmaps);
        
        // We still load user data in background but don't update UI if views are missing
        loadUserData();
    }

    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid();
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // User data is loaded but we don't have a greeting text in the new layout
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        mDatabase.child("Users").child(uid).addValueEventListener(userListener);
    }

    private void setupCardListeners() {
        if (findViewById(R.id.cardFrontend) != null) {
            findViewById(R.id.cardFrontend).setOnClickListener(v ->
                    startActivity(new Intent(HomeActivity.this, FrontendActivity.class)));
        }

        if (findViewById(R.id.cardBackend) != null)
            findViewById(R.id.cardBackend).setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BackendActivity.class)));
        
        if (findViewById(R.id.cardFullStack) != null)
            findViewById(R.id.cardFullStack).setOnClickListener(v -> showComingSoon("Full Stack"));

        if (findViewById(R.id.btnPython) != null)
            findViewById(R.id.btnPython).setOnClickListener(v -> showComingSoon("Python"));

        if (findViewById(R.id.btnSQL) != null)
            findViewById(R.id.btnSQL).setOnClickListener(v -> showComingSoon("SQL"));

        if (findViewById(R.id.cardCreateRoadmap) != null) {
            findViewById(R.id.cardCreateRoadmap).setOnClickListener(v -> {
                startActivity(new Intent(HomeActivity.this, CreateRoadmapActivity.class));
            });
        }
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