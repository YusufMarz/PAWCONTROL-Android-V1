package com.example.pawcontrolv1.ShelterVet;


import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawcontrolv1.R;

import java.util.ArrayList;
import java.util.List;

public class EducationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EducationAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupRecyclerView() {
        List<String> questions = getStrayCatQuestions();

        adapter = new EducationAdapter(questions);
        adapter.setOnItemClickListener((position, question) -> {
            Toast.makeText(this, "Clicked: " + question, Toast.LENGTH_SHORT).show();
            // Navigate to detail screen here
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private List<String> getStrayCatQuestions() {
        List<String> questions = new ArrayList<>();
        questions.add("Why stray always stay at 1 places ?");
        questions.add("Why stray always stay at 1 places ?");
        questions.add("Why stray always stay at 1 places ?");
        questions.add("Why stray always stay at 1 places ?");
        questions.add("Why stray always stay at 1 places ?");
        // Add more questions as needed
        return questions;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}