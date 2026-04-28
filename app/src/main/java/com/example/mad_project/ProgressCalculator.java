package com.example.mad_project;

import android.content.SharedPreferences;

/**
 * Computes ALL level-completion states for the Frontend Roadmap
 * from a single SharedPreferences instance.
 *
 * WHY THIS EXISTS:
 * The same 50-line calculation block was copy-pasted in FrontendActivity,
 * Progress, and EditProfileActivity. Any change to progress logic had to be
 * made in 3 places. This class fixes that.
 *
 * HOW TO USE:
 *   SharedPreferences prefs = getSharedPreferences(ProgressPrefs.PREFS_NAME, MODE_PRIVATE);
 *   ProgressCalculator calc = new ProgressCalculator(prefs);
 *   boolean done = calc.isHtmlComplete;
 *   int count  = calc.levelsCompleted;
 *
 * HOW TO EXTEND FOR NEW ROADMAPS:
 *   1. Add keys to ProgressPrefs.java
 *   2. Add new public final fields here
 *   3. Compute them in the constructor
 *   4. Increment levelsCompleted accordingly
 */
public class ProgressCalculator {

    // ─── Individual level states ───────────────────────────────────────────────
    public final boolean isInternetComplete;
    public final boolean isHtmlComplete;
    public final boolean isCssComplete;
    public final boolean isJsComplete;
    public final boolean isVcComplete;
    public final boolean isVcsComplete;
    public final boolean isPmComplete;
    public final boolean isCssfComplete;
    public final boolean isFwComplete;

    /** Raw count of Internet sub-topics completed (out of 6). */
    public final int internetTopicsDone;

    /** Total number of fully-completed levels (0–9). */
    public final int levelsCompleted;

    public ProgressCalculator(SharedPreferences prefs) {
        // Internet: 6 individual topic booleans
        int internetCount = 0;
        for (int i = 1; i <= 6; i++) {
            if (prefs.getBoolean("t" + i, false)) internetCount++;
        }
        internetTopicsDone = internetCount;
        isInternetComplete = (internetTopicsDone == 6);

        isHtmlComplete = prefs.getBoolean(ProgressPrefs.HTML_COMPLETE, false);
        isCssComplete  = prefs.getBoolean(ProgressPrefs.CSS_COMPLETE, false);
        isJsComplete   = prefs.getBoolean(ProgressPrefs.JS_COMPLETE, false);
        isVcComplete   = prefs.getBoolean(ProgressPrefs.VERSION_CONTROL, false);

        isVcsComplete = prefs.getBoolean(ProgressPrefs.VCS_HOSTING_1, false)
                     && prefs.getBoolean(ProgressPrefs.VCS_HOSTING_2, false);

        isPmComplete = prefs.getBoolean(ProgressPrefs.PM_1, false)
                    && prefs.getBoolean(ProgressPrefs.PM_2, false)
                    && prefs.getBoolean(ProgressPrefs.PM_3, false)
                    && prefs.getBoolean(ProgressPrefs.PM_4, false);

        isCssfComplete = prefs.getBoolean(ProgressPrefs.CSS_FRAMEWORKS, false);

        isFwComplete = prefs.getBoolean(ProgressPrefs.FRAMEWORK_1, false)
                    && prefs.getBoolean(ProgressPrefs.FRAMEWORK_2, false)
                    && prefs.getBoolean(ProgressPrefs.FRAMEWORK_3, false)
                    && prefs.getBoolean(ProgressPrefs.FRAMEWORK_4, false)
                    && prefs.getBoolean(ProgressPrefs.FRAMEWORK_5, false);

        // Count completed levels
        int count = 0;
        if (isInternetComplete) count++;
        if (isHtmlComplete)     count++;
        if (isCssComplete)      count++;
        if (isJsComplete)       count++;
        if (isVcComplete)       count++;
        if (isVcsComplete)      count++;
        if (isPmComplete)       count++;
        if (isCssfComplete)     count++;
        if (isFwComplete)       count++;
        levelsCompleted = count;
    }
}
