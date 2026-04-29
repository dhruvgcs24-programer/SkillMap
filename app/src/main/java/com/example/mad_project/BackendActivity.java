package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BackendActivity extends AppCompatActivity {

    private static final int TOTAL = 9;

    // Progress header
    private ProgressBar progressBar;
    private TextView    tvProgressPct, tvLevels;

    // Node background views (circle_gray / circle_yellow / circle_success)
    private View[] nodeBg  = new View[TOTAL];
    // Number labels inside grey/yellow nodes
    private TextView[] nodeNum = new TextView[TOTAL];
    // Checkmark icons inside green nodes
    private ImageView[] nodeCheck = new ImageView[TOTAL];
    // Whole node layout (clickable)
    private View[] nodeRoot = new View[TOTAL];

    // Maps index → module key for BackendModule / SharedPreferences
    private static final String[] MODULE_KEYS = {
        "internet", "frontend_basic", "pick_language", "vcs",
        "relational_db", "apis", "caching", "web_servers"
        // index 8 intentionally blank — "web_servers" is the last content node
        // Add a 9th key if you add a 9th module later
    };

    // Maps index → SharedPreferences "done" key (must match BackendModule.prefKey values)
    private static final String[] PREF_KEYS = {
        "be_internet_done",  "be_fe_basics_done", "be_lang_done",  "be_vcs_done",
        "be_reldb_done",     "be_apis_done",       "be_caching_done","be_webserver_done",
        ""
    };

    // Node IDs in layout (left-right alternating, 9 nodes)
    private static final int[] NODE_ROOT_IDS = {
        R.id.beNodeRoot0, R.id.beNodeRoot1, R.id.beNodeRoot2, R.id.beNodeRoot3,
        R.id.beNodeRoot4, R.id.beNodeRoot5, R.id.beNodeRoot6, R.id.beNodeRoot7,
        R.id.beNodeRoot8
    };


    // Node labels shown beneath each circle
    private static final String[] NODE_LABELS = {
        "Internet", "Frontend Basics", "Pick a Language", "Version Control",
        "Relational DB", "APIs", "Caching", "Web Servers", "Coming Soon"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend);

        Toolbar toolbar = findViewById(R.id.beToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        progressBar   = findViewById(R.id.beProgressBar);
        tvProgressPct = findViewById(R.id.beProgressPct);
        tvLevels      = findViewById(R.id.beLevels);

        for (int i = 0; i < TOTAL; i++) {
            nodeRoot[i]  = findViewById(NODE_ROOT_IDS[i]);
            nodeBg[i]    = nodeRoot[i].findViewById(R.id.beNodeBg0);
            nodeNum[i]   = nodeRoot[i].findViewById(R.id.beNodeNum0);
            nodeCheck[i] = nodeRoot[i].findViewById(R.id.beNodeCheck0);
            
            TextView label = nodeRoot[i].findViewById(R.id.beNodeLabel0);
            if (label != null) {
                label.setText(NODE_LABELS[i]);
            }
            if (i < 8) {
                nodeNum[i].setText(String.valueOf(i + 1));
            } else {
                nodeNum[i].setText("");
            }
        }

        setClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNodes();
        refreshProgress();
    }

    // ── Node click listeners ──────────────────────────────────────────────────

    private void setClickListeners() {
        for (int i = 0; i < TOTAL - 1; i++) { // last node is "Coming Soon"
            final int idx = i;
            nodeRoot[i].setOnClickListener(v -> {
                Intent intent = new Intent(this, BackendLearningModuleActivity.class);
                intent.putExtra(BackendLearningModuleActivity.EXTRA_MODULE_KEY, MODULE_KEYS[idx]);
                startActivity(intent);
            });
        }
        // Last node — no action yet
        nodeRoot[TOTAL - 1].setOnClickListener(v -> { /* Coming Soon */ });
    }

    // ── Progress ──────────────────────────────────────────────────────────────

    private void refreshProgress() {
        SharedPreferences prefs = getSharedPreferences("backend_progress", MODE_PRIVATE);
        int done = 0;
        for (int i = 0; i < TOTAL - 1; i++) { // exclude "Coming Soon"
            if (!PREF_KEYS[i].isEmpty() && prefs.getBoolean(PREF_KEYS[i], false)) done++;
        }
        int usableTotal = TOTAL - 1;
        int pct = (int) ((done / (float) usableTotal) * 100);
        progressBar.setProgress(pct);
        tvProgressPct.setText("Roadmap Progress: " + pct + "%");
        tvLevels.setText(done + "/" + usableTotal + " Levels Completed");
    }

    // ── Node state (green ✓ / yellow active / grey locked) ───────────────────

    private void refreshNodes() {
        SharedPreferences prefs = getSharedPreferences("backend_progress", MODE_PRIVATE);
        boolean foundActive = false;

        for (int i = 0; i < TOTAL; i++) {
            boolean done = !PREF_KEYS[i].isEmpty() && prefs.getBoolean(PREF_KEYS[i], false);

            if (done) {
                // Green with checkmark
                nodeBg[i].setBackgroundResource(R.drawable.circle_success);
                nodeNum[i].setVisibility(View.GONE);
                nodeCheck[i].setVisibility(View.VISIBLE);
            } else if (!foundActive && i < TOTAL - 1) {
                // First incomplete content node = ACTIVE (yellow)
                foundActive = true;
                nodeBg[i].setBackgroundResource(R.drawable.circle_yellow);
                nodeNum[i].setVisibility(View.VISIBLE);
                nodeCheck[i].setVisibility(View.GONE);
            } else {
                // Locked (grey)
                nodeBg[i].setBackgroundResource(R.drawable.circle_gray);
                nodeNum[i].setVisibility(View.VISIBLE);
                nodeCheck[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
