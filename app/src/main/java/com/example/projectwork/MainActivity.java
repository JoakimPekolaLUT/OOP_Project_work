package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectwork.data.Storage;

// This is the main screen of the application.
// It shows the current amount of crystals and allows the user
// to navigate to different parts of the game.
public class MainActivity extends AppCompatActivity {

    // Displays the current number of crystals
    private TextView textCrystals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find UI elements from the layout
        textCrystals = findViewById(R.id.textCrystals);
        Button buttonQuarters = findViewById(R.id.buttonQuarters);
        Button buttonSimulator = findViewById(R.id.buttonSimulator);
        Button buttonMission = findViewById(R.id.buttonMission);
        Button buttonStatistics = findViewById(R.id.buttonStatistics);

        // Update the displayed crystal amount
        updateUI();

        // Opens the Quarters screen where crew members are managed
        buttonQuarters.setOnClickListener(v ->
                startActivity(new Intent(this, QuartersActivity.class)));

        // Opens the Simulator screen where crew members can be trained
        buttonSimulator.setOnClickListener(v ->
                startActivity(new Intent(this, SimulatorActivity.class)));

        // Opens Mission Control where missions are prepared and started
        buttonMission.setOnClickListener(v ->
                startActivity(new Intent(this, MissionControlActivity.class)));

        // Opens the Statistics screen to view game progress
        buttonStatistics.setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh UI when returning to this screen
        updateUI();
    }

    // Updates the crystal display using data from Storage
    private void updateUI() {
        Storage storage = Storage.getInstance();
        textCrystals.setText("Crystals: " + storage.getCrystals());
    }
}