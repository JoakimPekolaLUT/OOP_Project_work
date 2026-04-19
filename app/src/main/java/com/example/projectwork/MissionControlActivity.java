package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.data.Storage;
import com.example.projectwork.logic.MissionControl;
import com.example.projectwork.logic.MissionHolder;
import com.example.projectwork.model.CrewMember;
import com.example.projectwork.model.Location;
import com.example.projectwork.ui.MissionCrewAdapter;

import java.util.ArrayList;
import java.util.List;

// This activity is used to prepare missions.
// The player can choose two crew members return them to Quarters,
// and start a mission.
public class MissionControlActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMissionCrew;
    private TextView textMissionLog;
    private TextView textSelectedCrew1;
    private TextView textSelectedCrew2;
    private Button buttonStartMission;

    // Adapter and list used to display crew members in Mission Control
    private MissionCrewAdapter crewAdapter;
    private final List<CrewMember> crewList = new ArrayList<>();

    // Stores the currently selected crew members for the mission
    private CrewMember selectedCrew1 = null;
    private CrewMember selectedCrew2 = null;

    // Stores the last tapped crew member for returning them to Quarters
    private CrewMember lastTappedCrew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control);

        // Find UI elements from the layout
        textSelectedCrew1 = findViewById(R.id.textSelectedCrew1);
        textSelectedCrew2 = findViewById(R.id.textSelectedCrew2);
        Button buttonReturnToQuarters = findViewById(R.id.buttonReturnToQuarters);
        buttonStartMission = findViewById(R.id.buttonStartMission);
        recyclerViewMissionCrew = findViewById(R.id.recyclerViewMissionCrew);
        textMissionLog = findViewById(R.id.textMissionLog);

        // Set up RecyclerView
        recyclerViewMissionCrew.setLayoutManager(new LinearLayoutManager(this));

        crewAdapter = new MissionCrewAdapter(crewList, this::handleCrewSelection);
        recyclerViewMissionCrew.setAdapter(crewAdapter);

        // Button for returning a crew member to Quarters
        buttonReturnToQuarters.setOnClickListener(v -> returnSelectedToQuarters());

        // Button for starting the mission
        buttonStartMission.setOnClickListener(v -> startMission());

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the list when returning to this screen
        updateUI();
    }

    // Handles selecting or unselecting crew members for the mission
    private void handleCrewSelection(CrewMember crewMember) {
        lastTappedCrew = crewMember;

        if (selectedCrew1 == null) {
            selectedCrew1 = crewMember;
            Toast.makeText(this, crewMember.getName() + " selected as Crew 1", Toast.LENGTH_SHORT).show();
        } else if (selectedCrew1.getId() == crewMember.getId()) {
            selectedCrew1 = null;
            Toast.makeText(this, crewMember.getName() + " removed from Crew 1", Toast.LENGTH_SHORT).show();
        } else if (selectedCrew2 == null) {
            selectedCrew2 = crewMember;
            Toast.makeText(this, crewMember.getName() + " selected as Crew 2", Toast.LENGTH_SHORT).show();
        } else if (selectedCrew2.getId() == crewMember.getId()) {
            selectedCrew2 = null;
            Toast.makeText(this, crewMember.getName() + " removed from Crew 2", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Already selected 2 crew members", Toast.LENGTH_SHORT).show();
        }

        updateSelectionUI();
    }

    // Returns the last tapped crew member back to Quarters
    private void returnSelectedToQuarters() {
        if (lastTappedCrew == null) {
            Toast.makeText(this, "Tap a crew member first", Toast.LENGTH_SHORT).show();
            return;
        }

        Storage.getInstance().moveCrewMember(lastTappedCrew.getId(), Location.QUARTERS);

        // Remove from selection if needed
        if (selectedCrew1 != null && selectedCrew1.getId() == lastTappedCrew.getId()) {
            selectedCrew1 = null;
        }

        if (selectedCrew2 != null && selectedCrew2.getId() == lastTappedCrew.getId()) {
            selectedCrew2 = null;
        }

        Toast.makeText(this, lastTappedCrew.getName() + " returned to Quarters", Toast.LENGTH_SHORT).show();
        lastTappedCrew = null;
        updateUI();
    }

    // Starts a mission if exactly two crew members are selected
    private void startMission() {
        if (selectedCrew1 == null || selectedCrew2 == null) {
            Toast.makeText(this, "Select exactly 2 crew members", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a mission session and store it so MissionActivity can access it
        MissionControl missionControl = new MissionControl();
        MissionHolder.setCurrentMission(
                missionControl.createMissionSession(selectedCrew1, selectedCrew2)
        );

        // Clear local selections before opening mission screen
        selectedCrew1 = null;
        selectedCrew2 = null;
        lastTappedCrew = null;

        startActivity(new Intent(this, MissionActivity.class));
    }

    // Updates the list of crew members currently in Mission Control
    private void updateUI() {
        List<CrewMember> missionCrew =
                Storage.getInstance().getCrewByLocation(Location.MISSION_CONTROL);

        crewList.clear();
        crewList.addAll(missionCrew);
        crewAdapter.notifyDataSetChanged();

        textMissionLog.setText("Select 2 crew members for mission.");
        updateSelectionUI();
    }

    // Updates the selection text and RecyclerView highlights
    private void updateSelectionUI() {
        String crew1Text = selectedCrew1 == null
                ? "Crew 1: None"
                : "Crew 1: " + selectedCrew1.getType() + " - " + selectedCrew1.getName();

        String crew2Text = selectedCrew2 == null
                ? "Crew 2: None"
                : "Crew 2: " + selectedCrew2.getType() + " - " + selectedCrew2.getName();

        textSelectedCrew1.setText(crew1Text);
        textSelectedCrew2.setText(crew2Text);

        int crew1Id = selectedCrew1 == null ? -1 : selectedCrew1.getId();
        int crew2Id = selectedCrew2 == null ? -1 : selectedCrew2.getId();
        crewAdapter.setSelectedCrewIds(crew1Id, crew2Id);

        // Mission can only start when both crew members are selected
        buttonStartMission.setEnabled(selectedCrew1 != null && selectedCrew2 != null);
    }
}