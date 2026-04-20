package com.example.projectwork;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.data.Storage;
import com.example.projectwork.model.CrewMember;
import com.example.projectwork.model.Location;
import com.example.projectwork.ui.CrewAdapter;

import java.util.ArrayList;
import java.util.List;

// This activity is used for training crew members in the Simulator.
// The player can train selected crew members using crystals
// or move them back to Quarters.
public class SimulatorActivity extends AppCompatActivity {

    private TextView textCrystals;
    private RecyclerView recyclerViewSimulator;
    private CrewAdapter crewAdapter;
    private final List<CrewMember> crewList = new ArrayList<>();

    // Stores the currently selected crew member
    private CrewMember selectedCrewMember = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        // Find UI elements
        textCrystals = findViewById(R.id.textCrystals);
        Button buttonTrain = findViewById(R.id.buttonTrain);
        Button buttonReturnToQuarters = findViewById(R.id.buttonReturnToQuarters);
        recyclerViewSimulator = findViewById(R.id.recyclerViewSimulator);

        // Set up RecyclerView
        recyclerViewSimulator.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter and handle selection
        crewAdapter = new CrewAdapter(crewList, crewMember -> {
            selectedCrewMember = crewMember;
            Toast.makeText(this, crewMember.getName() + " selected", Toast.LENGTH_SHORT).show();
        });

        recyclerViewSimulator.setAdapter(crewAdapter);

        //Back to main menu.
        Button buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> finish());

        // Train selected crew member
        buttonTrain.setOnClickListener(v -> trainSelectedCrew());

        // Move selected crew member back to Quarters
        buttonReturnToQuarters.setOnClickListener(v -> returnSelectedToQuarters());

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh UI when returning to this screen
        updateUI();
    }

    // Trains the selected crew member if possible
    private void trainSelectedCrew() {
        if (selectedCrewMember == null) {
            Toast.makeText(this, "Select a crew member first", Toast.LENGTH_SHORT).show();
            return;
        }

        Storage storage = Storage.getInstance();
        boolean success = storage.trainCrewMember(selectedCrewMember.getId());

        if (success) {
            Toast.makeText(this, selectedCrewMember.getName() + " trained successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Training failed. Check crystals or crew location.", Toast.LENGTH_SHORT).show();
        }

        updateUI();
    }

    // Moves the selected crew member back to Quarters
    private void returnSelectedToQuarters() {
        if (selectedCrewMember == null) {
            Toast.makeText(this, "Select a crew member first", Toast.LENGTH_SHORT).show();
            return;
        }

        Storage.getInstance().moveCrewMember(selectedCrewMember.getId(), Location.QUARTERS);

        Toast.makeText(this, selectedCrewMember.getName() + " returned to Quarters", Toast.LENGTH_SHORT).show();

        selectedCrewMember = null;
        updateUI();
    }

    // Updates the UI with current crystals and crew in the Simulator
    private void updateUI() {
        Storage storage = Storage.getInstance();

        // Show current crystals
        textCrystals.setText("Crystals: " + storage.getCrystals());

        // Get crew members currently in the Simulator
        List<CrewMember> simulatorCrew = storage.getCrewByLocation(Location.SIMULATOR);

        crewList.clear();
        crewList.addAll(simulatorCrew);
        crewAdapter.notifyDataSetChanged();

        // Clear selection if the crew member is no longer in the list
        if (selectedCrewMember != null) {
            boolean stillExists = false;
            for (CrewMember crewMember : crewList) {
                if (crewMember.getId() == selectedCrewMember.getId()) {
                    stillExists = true;
                    break;
                }
            }
            if (!stillExists) {
                selectedCrewMember = null;
            }
        }
    }
}