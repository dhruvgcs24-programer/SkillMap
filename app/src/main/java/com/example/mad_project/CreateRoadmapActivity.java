package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateRoadmapActivity extends AppCompatActivity {

    private EditText etGoal, etLevel, etDuration;
    private Button btnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_roadmap);

        etGoal = findViewById(R.id.etGoal);
        etLevel = findViewById(R.id.etLevel);
        etDuration = findViewById(R.id.etDuration);
        btnGenerate = findViewById(R.id.btnGenerateRoadmap);

        btnGenerate.setOnClickListener(v -> {
            String goal = etGoal.getText().toString().trim();
            String level = etLevel.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();

            if (goal.isEmpty() || level.isEmpty() || duration.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String roadmap = generateRoadmap(goal, level, duration);

            Intent intent = new Intent(CreateRoadmapActivity.this, RoadmapResultActivity.class);
            intent.putExtra("roadmap_text", roadmap);
            startActivity(intent);
        });
    }

    private String generateRoadmap(String goal, String level, String duration) {
        String lowerGoal = goal.toLowerCase();
        String lowerLevel = level.toLowerCase();
        String lowerDuration = duration.toLowerCase();

        String intro = "Personalized Roadmap\n\n"
                + "Goal: " + goal + "\n"
                + "Level: " + level + "\n"
                + "Duration: " + duration + "\n\n";

        if (lowerGoal.contains("android")) {
            if (lowerLevel.contains("beginner")) {
                return intro
                        + "Week 1-2:\n"
                        + "- Learn Java/Kotlin basics\n"
                        + "- Understand variables, loops, functions\n\n"
                        + "Week 3-4:\n"
                        + "- Install Android Studio\n"
                        + "- Learn project structure, Activity, XML layouts\n\n"
                        + "Week 5-6:\n"
                        + "- Learn Intents, RecyclerView, CardView\n"
                        + "- Build a simple notes or to-do app\n\n"
                        + "Week 7-8:\n"
                        + "- Learn Firebase basics\n"
                        + "- Add login and database\n\n"
                        + "Mini Projects:\n"
                        + "- Calculator app\n"
                        + "- To-do app\n"
                        + "- Student profile app\n\n"
                        + "Final Goal:\n"
                        + "- Build one complete Android project with login, dashboard, and data storage.";
            } else {
                return intro
                        + "Week 1-2:\n"
                        + "- Revise Activities, Fragments, RecyclerView\n\n"
                        + "Week 3-4:\n"
                        + "- Learn MVVM, ViewModel, LiveData\n\n"
                        + "Week 5-6:\n"
                        + "- Work with Firebase/Auth/API integration\n\n"
                        + "Week 7-8:\n"
                        + "- Build a production-style app\n\n"
                        + "Final Goal:\n"
                        + "- Create a polished Android app with backend integration.";
            }
        }

        if (lowerGoal.contains("web") || lowerGoal.contains("frontend")) {
            return intro
                    + "Week 1-2:\n"
                    + "- Learn HTML basics\n"
                    + "- Learn CSS styling\n\n"
                    + "Week 3-4:\n"
                    + "- Learn Flexbox, Grid, responsive design\n\n"
                    + "Week 5-6:\n"
                    + "- Learn JavaScript basics\n"
                    + "- DOM manipulation\n\n"
                    + "Week 7-8:\n"
                    + "- Build landing page and portfolio\n\n"
                    + "Final Goal:\n"
                    + "- Build a responsive frontend project.";
        }

        if (lowerGoal.contains("python")) {
            return intro
                    + "Week 1-2:\n"
                    + "- Learn Python syntax, loops, functions\n\n"
                    + "Week 3-4:\n"
                    + "- Learn lists, dictionaries, file handling\n\n"
                    + "Week 5-6:\n"
                    + "- Practice mini problems and projects\n\n"
                    + "Week 7-8:\n"
                    + "- Build one automation or beginner project\n\n"
                    + "Final Goal:\n"
                    + "- Become comfortable writing Python programs independently.";
        }

        return intro
                + "Week 1-2:\n"
                + "- Learn the basics of " + goal + "\n"
                + "- Understand the core concepts\n\n"
                + "Week 3-4:\n"
                + "- Practice beginner exercises\n"
                + "- Follow tutorials and notes\n\n"
                + "Week 5-6:\n"
                + "- Build a mini project\n"
                + "- Revise weak areas\n\n"
                + "Week 7-8:\n"
                + "- Build a final project\n"
                + "- Prepare a portfolio/demo\n\n"
                + "Recommended Approach:\n"
                + "- Study consistently\n"
                + "- Practice daily\n"
                + "- Build mini projects regularly\n\n"
                + "Duration Consideration:\n"
                + "- Your selected duration is " + duration + "\n"
                + "- Adjust study hours based on your schedule.";
    }
}