package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

    // Stats views
    private TextView tvLevelInfo, tvCurrentLevel, tvNextLevel, tvXpText;
    private TextView tvStreakValue, tvCoursesCompleted, tvTimeSpent;
    private TextView tvRoadmapPercent, tvRoadmapCount, tvOverallPercent;
    private TextView tvSkillHtml, tvSkillCss, tvSkillJs, tvSkillReact;
    private TextView tvRecent1, tvRecent2, tvRecent3;
    private ProgressBar pbXp, pbRoadmap;

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
        etFirstName  = findViewById(R.id.etEditFirstName);
        etPhone      = findViewById(R.id.etEditPhone);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvProfileName= findViewById(R.id.tvProfileName);
        btnSave      = findViewById(R.id.btnSaveProfile);
        btnLogout    = findViewById(R.id.btnLogout);

        ImageView btnBack     = findViewById(R.id.btnBack);
        ImageView btnSettings = findViewById(R.id.btnSettings);
        MaterialButton btnContinueLearning = findViewById(R.id.btnContinueLearning);

        tvLevelInfo         = findViewById(R.id.tvLevelInfo);
        tvCurrentLevel      = findViewById(R.id.tvCurrentLevel);
        tvNextLevel         = findViewById(R.id.tvNextLevel);
        tvXpText            = findViewById(R.id.tvXpText);
        tvStreakValue       = findViewById(R.id.tvStreakValue);
        tvCoursesCompleted  = findViewById(R.id.tvCoursesCompleted);
        tvTimeSpent         = findViewById(R.id.tvTimeSpent);
        tvRoadmapPercent    = findViewById(R.id.tvRoadmapPercent);
        tvRoadmapCount      = findViewById(R.id.tvRoadmapCount);
        tvOverallPercent    = findViewById(R.id.tvOverallPercent);
        tvSkillHtml         = findViewById(R.id.tvSkillHtml);
        tvSkillCss          = findViewById(R.id.tvSkillCss);
        tvSkillJs           = findViewById(R.id.tvSkillJs);
        tvSkillReact        = findViewById(R.id.tvSkillReact);
        tvRecent1           = findViewById(R.id.tvRecent1);
        tvRecent2           = findViewById(R.id.tvRecent2);
        tvRecent3           = findViewById(R.id.tvRecent3);
        pbXp                = findViewById(R.id.pbXp);
        pbRoadmap           = findViewById(R.id.pbRoadmap);

        // ── Toolbar buttons ───────────────────────────────────────────────────
        btnBack.setOnClickListener(v -> finish());
        btnSettings.setOnClickListener(v ->
                Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show());
        btnContinueLearning.setOnClickListener(v ->
                startActivity(new Intent(this, FrontendActivity.class)));

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
     * Computes and renders all profile dashboard statistics from SharedPreferences.
     * Uses ProgressCalculator for level booleans and ProgressPrefs for all keys.
     */
    private void loadDashboardStats() {
        SharedPreferences prefs = getSharedPreferences(ProgressPrefs.PREFS_NAME, MODE_PRIVATE);
        ProgressCalculator calc = new ProgressCalculator(prefs);

        // Raw per-topic counts (for granular XP display)
        int htmlDone  = calc.isHtmlComplete  ? 1 : 0;
        int cssDone   = calc.isCssComplete   ? 1 : 0;
        int jsDone    = calc.isJsComplete    ? 1 : 0;
        int vcDone    = calc.isVcComplete    ? 1 : 0;
        int vcsDone   = (prefs.getBoolean(ProgressPrefs.VCS_HOSTING_1, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.VCS_HOSTING_2, false) ? 1 : 0);
        int pmDone    = (prefs.getBoolean(ProgressPrefs.PM_1, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.PM_2, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.PM_3, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.PM_4, false) ? 1 : 0);
        int cssfDone  = calc.isCssfComplete  ? 1 : 0;
        int fwDone    = (prefs.getBoolean(ProgressPrefs.FRAMEWORK_1, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.FRAMEWORK_2, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.FRAMEWORK_3, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.FRAMEWORK_4, false) ? 1 : 0)
                      + (prefs.getBoolean(ProgressPrefs.FRAMEWORK_5, false) ? 1 : 0);

        int totalTopics     = 22;
        int completedTopics = calc.internetTopicsDone + htmlDone + cssDone + jsDone
                            + vcDone + vcsDone + pmDone + cssfDone + fwDone;
        int roadmapPercent  = (completedTopics * 100) / totalTopics;

        int level      = Math.max(1, calc.levelsCompleted + 1);
        int xpPercent  = roadmapPercent;

        pbXp.setProgress(xpPercent);
        pbRoadmap.setProgress(roadmapPercent);

        tvCurrentLevel.setText("Level " + level);
        tvNextLevel.setText("Level " + (level + 1));
        tvXpText.setText(completedTopics + " / " + totalTopics + " Topics");
        tvLevelInfo.setText("Level " + level + " Frontend Enthusiast");
        tvRoadmapPercent.setText(roadmapPercent + "%");
        tvRoadmapCount.setText(completedTopics + " of " + totalTopics + " topics completed");
        tvOverallPercent.setText("Overall Progress: " + roadmapPercent + "%");
        tvCoursesCompleted.setText(String.valueOf(calc.levelsCompleted));

        int streak        = prefs.getInt(ProgressPrefs.LEARNING_STREAK, 0);
        int weeklyMinutes = prefs.getInt(ProgressPrefs.TIME_SPENT_WEEK_MINUTES, 0);
        tvStreakValue.setText(String.valueOf(streak));
        tvTimeSpent.setText((weeklyMinutes / 60) + "h " + (weeklyMinutes % 60) + "m");

        tvSkillHtml.setText("HTML: "        + (htmlDone * 100)  + "%");
        tvSkillCss.setText("CSS: "          + (cssDone * 100)   + "%");
        tvSkillJs.setText("JavaScript: "    + (jsDone * 100)    + "%");
        tvSkillReact.setText("React: "
                + (prefs.getBoolean(ProgressPrefs.FRAMEWORK_1, false) ? 100 : 0) + "%");

        // Recent activity list (most recent first)
        List<String> recent = new ArrayList<>();
        if (prefs.getBoolean(ProgressPrefs.FRAMEWORK_5, false)) recent.add("Completed SolidJS");
        if (prefs.getBoolean(ProgressPrefs.FRAMEWORK_4, false)) recent.add("Completed Svelte");
        if (prefs.getBoolean(ProgressPrefs.FRAMEWORK_3, false)) recent.add("Completed Angular");
        if (prefs.getBoolean(ProgressPrefs.FRAMEWORK_2, false)) recent.add("Completed Vue.js");
        if (prefs.getBoolean(ProgressPrefs.FRAMEWORK_1, false)) recent.add("Completed React");
        if (prefs.getBoolean(ProgressPrefs.CSS_FRAMEWORKS, false)) recent.add("Completed CSS Frameworks");
        if (prefs.getBoolean(ProgressPrefs.PM_4, false)) recent.add("Completed Bun");
        if (prefs.getBoolean(ProgressPrefs.PM_3, false)) recent.add("Completed Yarn");
        if (prefs.getBoolean(ProgressPrefs.PM_2, false)) recent.add("Completed pnpm");
        if (prefs.getBoolean(ProgressPrefs.PM_1, false)) recent.add("Completed npm");
        if (prefs.getBoolean(ProgressPrefs.VCS_HOSTING_2, false)) recent.add("Completed GitHub");
        if (prefs.getBoolean(ProgressPrefs.VCS_HOSTING_1, false)) recent.add("Completed GitLab");
        if (prefs.getBoolean(ProgressPrefs.VERSION_CONTROL, false)) recent.add("Completed Version Control");
        if (prefs.getBoolean(ProgressPrefs.JS_COMPLETE, false)) recent.add("Completed JavaScript");
        if (prefs.getBoolean(ProgressPrefs.CSS_COMPLETE, false)) recent.add("Completed CSS");
        if (prefs.getBoolean(ProgressPrefs.HTML_COMPLETE, false)) recent.add("Completed HTML");

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
}