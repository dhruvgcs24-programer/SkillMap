package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etPhone;
    private ShapeableImageView ivProfilePic;
    private TextView tvProfileName, tvLevelInfo, tvStreakValue, tvCoursesCompleted;
    private MaterialButton btnSave, btnLogout;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // ── Firebase auth guard ───────────────────────────────────────────────
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        // ── View bindings ─────────────────────────────────────────────────────
        etFirstName         = findViewById(R.id.etEditFirstName);
        etPhone             = findViewById(R.id.etEditPhone);
        ivProfilePic        = findViewById(R.id.ivProfilePic);
        tvProfileName       = findViewById(R.id.tvProfileName);
        tvLevelInfo         = findViewById(R.id.tvLevelInfo);
        tvStreakValue       = findViewById(R.id.tvStreakValue);
        tvCoursesCompleted  = findViewById(R.id.tvCoursesCompleted);
        btnSave             = findViewById(R.id.btnSaveProfile);
        btnLogout           = findViewById(R.id.btnLogout);

        ImageView btnBack     = findViewById(R.id.btnBack);
        ImageView btnSettings = findViewById(R.id.btnSettings);

        // ── Toolbar buttons ───────────────────────────────────────────────────
        btnBack.setOnClickListener(v -> finish());
        btnSettings.setOnClickListener(v ->
                Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show());

        // ── Bottom Navigation (shared helper) ─────────────────────────────────
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavigationHelper.setup(this, bottomNav, R.id.nav_profile);

        // ── Load Firebase user data ───────────────────────────────────────────
        mDatabase.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String firstName = snapshot.child("firstName").getValue(String.class);
                etFirstName.setText(firstName);
                tvProfileName.setText(firstName != null ? firstName : "User Name");
                etPhone.setText(snapshot.child("phone").getValue(String.class));

                // Use ImageUtils instead of inline Base64 decode
                String base64 = snapshot.child("profileImageUrl").getValue(String.class);
                ImageUtils.loadBase64Image(base64, ivProfilePic);
            }
        });

        loadDashboardStats();

        // ── Image picker ──────────────────────────────────────────────────────
        ActivityResultLauncher<Intent> picker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(), imageUri);
                            // Use ImageUtils for encoding
                            encodedImage = ImageUtils.encodeToBase64(bitmap);
                            ivProfilePic.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        findViewById(R.id.btnChangePic).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            picker.launch(intent);
        });

        // ── Save profile ──────────────────────────────────────────────────────
        btnSave.setOnClickListener(v -> {
            String name  = etFirstName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty()) { etFirstName.setError("Enter name"); return; }

            Map<String, Object> map = new HashMap<>();
            map.put("firstName", name);
            map.put("phone", phone);
            if (!encodedImage.isEmpty()) map.put("profileImageUrl", encodedImage);

            mDatabase.updateChildren(map).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                tvProfileName.setText(name);
            });
        });

        // ── Logout ────────────────────────────────────────────────────────────
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Computes and renders profile statistics from SharedPreferences.
     */
    private void loadDashboardStats() {
        SharedPreferences prefs = getSharedPreferences(ProgressPrefs.PREFS_NAME, MODE_PRIVATE);
        ProgressCalculator calc = new ProgressCalculator(prefs);

        int level = Math.max(1, calc.levelsCompleted + 1);
        tvCurrentLevelInfo(level);
        tvCoursesCompleted.setText(String.valueOf(calc.levelsCompleted));

        int streak = prefs.getInt(ProgressPrefs.LEARNING_STREAK, 0);
        tvStreakValue.setText(String.valueOf(streak));
    }

    private void tvCurrentLevelInfo(int level) {
        String info = "Level " + level;
        if (level < 3) info += " Explorer";
        else if (level < 6) info += " Enthusiast";
        else if (level < 9) info += " Master";
        else info += " Legend";
        tvLevelInfo.setText(info);
    }
}