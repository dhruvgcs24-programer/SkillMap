package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

public class InternetActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private ProgressBar progressBar;
    private TextView progressText, progressCount;
    private ScrollView mainScrollView;
    private CheckBox checkMarkAll;
    private List<CheckBox> topicCheckBoxes = new ArrayList<>();
    private boolean isUpdatingFromMarkAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        progressCount = findViewById(R.id.progressCount);
        mainScrollView = findViewById(R.id.mainScrollView);
        checkMarkAll = findViewById(R.id.checkMarkAll);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        setup(R.id.topic1, R.id.check1, "t1", "How does the internet work?", R.id.expandable1);
        setup(R.id.topic2, R.id.check2, "t2", "What is HTTP?", R.id.expandable2);
        setup(R.id.topic3, R.id.check3, "t3", "What is Domain Name?", R.id.expandable3);
        setup(R.id.topic4, R.id.check4, "t4", "What is Hosting?", R.id.expandable4);
        setup(R.id.topic5, R.id.check5, "t5", "DNS and how it works?", R.id.expandable5);
        setup(R.id.topic6, R.id.check6, "t6", "Browsers and how they work?", R.id.expandable6);

        // Mark All Logic
        checkMarkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingFromMarkAll) return;
            isUpdatingFromMarkAll = true;
            for (int i = 0; i < topicCheckBoxes.size(); i++) {
                CheckBox cb = topicCheckBoxes.get(i);
                cb.setChecked(isChecked);
                prefs.edit().putBoolean("t" + (i + 1), isChecked).apply();
            }
            updateProgress();
            isUpdatingFromMarkAll = false;
        });

        findViewById(R.id.btnMarkAll).setOnClickListener(v -> checkMarkAll.toggle());

        // Topic 1 links
        findViewById(R.id.linkArticle).setOnClickListener(v -> openUrl("https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/How_does_the_Internet_work"));
        findViewById(R.id.linkVideo).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=7_LPdttKXPc"));

        // Topic 2 links
        findViewById(R.id.linkHttp1).setOnClickListener(v -> openUrl("https://www.cloudflare.com/en-gb/learning/ddos/glossary/hypertext-transfer-protocol-http/"));
        findViewById(R.id.linkHttp2).setOnClickListener(v -> openUrl("https://howhttps.works/"));
        findViewById(R.id.linkHttp3).setOnClickListener(v -> openUrl("https://thenewstack.io/http-3-is-now-a-standard-why-use-it-and-how-to-get-started/"));

        // Topic 3 links
        findViewById(R.id.linkDomain1).setOnClickListener(v -> openUrl("https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/What_is_a_domain_name"));
        findViewById(R.id.linkDomain2).setOnClickListener(v -> openUrl("https://www.cloudflare.com/en-gb/learning/dns/glossary/what-is-a-domain-name/"));
        findViewById(R.id.linkDomainVideo).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=lMHzpBwPuG8"));

        // Topic 4 links
        findViewById(R.id.linkHosting1).setOnClickListener(v -> openUrl("https://www.namecheap.com/guru-guides/what-is-web-hosting/"));
        findViewById(R.id.linkHostingVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=AXVZYzw8geg"));
        findViewById(R.id.linkHostingVideo2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=Kx_1NYYJS7Q"));

        // Topic 5 links
        findViewById(R.id.linkDns1).setOnClickListener(v -> openUrl("https://www.cloudflare.com/en-gb/learning/dns/what-is-dns/"));
        findViewById(R.id.linkDns2).setOnClickListener(v -> openUrl("http://messwithdns.net/"));
        findViewById(R.id.linkDns3).setOnClickListener(v -> openUrl("https://howdns.works/"));
        findViewById(R.id.linkDnsVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=Wj0od2ag5sk"));

        // Topic 6 links
        findViewById(R.id.linkBrowser1).setOnClickListener(v -> openUrl("https://www.ramotion.com/blog/what-is-web-browser/"));
        findViewById(R.id.linkBrowser2).setOnClickListener(v -> openUrl("https://developer.mozilla.org/en-US/docs/Web/Performance/Guides/How_browsers_work"));
        findViewById(R.id.linkBrowserVideo).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=5rLFYtXHo9s"));

        updateProgress();
        syncMarkAllState();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void toggleTheme() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }

    private void setup(int topicId, int checkId, String key, String topicName, int expandableId) {
        LinearLayout topic = findViewById(topicId);
        CheckBox checkBox = findViewById(checkId);
        topicCheckBoxes.add(checkBox);

        boolean done = prefs.getBoolean(key, false);
        checkBox.setChecked(done);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isUpdatingFromMarkAll) {
                prefs.edit().putBoolean(key, isChecked).apply();
                updateProgress();
                syncMarkAllState();
            }
        });

        topic.setOnClickListener(v -> {
            if (expandableId != 0) {
                View expandable = findViewById(expandableId);
                if (expandable.getVisibility() == View.GONE) {
                    expandable.setVisibility(View.VISIBLE);
                    mainScrollView.post(() -> {
                        mainScrollView.smoothScrollTo(0, topic.getTop());
                    });
                } else {
                    expandable.setVisibility(View.GONE);
                }
            } else {
                checkBox.toggle();
            }
        });
    }

    private void syncMarkAllState() {
        boolean allChecked = true;
        for (CheckBox cb : topicCheckBoxes) {
            if (!cb.isChecked()) {
                allChecked = false;
                break;
            }
        }
        isUpdatingFromMarkAll = true;
        checkMarkAll.setChecked(allChecked);
        isUpdatingFromMarkAll = false;
    }

    private void updateProgress() {
        int count = 0;
        for (int i = 1; i <= 6; i++) {
            if (prefs.getBoolean("t" + i, false)) count++;
        }

        progressBar.setProgress(count);
        progressCount.setText(count + "/6 Completed");
        int percent = (int) ((count / 6.0) * 100);
        progressText.setText("Progress: " + percent + "%");
    }
}