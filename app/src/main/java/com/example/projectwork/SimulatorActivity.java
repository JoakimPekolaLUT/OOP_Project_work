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

public class SimulatorActivity extends AppCompatActivity {

    private TextView textCrystals;
    private RecyclerView recyclerViewSimulator;
    private CrewAdapter crewAdapter;
    private final List<CrewMember> crewList = new ArrayList<>();

    private CrewMember selectedCrewMember = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        textCrystals = findViewById(R.id.textCrystals);
        Button buttonTrain = findViewById(R.id.buttonTrain);
        Button buttonReturnToQuarters = findViewById(R.id.buttonReturnToQuarters);
        recyclerViewSimulator = findViewById(R.id.recyclerViewSimulator);

        recyclerViewSimulator.setLayoutManager(new LinearLayoutManager(this));

        crewAdapter = new CrewAdapter(crewList, crewMember -> {
            selectedCrewMember = crewMember;
            Toast.makeText(this, crewMember.getName() + " selected", Toast.LENGTH_SHORT).show();
        });

        recyclerViewSimulator.setAdapter(crewAdapter);

        buttonTrain.setOnClickListener(v -> trainSelectedCrew());

        buttonReturnToQuarters.setOnClickListener(v -> returnSelectedToQuarters());

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

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

    private void updateUI() {
        Storage storage = Storage.getInstance();

        textCrystals.setText("Crystals: " + storage.getCrystals());

        List<CrewMember> simulatorCrew = storage.getCrewByLocation(Location.SIMULATOR);

        crewList.clear();
        crewList.addAll(simulatorCrew);
        crewAdapter.notifyDataSetChanged();

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