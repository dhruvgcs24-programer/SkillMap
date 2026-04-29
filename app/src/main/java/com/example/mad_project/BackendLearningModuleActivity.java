package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

/**
 * Single reusable Activity for ALL backend roadmap topic screens.
 *
 * Usage — call from BackendActivity (or anywhere):
 *
 *   Intent i = new Intent(this, BackendLearningModuleActivity.class);
 *   i.putExtra(BackendLearningModuleActivity.EXTRA_MODULE_KEY, "internet");
 *   startActivity(i);
 *
 * Supported moduleKey values (defined in BackendModule):
 *   "internet" | "frontend_basic" | "pick_language" | "vcs" |
 *   "relational_db" | "apis" | "caching" | "web_servers"
 */
public class BackendLearningModuleActivity extends AppCompatActivity {

    public static final String EXTRA_MODULE_KEY = "backend_module_key";

    // ── Views ─────────────────────────────────────────────────────────────────
    private TextView     tvTitle, tvSubtitle, tvProgressPct, tvCompleted;
    private ProgressBar  progressBar;
    private CheckBox     cbMarkAll;
    private TextView     tvOverviewTitle, tvOverviewBody;
    private LinearLayout topicsContainer;

    // ── State ─────────────────────────────────────────────────────────────────
    private BackendModule        module;
    private SharedPreferences    prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_learning_module);

        // Resolve module from Intent
        String key = getIntent().getStringExtra(EXTRA_MODULE_KEY);
        if (key == null) { finish(); return; }
        module = BackendModule.get(key);
        prefs  = getSharedPreferences("backend_progress", MODE_PRIVATE);

        bindViews();
        setupToolbar();
        populateHeader();
        populateOverview();
        populateTopics();
        refreshProgress();
        setupMarkAll();
    }

    // ── Binding ───────────────────────────────────────────────────────────────

    private void bindViews() {
        tvTitle          = findViewById(R.id.tvBLMTitle);
        tvSubtitle       = findViewById(R.id.tvBLMSubtitle);
        tvProgressPct    = findViewById(R.id.tvBLMProgressPct);
        tvCompleted      = findViewById(R.id.tvBLMCompleted);
        progressBar      = findViewById(R.id.blmProgressBar);
        cbMarkAll        = findViewById(R.id.cbBLMMarkAll);
        tvOverviewTitle  = findViewById(R.id.tvBLMOverviewTitle);
        tvOverviewBody   = findViewById(R.id.tvBLMOverviewBody);
        topicsContainer  = findViewById(R.id.blmTopicsContainer);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.blmToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void populateHeader() {
        tvTitle.setText(module.title);
        tvSubtitle.setText(module.subtitle);
    }

    private void populateOverview() {
        tvOverviewTitle.setText(module.overviewTitle);
        tvOverviewBody.setText(module.overviewBody);
    }

    // ── Topics ────────────────────────────────────────────────────────────────

    private void populateTopics() {
        topicsContainer.removeAllViews();
        for (int i = 0; i < module.subTopics.size(); i++) {
            BackendModule.SubTopic st = module.subTopics.get(i);
            View card = buildTopicCard(st, i);
            topicsContainer.addView(card);
        }
    }

    private View buildTopicCard(BackendModule.SubTopic st, int index) {
        View card = getLayoutInflater().inflate(
                R.layout.item_backend_learning_topic, topicsContainer, false);

        TextView  tvNum    = card.findViewById(R.id.tvBLMTopicNum);
        TextView  tvTitle  = card.findViewById(R.id.tvBLMTopicTitle);
        TextView  tvDesc   = card.findViewById(R.id.tvBLMTopicDesc);
        CheckBox  cb       = card.findViewById(R.id.cbBLMTopicDone);
        LinearLayout resCont = card.findViewById(R.id.blmResContainer);

        tvNum.setText("Topic " + st.number);
        tvTitle.setText(st.title);
        tvDesc.setText(st.description);

        // Individual checkbox
        cb.setChecked(prefs.getBoolean(st.prefKey, false));
        cb.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean(st.prefKey, checked).apply();
            refreshProgress();
            syncMarkAll();
        });

        // Resource rows
        for (BackendModule.Resource res : st.resources) {
            View row = buildResourceRow(res);
            resCont.addView(row);
        }

        return card;
    }

    private View buildResourceRow(BackendModule.Resource res) {
        View row = getLayoutInflater().inflate(
                R.layout.item_backend_resource_row, topicsContainer, false);

        TextView tvType = row.findViewById(R.id.tvBLMResType);
        TextView tvName = row.findViewById(R.id.tvBLMResName);

        tvType.setText(res.type);
        tvName.setText(res.label);

        // Badge tint matching your existing Course/Official/Video colours
        switch (res.type) {
            case "Course":
                tvType.setBackgroundResource(R.drawable.bg_badge_course);
                break;
            case "Video":
                tvType.setBackgroundResource(R.drawable.bg_badge_video);
                break;
            default: // "Official"
                tvType.setBackgroundResource(R.drawable.bg_badge_official);
                break;
        }

        row.setOnClickListener(v -> openUrl(res.url));
        tvName.setOnClickListener(v -> openUrl(res.url));
        return row;
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    // ── Progress ──────────────────────────────────────────────────────────────

    private void refreshProgress() {
        int total = module.subTopics.size();
        int done  = 0;
        for (BackendModule.SubTopic st : module.subTopics) {
            if (prefs.getBoolean(st.prefKey, false)) done++;
        }
        int pct = total == 0 ? 0 : (int) ((done / (float) total) * 100);

        progressBar.setProgress(pct);
        tvProgressPct.setText("Progress: " + pct + "%");
        tvCompleted.setText(done + "/" + total + " Completed");

        // Write section-done flag (used by BackendActivity path screen)
        prefs.edit().putBoolean(module.prefKey, done == total && total > 0).apply();
    }

    // ── Mark All ──────────────────────────────────────────────────────────────

    private void setupMarkAll() {
        syncMarkAll(); // set initial state without firing listener
        cbMarkAll.setOnCheckedChangeListener((btn, checked) -> {
            SharedPreferences.Editor ed = prefs.edit();
            for (BackendModule.SubTopic st : module.subTopics)
                ed.putBoolean(st.prefKey, checked);
            ed.putBoolean(module.prefKey, checked);
            ed.apply();
            refreshAllTopicCheckboxes(checked);
            refreshProgress();
        });
    }

    private void syncMarkAll() {
        int total = module.subTopics.size();
        int done  = 0;
        for (BackendModule.SubTopic st : module.subTopics)
            if (prefs.getBoolean(st.prefKey, false)) done++;

        // Detach listener before programmatic change to avoid infinite loop
        cbMarkAll.setOnCheckedChangeListener(null);
        cbMarkAll.setChecked(total > 0 && done == total);
        cbMarkAll.setOnCheckedChangeListener((btn, checked) -> {
            SharedPreferences.Editor ed = prefs.edit();
            for (BackendModule.SubTopic st : module.subTopics)
                ed.putBoolean(st.prefKey, checked);
            ed.putBoolean(module.prefKey, checked);
            ed.apply();
            refreshAllTopicCheckboxes(checked);
            refreshProgress();
        });
    }

    private void refreshAllTopicCheckboxes(boolean checked) {
        for (int i = 0; i < topicsContainer.getChildCount(); i++) {
            View card = topicsContainer.getChildAt(i);
            CheckBox cb = card.findViewById(R.id.cbBLMTopicDone);
            if (cb == null) continue;
            cb.setOnCheckedChangeListener(null);
            cb.setChecked(checked);
            final int idx = i;
            cb.setOnCheckedChangeListener((btn, c) -> {
                prefs.edit().putBoolean(module.subTopics.get(idx).prefKey, c).apply();
                refreshProgress();
                syncMarkAll();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProgress();
        syncMarkAll();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
