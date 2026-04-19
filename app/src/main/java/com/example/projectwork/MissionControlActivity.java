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

public class MissionControlActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMissionCrew;
    private TextView textMissionLog;
    private TextView textSelectedCrew1;
    private TextView textSelectedCrew2;
    private Button buttonStartMission;

    private MissionCrewAdapter crewAdapter;
    private final List<CrewMember> crewList = new ArrayList<>();

    private CrewMember selectedCrew1 = null;
    private CrewMember selectedCrew2 = null;
    private CrewMember lastTappedCrew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control);

        textSelectedCrew1 = findViewById(R.id.textSelectedCrew1);
        textSelectedCrew2 = findViewById(R.id.textSelectedCrew2);
        Button buttonReturnToQuarters = findViewById(R.id.buttonReturnToQuarters);
        buttonStartMission = findViewById(R.id.buttonStartMission);
        recyclerViewMissionCrew = findViewById(R.id.recyclerViewMissionCrew);
        textMissionLog = findViewById(R.id.textMissionLog);

        recyclerViewMissionCrew.setLayoutManager(new LinearLayoutManager(this));

        crewAdapter = new MissionCrewAdapter(crewList, this::handleCrewSelection);
        recyclerViewMissionCrew.setAdapter(crewAdapter);

        buttonReturnToQuarters.setOnClickListener(v -> returnSelectedToQuarters());
        buttonStartMission.setOnClickListener(v -> startMission());

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

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

    private void returnSelectedToQuarters() {
        if (lastTappedCrew == null) {
            Toast.makeText(this, "Tap a crew member first", Toast.LENGTH_SHORT).show();
            return;
        }

        Storage.getInstance().moveCrewMember(lastTappedCrew.getId(), Location.QUARTERS);

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

    private void startMission() {
        if (selectedCrew1 == null || selectedCrew2 == null) {
            Toast.makeText(this, "Select exactly 2 crew members", Toast.LENGTH_SHORT).show();
            return;
        }

        MissionControl missionControl = new MissionControl();
        MissionHolder.setCurrentMission(
                missionControl.createMissionSession(selectedCrew1, selectedCrew2)
        );

        selectedCrew1 = null;
        selectedCrew2 = null;
        lastTappedCrew = null;

        startActivity(new Intent(this, MissionActivity.class));
    }

    private void updateUI() {
        List<CrewMember> missionCrew =
                Storage.getInstance().getCrewByLocation(Location.MISSION_CONTROL);

        crewList.clear();
        crewList.addAll(missionCrew);
        crewAdapter.notifyDataSetChanged();

        textMissionLog.setText("Select 2 crew members for mission.");
        updateSelectionUI();
    }

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

        buttonStartMission.setEnabled(selectedCrew1 != null && selectedCrew2 != null);
    }
}