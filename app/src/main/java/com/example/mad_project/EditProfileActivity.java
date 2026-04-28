package com.example.mad_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etPhone;
    private ShapeableImageView ivProfilePic;
    private TextView tvProfileName;
    private MaterialButton btnSave, btnLogout;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String encodedImage = "";
    private TextView tvLevelInfo;
    private TextView tvCurrentLevel;
    private TextView tvNextLevel;
    private TextView tvXpText;
    private TextView tvStreakValue;
    private TextView tvCoursesCompleted;
    private TextView tvTimeSpent;
    private TextView tvRoadmapPercent;
    private TextView tvRoadmapCount;
    private TextView tvOverallPercent;
    private TextView tvSkillHtml;
    private TextView tvSkillCss;
    private TextView tvSkillJs;
    private TextView tvSkillReact;
    private TextView tvRecent1;
    private TextView tvRecent2;
    private TextView tvRecent3;
    private ProgressBar pbXp;
    private ProgressBar pbRoadmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        // Initialize Views
        etFirstName = findViewById(R.id.etEditFirstName);
        etPhone = findViewById(R.id.etEditPhone);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvProfileName = findViewById(R.id.tvProfileName);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnSettings = findViewById(R.id.btnSettings);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        MaterialButton btnContinueLearning = findViewById(R.id.btnContinueLearning);

        tvLevelInfo = findViewById(R.id.tvLevelInfo);
        tvCurrentLevel = findViewById(R.id.tvCurrentLevel);
        tvNextLevel = findViewById(R.id.tvNextLevel);
        tvXpText = findViewById(R.id.tvXpText);
        tvStreakValue = findViewById(R.id.tvStreakValue);
        tvCoursesCompleted = findViewById(R.id.tvCoursesCompleted);
        tvTimeSpent = findViewById(R.id.tvTimeSpent);
        tvRoadmapPercent = findViewById(R.id.tvRoadmapPercent);
        tvRoadmapCount = findViewById(R.id.tvRoadmapCount);
        tvOverallPercent = findViewById(R.id.tvOverallPercent);
        tvSkillHtml = findViewById(R.id.tvSkillHtml);
        tvSkillCss = findViewById(R.id.tvSkillCss);
        tvSkillJs = findViewById(R.id.tvSkillJs);
        tvSkillReact = findViewById(R.id.tvSkillReact);
        tvRecent1 = findViewById(R.id.tvRecent1);
        tvRecent2 = findViewById(R.id.tvRecent2);
        tvRecent3 = findViewById(R.id.tvRecent3);
        pbXp = findViewById(R.id.pbXp);
        pbRoadmap = findViewById(R.id.pbRoadmap);

        // Back button functionality
        btnBack.setOnClickListener(v -> finish());
        btnSettings.setOnClickListener(v -> Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show());
        btnContinueLearning.setOnClickListener(v -> startActivity(new Intent(this, FrontendActivity.class)));

        // Bottom Navigation Logic
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_roadmaps) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_progress) {
                startActivity(new Intent(this, Progress.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });

        // Load current data
        mDatabase.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String firstName = snapshot.child("firstName").getValue(String.class);
                etFirstName.setText(firstName);
                tvProfileName.setText(firstName != null ? firstName : "User Name");
                etPhone.setText(snapshot.child("phone").getValue(String.class));

                String base64String = snapshot.child("profileImageUrl").getValue(String.class);
                if (base64String != null && !base64String.isEmpty()) {
                    try {
                        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ivProfilePic.setImageBitmap(decodedByte);
                    } catch (Exception ignored) {}
                }
            }
        });

        loadDashboardStats();

        // Image Picker logic
        ActivityResultLauncher<Intent> picker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            encodedImage = encodeImage(bitmap);
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

        btnSave.setOnClickListener(v -> {
            String name = etFirstName.getText().toString().trim();
            String phone = etEditPhone();

            if (name.isEmpty()) {
                etFirstName.setError("Enter name");
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("firstName", name);
            map.put("phone", phone);
            if (!encodedImage.isEmpty()) {
                map.put("profileImageUrl", encodedImage);
            }

            mDatabase.updateChildren(map).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                tvProfileName.setText(name);
            });
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String etEditPhone() {
        return etPhone.getText().toString().trim();
    }

    private void loadDashboardStats() {
        android.content.SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);

        int internetTopicsDone = 0;
        for (int i = 1; i <= 6; i++) {
            if (prefs.getBoolean("t" + i, false)) internetTopicsDone++;
        }

        int htmlDone = prefs.getBoolean("h_complete", false) ? 1 : 0;
        int cssDone = prefs.getBoolean("c_complete", false) ? 1 : 0;
        int jsDone = prefs.getBoolean("j_complete", false) ? 1 : 0;
        int vcDone = prefs.getBoolean("vc1", false) ? 1 : 0;
        int vcsDone = (prefs.getBoolean("vcs1", false) ? 1 : 0) + (prefs.getBoolean("vcs2", false) ? 1 : 0);
        int pmDone = (prefs.getBoolean("pm1", false) ? 1 : 0) + (prefs.getBoolean("pm2", false) ? 1 : 0)
                + (prefs.getBoolean("pm3", false) ? 1 : 0) + (prefs.getBoolean("pm4", false) ? 1 : 0);
        int cssfDone = prefs.getBoolean("cssf1", false) ? 1 : 0;
        int fwDone = (prefs.getBoolean("fw1", false) ? 1 : 0) + (prefs.getBoolean("fw2", false) ? 1 : 0)
                + (prefs.getBoolean("fw3", false) ? 1 : 0) + (prefs.getBoolean("fw4", false) ? 1 : 0)
                + (prefs.getBoolean("fw5", false) ? 1 : 0);

        int totalTopics = 22;
        int completedTopics = internetTopicsDone + htmlDone + cssDone + jsDone + vcDone + vcsDone + pmDone + cssfDone + fwDone;
        int roadmapPercent = (completedTopics * 100) / totalTopics;

        int completedLevels = 0;
        if (internetTopicsDone == 6) completedLevels++;
        if (htmlDone == 1) completedLevels++;
        if (cssDone == 1) completedLevels++;
        if (jsDone == 1) completedLevels++;
        if (vcDone == 1) completedLevels++;
        if (vcsDone == 2) completedLevels++;
        if (pmDone == 4) completedLevels++;
        if (cssfDone == 1) completedLevels++;
        if (fwDone == 5) completedLevels++;

        int level = Math.max(1, completedLevels + 1);
        int xpPercent = roadmapPercent;

        pbXp.setProgress(xpPercent);
        pbRoadmap.setProgress(roadmapPercent);

        tvCurrentLevel.setText("Level " + level);
        tvNextLevel.setText("Level " + (level + 1));
        tvXpText.setText(completedTopics + " / " + totalTopics + " Topics");
        tvLevelInfo.setText("Level " + level + " Frontend Enthusiast");

        tvRoadmapPercent.setText(roadmapPercent + "%");
        tvRoadmapCount.setText(completedTopics + " of " + totalTopics + " topics completed");
        tvOverallPercent.setText("Overall Progress: " + roadmapPercent + "%");
        tvCoursesCompleted.setText(String.valueOf(completedLevels));

        // Use only explicitly tracked values. If not tracked yet, show 0.
        int streak = prefs.getInt("learning_streak", 0);
        int weeklyMinutes = prefs.getInt("time_spent_week_minutes", 0);
        tvStreakValue.setText(String.valueOf(streak));
        tvTimeSpent.setText((weeklyMinutes / 60) + "h " + (weeklyMinutes % 60) + "m");

        tvSkillHtml.setText("HTML: " + ((htmlDone * 100)) + "%");
        tvSkillCss.setText("CSS: " + (cssDone * 100) + "%");
        tvSkillJs.setText("JavaScript: " + (jsDone * 100) + "%");
        tvSkillReact.setText("React: " + ((prefs.getBoolean("fw1", false) ? 100 : 0)) + "%");

        List<String> recent = new ArrayList<>();
        if (prefs.getBoolean("fw5", false)) recent.add("Completed SolidJS");
        if (prefs.getBoolean("fw4", false)) recent.add("Completed Svelte");
        if (prefs.getBoolean("fw3", false)) recent.add("Completed Angular");
        if (prefs.getBoolean("fw2", false)) recent.add("Completed Vue.js");
        if (prefs.getBoolean("fw1", false)) recent.add("Completed React");
        if (prefs.getBoolean("cssf1", false)) recent.add("Completed CSS Frameworks");
        if (prefs.getBoolean("pm4", false)) recent.add("Completed Bun");
        if (prefs.getBoolean("pm3", false)) recent.add("Completed Yarn");
        if (prefs.getBoolean("pm2", false)) recent.add("Completed pnpm");
        if (prefs.getBoolean("pm1", false)) recent.add("Completed npm");
        if (prefs.getBoolean("vcs2", false)) recent.add("Completed GitHub");
        if (prefs.getBoolean("vcs1", false)) recent.add("Completed GitLab");
        if (prefs.getBoolean("vc1", false)) recent.add("Completed Version Control");
        if (prefs.getBoolean("j_complete", false)) recent.add("Completed JavaScript");
        if (prefs.getBoolean("c_complete", false)) recent.add("Completed CSS");
        if (prefs.getBoolean("h_complete", false)) recent.add("Completed HTML");

        if (recent.isEmpty()) {
            tvRecent1.setText("- No completed activity yet");
            tvRecent2.setText("- Start Internet Fundamentals");
            tvRecent3.setText("- Progress will appear here");
        } else {
            tvRecent1.setText("- " + recent.get(0));
            tvRecent2.setText(recent.size() > 1 ? "- " + recent.get(1) : "- Keep learning to unlock more");
            tvRecent3.setText(recent.size() > 2 ? "- " + recent.get(2) : "- Keep learning to unlock more");
        }
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}