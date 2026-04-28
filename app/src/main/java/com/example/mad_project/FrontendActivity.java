package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FrontendActivity extends AppCompatActivity {

    private LinearLayout lvl1, lvl2, lvl3, lvl4, lvl5, lvl6, lvl7, lvl8, lvl9;
    private TextView txtLvl1, txtLvl2, txtLvl3, txtLvl4, txtLvl5, txtLvl6, txtLvl7, txtLvl8, txtLvl9;
    private ImageView imgLvl1, imgLvl2, imgLvl3, imgLvl4, imgLvl5, imgLvl6, imgLvl7, imgLvl8, imgLvl9;
    private ImageView tickLvl1, tickLvl2, tickLvl3, tickLvl4, tickLvl5, tickLvl6, tickLvl7, tickLvl8, tickLvl9;
    private CardView lockBadge2, activeBadge2, lockBadge3, activeBadge3, lockBadge4, activeBadge4,
            lockBadge5, activeBadge5, lockBadge6, activeBadge6, lockBadge7, activeBadge7,
            lockBadge8, activeBadge8, lockBadge9, activeBadge9;
    private DashedPathView dashedPathView;

    // Level unlock flags (set from ProgressCalculator in updateUI)
    private boolean isLevel2Unlocked = false;
    private boolean isLevel3Unlocked = false;
    private boolean isLevel4Unlocked = false;
    private boolean isLevel5Unlocked = false;
    private boolean isLevel6Unlocked = false;
    private boolean isLevel7Unlocked = false;
    private boolean isLevel8Unlocked = false;
    private boolean isLevel9Unlocked = false;

    private ProgressBar overallProgressBar;
    private TextView overallProgressText, overallCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontend);

        dashedPathView = findViewById(R.id.dashedPath);

        // ── Level containers ──────────────────────────────────────────────────
        lvl1 = findViewById(R.id.lvl1); lvl2 = findViewById(R.id.lvl2);
        lvl3 = findViewById(R.id.lvl3); lvl4 = findViewById(R.id.lvl4);
        lvl5 = findViewById(R.id.lvl5); lvl6 = findViewById(R.id.lvl6);
        lvl7 = findViewById(R.id.lvl7); lvl8 = findViewById(R.id.lvl8);
        lvl9 = findViewById(R.id.lvl9);

        // ── Circle images ─────────────────────────────────────────────────────
        imgLvl1 = findViewById(R.id.imgLvl1); imgLvl2 = findViewById(R.id.imgLvl2);
        imgLvl3 = findViewById(R.id.imgLvl3); imgLvl4 = findViewById(R.id.imgLvl4);
        imgLvl5 = findViewById(R.id.imgLvl5); imgLvl6 = findViewById(R.id.imgLvl6);
        imgLvl7 = findViewById(R.id.imgLvl7); imgLvl8 = findViewById(R.id.imgLvl8);
        imgLvl9 = findViewById(R.id.imgLvl9);

        // ── Labels ────────────────────────────────────────────────────────────
        txtLvl1 = findViewById(R.id.txtLvl1); txtLvl2 = findViewById(R.id.txtLvl2);
        txtLvl3 = findViewById(R.id.txtLvl3); txtLvl4 = findViewById(R.id.txtLvl4);
        txtLvl5 = findViewById(R.id.txtLvl5); txtLvl6 = findViewById(R.id.txtLvl6);
        txtLvl7 = findViewById(R.id.txtLvl7); txtLvl8 = findViewById(R.id.txtLvl8);
        txtLvl9 = findViewById(R.id.txtLvl9);

        // ── Tick icons ────────────────────────────────────────────────────────
        tickLvl1 = findViewById(R.id.tickLvl1); tickLvl2 = findViewById(R.id.tickLvl2);
        tickLvl3 = findViewById(R.id.tickLvl3); tickLvl4 = findViewById(R.id.tickLvl4);
        tickLvl5 = findViewById(R.id.tickLvl5); tickLvl6 = findViewById(R.id.tickLvl6);
        tickLvl7 = findViewById(R.id.tickLvl7); tickLvl8 = findViewById(R.id.tickLvl8);
        tickLvl9 = findViewById(R.id.tickLvl9);

        // ── Lock / active badges ──────────────────────────────────────────────
        lockBadge2 = findViewById(R.id.lockBadge2);   activeBadge2 = findViewById(R.id.activeBadge2);
        lockBadge3 = findViewById(R.id.lockBadge3);   activeBadge3 = findViewById(R.id.activeBadge3);
        lockBadge4 = findViewById(R.id.lockBadge4);   activeBadge4 = findViewById(R.id.activeBadge4);
        lockBadge5 = findViewById(R.id.lockBadge5);   activeBadge5 = findViewById(R.id.activeBadge5);
        lockBadge6 = findViewById(R.id.lockBadge6);   activeBadge6 = findViewById(R.id.activeBadge6);
        lockBadge7 = findViewById(R.id.lockBadge7);   activeBadge7 = findViewById(R.id.activeBadge7);
        lockBadge8 = findViewById(R.id.lockBadge8);   activeBadge8 = findViewById(R.id.activeBadge8);
        lockBadge9 = findViewById(R.id.lockBadge9);   activeBadge9 = findViewById(R.id.activeBadge9);

        // ── Progress bar ──────────────────────────────────────────────────────
        overallProgressBar  = findViewById(R.id.overallProgressBar);
        overallProgressText = findViewById(R.id.overallProgressText);
        overallCountText    = findViewById(R.id.overallCountText);

        // ── Back button ───────────────────────────────────────────────────────
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // ── Level click listeners ─────────────────────────────────────────────
        lvl1.setOnClickListener(v -> openModule(LearningModuleActivity.MODE_INTERNET, null));

        lvl2.setOnClickListener(v -> {
            if (isLevel2Unlocked) openModule(LearningModuleActivity.MODE_HTML, null);
            else Toast.makeText(this, "Complete all Internet topics first", Toast.LENGTH_SHORT).show();
        });
        lvl3.setOnClickListener(v -> {
            if (isLevel3Unlocked) openModule(LearningModuleActivity.MODE_CSS, null);
            else Toast.makeText(this, "Complete HTML level first", Toast.LENGTH_SHORT).show();
        });
        lvl4.setOnClickListener(v -> {
            if (isLevel4Unlocked) openModule(LearningModuleActivity.MODE_JAVASCRIPT, null);
            else Toast.makeText(this, "Complete CSS level first", Toast.LENGTH_SHORT).show();
        });
        lvl5.setOnClickListener(v -> {
            if (isLevel5Unlocked) openModule(LearningModuleActivity.MODE_VERSION_CONTROL, null);
            else Toast.makeText(this, "Complete JavaScript level first", Toast.LENGTH_SHORT).show();
        });
        lvl6.setOnClickListener(v -> {
            if (isLevel6Unlocked) openModule(LearningModuleActivity.MODE_VCS_HOSTING, null);
            else Toast.makeText(this, "Complete Version Control level first", Toast.LENGTH_SHORT).show();
        });
        lvl7.setOnClickListener(v -> {
            if (isLevel7Unlocked) openModule(LearningModuleActivity.MODE_PACKAGE_MANAGERS, null);
            else Toast.makeText(this, "Complete VCS Hosting level first", Toast.LENGTH_SHORT).show();
        });
        lvl8.setOnClickListener(v -> {
            if (isLevel8Unlocked) openModule(LearningModuleActivity.MODE_CSS_FRAMEWORKS, null);
            else Toast.makeText(this, "Complete Package Managers level first", Toast.LENGTH_SHORT).show();
        });
        lvl9.setOnClickListener(v -> {
            if (isLevel9Unlocked) openModule(LearningModuleActivity.MODE_LEARN_FRAMEWORK, null);
            else Toast.makeText(this, "Complete CSS Frameworks level first", Toast.LENGTH_SHORT).show();
        });

        // Draw connecting dashed path once layout is ready
        lvl1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setupDashedPaths();
                lvl1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /** Helper to launch LearningModuleActivity for a given mode. */
    private void openModule(String mode, String ignored) {
        Intent intent = new Intent(this, LearningModuleActivity.class);
        intent.putExtra(LearningModuleActivity.EXTRA_MODE, mode);
        startActivity(intent);
    }

    private void setupDashedPaths() {
        dashedPathView.clearPoints();
        addPoint(lvl1); addPoint(lvl2); addPoint(lvl3);
        addPoint(lvl4); addPoint(lvl5); addPoint(lvl6);
        addPoint(lvl7); addPoint(lvl8); addPoint(lvl9);
    }

    private void addPoint(View view) {
        float x = view.getX() + (view.getWidth() / 2f);
        float y = view.getY() + (view.getHeight() / 2f);
        dashedPathView.addPathPoint(x, y);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Reads progress from SharedPreferences via ProgressCalculator and
     * updates every level's visual state (locked / active / complete).
     */
    private void updateUI() {
        SharedPreferences prefs = getSharedPreferences(ProgressPrefs.PREFS_NAME, MODE_PRIVATE);
        ProgressCalculator calc = new ProgressCalculator(prefs);

        // ── Sync unlock flags ─────────────────────────────────────────────────
        isLevel2Unlocked = calc.isInternetComplete;
        isLevel3Unlocked = calc.isHtmlComplete;
        isLevel4Unlocked = calc.isCssComplete;
        isLevel5Unlocked = calc.isJsComplete;
        isLevel6Unlocked = calc.isVcComplete;
        isLevel7Unlocked = calc.isVcsComplete;
        isLevel8Unlocked = calc.isPmComplete;
        isLevel9Unlocked = calc.isCssfComplete;

        // ── Progress bar ──────────────────────────────────────────────────────
        overallProgressBar.setProgress(calc.levelsCompleted);
        overallCountText.setText(calc.levelsCompleted + "/9 Levels Completed");
        overallProgressText.setText("Roadmap Progress: "
                + (int) ((calc.levelsCompleted / 9.0) * 100) + "%");

        // ── Level 1 — Internet ────────────────────────────────────────────────
        if (calc.isInternetComplete) {
            imgLvl1.setImageResource(R.drawable.circle_success);
            txtLvl1.setVisibility(View.GONE);
            tickLvl1.setVisibility(View.VISIBLE);
        } else {
            imgLvl1.setImageResource(R.drawable.circle_purple);
            txtLvl1.setVisibility(View.VISIBLE);
            tickLvl1.setVisibility(View.GONE);
        }

        // ── Levels 2–9: locked / active / complete ────────────────────────────
        applyLevelState(calc.isInternetComplete, calc.isHtmlComplete,
                lockBadge2, activeBadge2, imgLvl2, txtLvl2, tickLvl2);
        applyLevelState(calc.isHtmlComplete, calc.isCssComplete,
                lockBadge3, activeBadge3, imgLvl3, txtLvl3, tickLvl3);
        applyLevelState(calc.isCssComplete, calc.isJsComplete,
                lockBadge4, activeBadge4, imgLvl4, txtLvl4, tickLvl4);
        applyLevelState(calc.isJsComplete, calc.isVcComplete,
                lockBadge5, activeBadge5, imgLvl5, txtLvl5, tickLvl5);
        applyLevelState(calc.isVcComplete, calc.isVcsComplete,
                lockBadge6, activeBadge6, imgLvl6, txtLvl6, tickLvl6);
        applyLevelState(calc.isVcsComplete, calc.isPmComplete,
                lockBadge7, activeBadge7, imgLvl7, txtLvl7, tickLvl7);
        applyLevelState(calc.isPmComplete, calc.isCssfComplete,
                lockBadge8, activeBadge8, imgLvl8, txtLvl8, tickLvl8);
        applyLevelState(calc.isCssfComplete, calc.isFwComplete,
                lockBadge9, activeBadge9, imgLvl9, txtLvl9, tickLvl9);

        // ── Congrats dialog (shown once on 100% completion) ───────────────────
        if (calc.levelsCompleted == 9
                && !prefs.getBoolean(ProgressPrefs.FRONTEND_CONGRATS_SHOWN, false)) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Congratulations! \uD83C\uDF89")
                    .setMessage("You have successfully completed the entire Frontend Development Roadmap!")
                    .setPositiveButton("Awesome!", (dialog, which) ->
                            prefs.edit().putBoolean(ProgressPrefs.FRONTEND_CONGRATS_SHOWN, true).apply())
                    .show();
        }
    }

    /**
     * Applies the correct visual state to a single roadmap level node.
     *
     * @param prerequisiteDone Whether the previous level is complete (unlocks this level)
     * @param thisLevelDone    Whether this level itself is complete
     * @param lockBadge        CardView shown when locked
     * @param activeBadge      CardView shown when unlocked but not complete
     * @param img              Circle image for this level
     * @param label            Text label for this level
     * @param tick             Tick/checkmark icon shown on completion
     */
    private void applyLevelState(boolean prerequisiteDone, boolean thisLevelDone,
                                 CardView lockBadge, CardView activeBadge,
                                 ImageView img, TextView label, ImageView tick) {
        if (prerequisiteDone) {
            lockBadge.setVisibility(View.GONE);
            img.setAlpha(1.0f);
            if (thisLevelDone) {
                activeBadge.setVisibility(View.GONE);
                img.setImageResource(R.drawable.circle_success);
                label.setVisibility(View.GONE);
                if (tick != null) tick.setVisibility(View.VISIBLE);
            } else {
                activeBadge.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.circle_yellow);
                label.setVisibility(View.VISIBLE);
                if (tick != null) tick.setVisibility(View.GONE);
            }
        } else {
            lockBadge.setVisibility(View.VISIBLE);
            activeBadge.setVisibility(View.GONE);
            img.setAlpha(0.5f);
        }
    }
}