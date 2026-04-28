package com.example.mad_project;

/**
 * Central registry of ALL SharedPreferences keys used for tracking progress.
 *
 * WHY THIS EXISTS:
 * Using raw string literals like "h_complete" scattered across 4+ files causes
 * silent bugs when you add new roadmaps. This class is the single source of truth.
 *
 * HOW TO ADD A NEW ROADMAP:
 * 1. Add your new key constants here (e.g., BACKEND_COMPLETE = "be_complete")
 * 2. Update ProgressCalculator.java to read the new keys
 * 3. Use the constant everywhere — never type the raw string again
 */
public final class ProgressPrefs {

    /** Name of the SharedPreferences file used for all progress data. */
    public static final String PREFS_NAME = "progress";

    // ─── Internet (6 individual topics) ───────────────────────────────────────
    public static final String INTERNET_T1 = "t1";
    public static final String INTERNET_T2 = "t2";
    public static final String INTERNET_T3 = "t3";
    public static final String INTERNET_T4 = "t4";
    public static final String INTERNET_T5 = "t5";
    public static final String INTERNET_T6 = "t6";

    // ─── HTML ─────────────────────────────────────────────────────────────────
    public static final String HTML_COMPLETE = "h_complete";

    // ─── CSS ──────────────────────────────────────────────────────────────────
    public static final String CSS_COMPLETE = "c_complete";

    // ─── JavaScript ───────────────────────────────────────────────────────────
    public static final String JS_COMPLETE = "j_complete";

    // ─── Version Control ──────────────────────────────────────────────────────
    public static final String VERSION_CONTROL = "vc1";

    // ─── VCS Hosting ──────────────────────────────────────────────────────────
    public static final String VCS_HOSTING_1 = "vcs1";
    public static final String VCS_HOSTING_2 = "vcs2";

    // ─── Package Managers ─────────────────────────────────────────────────────
    public static final String PM_1 = "pm1";
    public static final String PM_2 = "pm2";
    public static final String PM_3 = "pm3";
    public static final String PM_4 = "pm4";

    // ─── CSS Frameworks ───────────────────────────────────────────────────────
    public static final String CSS_FRAMEWORKS = "cssf1";

    // ─── Learn a Framework (5 options) ────────────────────────────────────────
    public static final String FRAMEWORK_1 = "fw1";
    public static final String FRAMEWORK_2 = "fw2";
    public static final String FRAMEWORK_3 = "fw3";
    public static final String FRAMEWORK_4 = "fw4";
    public static final String FRAMEWORK_5 = "fw5";

    // ─── UI State ─────────────────────────────────────────────────────────────
    public static final String FRONTEND_CONGRATS_SHOWN = "frontend_congrats_shown";

    // ─── Gamification ─────────────────────────────────────────────────────────
    public static final String LEARNING_STREAK          = "learning_streak";
    public static final String TIME_SPENT_WEEK_MINUTES  = "time_spent_week_minutes";

    // Utility class — no instantiation
    private ProgressPrefs() {}
}
