package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectwork.data.Storage;

public class MainActivity extends AppCompatActivity {

    private TextView textCrystals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textCrystals = findViewById(R.id.textCrystals);
        Button buttonQuarters = findViewById(R.id.buttonQuarters);
        Button buttonSimulator = findViewById(R.id.buttonSimulator);
        Button buttonMission = findViewById(R.id.buttonMission);
        Button buttonStatistics = findViewById(R.id.buttonStatistics);

        updateUI();

        buttonQuarters.setOnClickListener(v ->
                startActivity(new Intent(this, QuartersActivity.class)));

        buttonSimulator.setOnClickListener(v ->
                startActivity(new Intent(this, SimulatorActivity.class)));

        buttonMission.setOnClickListener(v ->
                startActivity(new Intent(this, MissionControlActivity.class)));

        buttonStatistics.setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        Storage storage = Storage.getInstance();
        textCrystals.setText("Crystals: " + storage.getCrystals());
    }
}