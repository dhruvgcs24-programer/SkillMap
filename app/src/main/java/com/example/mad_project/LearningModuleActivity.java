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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

public class LearningModuleActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "module_mode";
    public static final String MODE_INTERNET = "internet";
    public static final String MODE_HTML = "html";
    public static final String MODE_CSS = "css";
    public static final String MODE_JAVASCRIPT = "javascript";
    public static final String MODE_VERSION_CONTROL = "version_control";
    public static final String MODE_VCS_HOSTING = "vcs_hosting";
    public static final String MODE_PACKAGE_MANAGERS = "package_managers";
    public static final String MODE_CSS_FRAMEWORKS = "css_frameworks";
    public static final String MODE_LEARN_FRAMEWORK = "learn_framework";

    private SharedPreferences prefs;
    private ProgressBar progressBar;
    private TextView progressText;
    private TextView progressCount;
    private ScrollView mainScrollView;
    private CheckBox checkMarkAll;
    private final List<CheckBox> topicCheckBoxes = new ArrayList<>();
    private boolean isUpdatingFromMarkAll = false;
    private ModuleConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mode = getIntent().getStringExtra(EXTRA_MODE);
        if (mode == null) {
            finish();
            return;
        }

        config = getConfig(mode);
        if (config == null) {
            finish();
            return;
        }

        setContentView(config.layoutId);
        prefs = getSharedPreferences("progress", MODE_PRIVATE);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        if (config.moduleType == ModuleType.SINGLE) {
            setupSingleModule(config.singleConfig);
        } else {
            setupChecklistModule(config.checklistConfig);
        }

        setupLinks(mode);
    }

    private void setupSingleModule(SingleConfig singleConfig) {
        CheckBox completionCheck = findViewById(singleConfig.checkBoxId);
        completionCheck.setChecked(prefs.getBoolean(singleConfig.progressKey, false));
        completionCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(singleConfig.progressKey, isChecked).apply();
            if (isChecked) {
                Toast.makeText(this, singleConfig.completedToast, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(singleConfig.completeButtonId).setOnClickListener(v -> completionCheck.toggle());
    }

    private void setupChecklistModule(ChecklistConfig checklistConfig) {
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        progressCount = findViewById(R.id.progressCount);
        mainScrollView = findViewById(R.id.mainScrollView);
        checkMarkAll = findViewById(R.id.checkMarkAll);

        for (TopicConfig topicConfig : checklistConfig.topics) {
            setupTopic(topicConfig);
        }

        checkMarkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingFromMarkAll) {
                return;
            }
            isUpdatingFromMarkAll = true;
            for (int i = 0; i < topicCheckBoxes.size(); i++) {
                topicCheckBoxes.get(i).setChecked(isChecked);
                prefs.edit().putBoolean(checklistConfig.topics[i].progressKey, isChecked).apply();
            }
            updateChecklistProgress(checklistConfig);
            isUpdatingFromMarkAll = false;
        });

        findViewById(R.id.btnMarkAll).setOnClickListener(v -> checkMarkAll.toggle());
        updateChecklistProgress(checklistConfig);
        syncMarkAllState();
    }

    private void setupTopic(TopicConfig topicConfig) {
        LinearLayout topic = findViewById(topicConfig.topicId);
        CheckBox checkBox = findViewById(topicConfig.checkId);
        topicCheckBoxes.add(checkBox);

        checkBox.setChecked(prefs.getBoolean(topicConfig.progressKey, false));
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isUpdatingFromMarkAll) {
                prefs.edit().putBoolean(topicConfig.progressKey, isChecked).apply();
                updateChecklistProgress(config.checklistConfig);
                syncMarkAllState();
            }
        });

        topic.setOnClickListener(v -> {
            if (topicConfig.expandableId != 0) {
                View expandable = findViewById(topicConfig.expandableId);
                if (expandable.getVisibility() == View.GONE) {
                    expandable.setVisibility(View.VISIBLE);
                    mainScrollView.post(() -> mainScrollView.smoothScrollTo(0, topic.getTop()));
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

    private void updateChecklistProgress(ChecklistConfig checklistConfig) {
        int count = 0;
        for (TopicConfig topic : checklistConfig.topics) {
            if (prefs.getBoolean(topic.progressKey, false)) {
                count++;
            }
        }
        int total = checklistConfig.topics.length;
        progressBar.setProgress(count);
        progressCount.setText(count + "/" + total + " Completed");
        int percent = (int) ((count / (double) total) * 100);
        progressText.setText("Progress: " + percent + "%");
    }

    private ModuleConfig getConfig(String mode) {
        if (MODE_HTML.equals(mode)) {
            return ModuleConfig.single(
                    R.layout.activity_html,
                    new SingleConfig(R.id.checkHtmlComplete, R.id.btnCompleteHtml, "h_complete", "HTML level marked as completed!")
            );
        }
        if (MODE_CSS.equals(mode)) {
            return ModuleConfig.single(
                    R.layout.activity_css,
                    new SingleConfig(R.id.checkCssComplete, R.id.btnCompleteCss, "c_complete", "CSS level marked as completed!")
            );
        }
        if (MODE_JAVASCRIPT.equals(mode)) {
            return ModuleConfig.single(
                    R.layout.activity_javascript,
                    new SingleConfig(R.id.checkJavascriptComplete, R.id.btnCompleteJavascript, "j_complete", "JavaScript level marked as completed!")
            );
        }
        if (MODE_INTERNET.equals(mode)) {
            return ModuleConfig.checklist(
                    R.layout.activity_internet,
                    new ChecklistConfig(new TopicConfig[]{
                            new TopicConfig(R.id.topic1, R.id.check1, "t1", R.id.expandable1),
                            new TopicConfig(R.id.topic2, R.id.check2, "t2", R.id.expandable2),
                            new TopicConfig(R.id.topic3, R.id.check3, "t3", R.id.expandable3),
                            new TopicConfig(R.id.topic4, R.id.check4, "t4", R.id.expandable4),
                            new TopicConfig(R.id.topic5, R.id.check5, "t5", R.id.expandable5),
                            new TopicConfig(R.id.topic6, R.id.check6, "t6", R.id.expandable6)
                    })
            );
        }
        if (MODE_VERSION_CONTROL.equals(mode)) {
            return ModuleConfig.checklist(
                    R.layout.activity_version_control,
                    new ChecklistConfig(new TopicConfig[]{
                            new TopicConfig(R.id.topic1, R.id.check1, "vc1", R.id.expandable1)
                    })
            );
        }
        if (MODE_VCS_HOSTING.equals(mode)) {
            return ModuleConfig.checklist(
                    R.layout.activity_vcs_hosting,
                    new ChecklistConfig(new TopicConfig[]{
                            new TopicConfig(R.id.topic1, R.id.check1, "vcs1", R.id.expandable1),
                            new TopicConfig(R.id.topic2, R.id.check2, "vcs2", R.id.expandable2)
                    })
            );
        }
        if (MODE_PACKAGE_MANAGERS.equals(mode)) {
            return ModuleConfig.checklist(
                    R.layout.activity_package_managers,
                    new ChecklistConfig(new TopicConfig[]{
                            new TopicConfig(R.id.topic1, R.id.check1, "pm1", R.id.expandable1),
                            new TopicConfig(R.id.topic2, R.id.check2, "pm2", R.id.expandable2),
                            new TopicConfig(R.id.topic3, R.id.check3, "pm3", R.id.expandable3),
                            new TopicConfig(R.id.topic4, R.id.check4, "pm4", R.id.expandable4)
                    })
            );
        }
        if (MODE_CSS_FRAMEWORKS.equals(mode)) {
            return ModuleConfig.checklist(
                    R.layout.activity_css_frameworks,
                    new ChecklistConfig(new TopicConfig[]{
                            new TopicConfig(R.id.topic1, R.id.check1, "cssf1", R.id.expandable1)
                    })
            );
        }
        if (MODE_LEARN_FRAMEWORK.equals(mode)) {
            return ModuleConfig.checklist(
                    R.layout.activity_learn_framework,
                    new ChecklistConfig(new TopicConfig[]{
                            new TopicConfig(R.id.topic1, R.id.check1, "fw1", R.id.expandable1),
                            new TopicConfig(R.id.topic2, R.id.check2, "fw2", R.id.expandable2),
                            new TopicConfig(R.id.topic3, R.id.check3, "fw3", R.id.expandable3),
                            new TopicConfig(R.id.topic4, R.id.check4, "fw4", R.id.expandable4),
                            new TopicConfig(R.id.topic5, R.id.check5, "fw5", R.id.expandable5)
                    })
            );
        }
        return null;
    }

    private void setupLinks(String mode) {
        if (MODE_HTML.equals(mode)) {
            findViewById(R.id.linkHtmlRoadmap).setOnClickListener(v -> Toast.makeText(this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
            findViewById(R.id.linkHtmlCourse).setOnClickListener(v -> openUrl("https://www.freecodecamp.org/learn/2022/responsive-web-design/"));
            findViewById(R.id.linkHtmlVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=mJgBOIoGihA"));
            findViewById(R.id.linkHtmlVideo2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=pQN-pnXPaVg"));
            findViewById(R.id.linkHtmlFeed).setOnClickListener(v -> openUrl("https://dev.to/t/html"));
            return;
        }
        if (MODE_CSS.equals(mode)) {
            findViewById(R.id.linkCssRoadmap).setOnClickListener(v -> Toast.makeText(this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
            findViewById(R.id.linkCssCourse).setOnClickListener(v -> openUrl("https://www.freecodecamp.org/learn/2022/responsive-web-design/"));
            findViewById(R.id.linkCssArticle).setOnClickListener(v -> openUrl("https://web.dev/learn/css/"));
            findViewById(R.id.linkCssVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=n4R2E7O-Ngo"));
            findViewById(R.id.linkCssVideo2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=G3e-cpL7ofc"));
            return;
        }
        if (MODE_JAVASCRIPT.equals(mode)) {
            findViewById(R.id.linkJavascriptRoadmap).setOnClickListener(v -> Toast.makeText(this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
            findViewById(R.id.linkJavascriptArticle1).setOnClickListener(v -> openUrl("https://javascript.info/"));
            findViewById(R.id.linkJavascriptArticle2).setOnClickListener(v -> openUrl("https://javascript30.com/"));
            return;
        }
        if (MODE_INTERNET.equals(mode)) {
            findViewById(R.id.linkArticle).setOnClickListener(v -> openUrl("https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/How_does_the_Internet_work"));
            findViewById(R.id.linkVideo).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=7_LPdttKXPc"));
            findViewById(R.id.linkHttp1).setOnClickListener(v -> openUrl("https://www.cloudflare.com/en-gb/learning/ddos/glossary/hypertext-transfer-protocol-http/"));
            findViewById(R.id.linkHttp2).setOnClickListener(v -> openUrl("https://howhttps.works/"));
            findViewById(R.id.linkHttp3).setOnClickListener(v -> openUrl("https://thenewstack.io/http-3-is-now-a-standard-why-use-it-and-how-to-get-started/"));
            findViewById(R.id.linkDomain1).setOnClickListener(v -> openUrl("https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/What_is_a_domain_name"));
            findViewById(R.id.linkDomain2).setOnClickListener(v -> openUrl("https://www.cloudflare.com/en-gb/learning/dns/glossary/what-is-a-domain-name/"));
            findViewById(R.id.linkDomainVideo).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=lMHzpBwPuG8"));
            findViewById(R.id.linkHosting1).setOnClickListener(v -> openUrl("https://www.namecheap.com/guru-guides/what-is-web-hosting/"));
            findViewById(R.id.linkHostingVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=AXVZYzw8geg"));
            findViewById(R.id.linkHostingVideo2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=Kx_1NYYJS7Q"));
            findViewById(R.id.linkDns1).setOnClickListener(v -> openUrl("https://www.cloudflare.com/en-gb/learning/dns/what-is-dns/"));
            findViewById(R.id.linkDns2).setOnClickListener(v -> openUrl("http://messwithdns.net/"));
            findViewById(R.id.linkDns3).setOnClickListener(v -> openUrl("https://howdns.works/"));
            findViewById(R.id.linkDnsVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=Wj0od2ag5sk"));
            findViewById(R.id.linkBrowser1).setOnClickListener(v -> openUrl("https://www.ramotion.com/blog/what-is-web-browser/"));
            findViewById(R.id.linkBrowser2).setOnClickListener(v -> openUrl("https://developer.mozilla.org/en-US/docs/Web/Performance/Guides/How_browsers_work"));
            findViewById(R.id.linkBrowserVideo).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=5rLFYtXHo9s"));
            return;
        }
        if (MODE_VERSION_CONTROL.equals(mode)) {
            findViewById(R.id.linkRoadmap).setOnClickListener(v -> Toast.makeText(this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
            findViewById(R.id.linkArticle1).setOnClickListener(v -> openUrl("https://cs.fyi/guide/git-cheatsheet"));
            findViewById(R.id.linkArticle2).setOnClickListener(v -> openUrl("https://thenewstack.io/tutorial-git-for-absolutely-everyone/"));
            findViewById(R.id.linkVideo1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=SWYqp7iY_Tc"));
            return;
        }
        if (MODE_VCS_HOSTING.equals(mode)) {
            findViewById(R.id.linkGl1).setOnClickListener(v -> openUrl("https://about.gitlab.com/"));
            findViewById(R.id.linkGl2).setOnClickListener(v -> openUrl("https://docs.gitlab.com/"));
            findViewById(R.id.linkGl3).setOnClickListener(v -> openUrl("https://thenewstack.io/development-connect-git-to-gitlab-for-small-projects/"));
            findViewById(R.id.linkGh1).setOnClickListener(v -> Toast.makeText(this, "Under process, coming soon!", Toast.LENGTH_SHORT).show());
            findViewById(R.id.linkGh2).setOnClickListener(v -> openUrl("https://docs.github.com/en/get-started/start-your-journey/hello-world"));
            findViewById(R.id.linkGh3).setOnClickListener(v -> openUrl("https://learn.github.com/skills"));
            findViewById(R.id.linkGh4).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=w3jLJU7DT5E"));
            return;
        }
        if (MODE_PACKAGE_MANAGERS.equals(mode)) {
            findViewById(R.id.linkNpm1).setOnClickListener(v -> openUrl("https://www.npmjs.com/"));
            findViewById(R.id.linkNpm2).setOnClickListener(v -> openUrl("https://docs.npmjs.com/"));
            findViewById(R.id.linkNpm3).setOnClickListener(v -> openUrl("https://github.com/workshopper/how-to-npm"));
            findViewById(R.id.linkNpm4).setOnClickListener(v -> openUrl("https://peterxjang.com/blog/modern-javascript-explained-for-dinosaurs.html"));
            findViewById(R.id.linkNpm5).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=2V1UUhBJ62Y"));
            findViewById(R.id.linkPnpm1).setOnClickListener(v -> openUrl("https://pnpm.io/"));
            findViewById(R.id.linkPnpm2).setOnClickListener(v -> openUrl("https://blog.bitsrc.io/pnpm-javascript-package-manager-4b5abd59dc9"));
            findViewById(R.id.linkPnpm3).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=MvbReZDSKHI"));
            findViewById(R.id.linkYarn1).setOnClickListener(v -> openUrl("https://classic.yarnpkg.com/en/docs/getting-started"));
            findViewById(R.id.linkYarn2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=g9_6KmiBISk"));
            findViewById(R.id.linkBun1).setOnClickListener(v -> openUrl("https://bun.com/"));
            findViewById(R.id.linkBun2).setOnClickListener(v -> openUrl("http://bun.com/docs"));
            findViewById(R.id.linkBun3).setOnClickListener(v -> openUrl("https://github.com/oven-sh/bun"));
            findViewById(R.id.linkBun4).setOnClickListener(v -> openUrl("https://kinsta.com/blog/bun-sh/"));
            findViewById(R.id.linkBun5).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=dWqNgzZwVJQ"));
            findViewById(R.id.linkBun6).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=U4JVw8K19uY"));
            return;
        }
        if (MODE_CSS_FRAMEWORKS.equals(mode)) {
            findViewById(R.id.link1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=lCxcTsOHrjo"));
            findViewById(R.id.link2).setOnClickListener(v -> openUrl("https://tailwindcss.com/"));
            findViewById(R.id.link3).setOnClickListener(v -> openUrl("https://play.tailwindcss.com/"));
            findViewById(R.id.link4).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=hdGsFpZ0J2E"));
            findViewById(R.id.link5).setOnClickListener(v -> openUrl("https://www.youtube.com/c/TailwindLabs/videos"));
            return;
        }
        if (MODE_LEARN_FRAMEWORK.equals(mode)) {
            findViewById(R.id.link1_1).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=Bvwq_S0n2pk"));
            findViewById(R.id.link1_2).setOnClickListener(v -> openUrl("https://react.dev/"));
            findViewById(R.id.link2_1).setOnClickListener(v -> openUrl("https://vuejs.org/"));
            findViewById(R.id.link2_2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=VeNfHj6MhgA"));
            findViewById(R.id.link3_1).setOnClickListener(v -> openUrl("https://angular.dev/tutorials/learn-angular"));
            findViewById(R.id.link3_2).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=3qBXWUpoPHo"));
            findViewById(R.id.link4_1).setOnClickListener(v -> openUrl("https://www.youtube.com/playlist?list=PL4cUxeGkcC9hlbrVO_2QFVqVPhlZmz7tO"));
            findViewById(R.id.link4_2).setOnClickListener(v -> openUrl("https://svelte.dev/"));
            findViewById(R.id.link4_3).setOnClickListener(v -> openUrl("https://thenewstack.io/svelte-and-the-future-of-front-end-development/"));
            findViewById(R.id.link5_1).setOnClickListener(v -> openUrl("https://www.solidjs.com/"));
            findViewById(R.id.link5_2).setOnClickListener(v -> openUrl("https://www.solidjs.com/tutorial/introduction_basics"));
            findViewById(R.id.link5_3).setOnClickListener(v -> openUrl("https://www.youtube.com/watch?v=hw3Bx5vxKl0"));
        }
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

    private enum ModuleType { SINGLE, CHECKLIST }

    private static class ModuleConfig {
        final int layoutId;
        final ModuleType moduleType;
        final SingleConfig singleConfig;
        final ChecklistConfig checklistConfig;

        private ModuleConfig(int layoutId, ModuleType moduleType, SingleConfig singleConfig, ChecklistConfig checklistConfig) {
            this.layoutId = layoutId;
            this.moduleType = moduleType;
            this.singleConfig = singleConfig;
            this.checklistConfig = checklistConfig;
        }

        static ModuleConfig single(int layoutId, SingleConfig singleConfig) {
            return new ModuleConfig(layoutId, ModuleType.SINGLE, singleConfig, null);
        }

        static ModuleConfig checklist(int layoutId, ChecklistConfig checklistConfig) {
            return new ModuleConfig(layoutId, ModuleType.CHECKLIST, null, checklistConfig);
        }
    }

    private static class SingleConfig {
        final int checkBoxId;
        final int completeButtonId;
        final String progressKey;
        final String completedToast;

        SingleConfig(int checkBoxId, int completeButtonId, String progressKey, String completedToast) {
            this.checkBoxId = checkBoxId;
            this.completeButtonId = completeButtonId;
            this.progressKey = progressKey;
            this.completedToast = completedToast;
        }
    }

    private static class ChecklistConfig {
        final TopicConfig[] topics;

        ChecklistConfig(TopicConfig[] topics) {
            this.topics = topics;
        }
    }

    private static class TopicConfig {
        final int topicId;
        final int checkId;
        final String progressKey;
        final int expandableId;

        TopicConfig(int topicId, int checkId, String progressKey, int expandableId) {
            this.topicId = topicId;
            this.checkId = checkId;
            this.progressKey = progressKey;
            this.expandableId = expandableId;
        }
    }
}
