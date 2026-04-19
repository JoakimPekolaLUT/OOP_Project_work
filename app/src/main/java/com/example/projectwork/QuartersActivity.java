package com.example.projectwork;

import android.os.Bundle;
import android.widget.Button;
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

public class QuartersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCrew;
    private CrewAdapter crewAdapter;
    private final List<CrewMember> crewList = new ArrayList<>();
    private int recruitCounter = 1;
    private CrewMember selectedCrewMember = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        recyclerViewCrew = findViewById(R.id.recyclerViewCrew);
        Button buttonAddCrew = findViewById(R.id.buttonAddCrew);
        Button buttonMoveToSimulator = findViewById(R.id.buttonMoveToSimulator);
        Button buttonMoveToMission = findViewById(R.id.buttonMoveToMission);

        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(this));

        crewAdapter = new CrewAdapter(crewList, crewMember -> {
            selectedCrewMember = crewMember;
            Toast.makeText(this, crewMember.getName() + " selected", Toast.LENGTH_SHORT).show();
        });

        recyclerViewCrew.setAdapter(crewAdapter);

        buttonAddCrew.setOnClickListener(v -> {
            Storage storage = Storage.getInstance();

            String[] types = {"Pilot", "Engineer", "Medic", "Scientist", "Soldier"};
            String type = types[(recruitCounter - 1) % types.length];
            String name = "Crew" + recruitCounter;

            storage.createCrewMember(type, name);
            recruitCounter++;

            updateList();
        });

        buttonMoveToSimulator.setOnClickListener(v -> {
            if (selectedCrewMember == null) {
                Toast.makeText(this, "Select a crew member first", Toast.LENGTH_SHORT).show();
                return;
            }

            Storage.getInstance().moveCrewMember(selectedCrewMember.getId(), Location.SIMULATOR);
            Toast.makeText(this, selectedCrewMember.getName() + " moved to Simulator", Toast.LENGTH_SHORT).show();
            selectedCrewMember = null;
            updateList();
        });

        buttonMoveToMission.setOnClickListener(v -> {
            if (selectedCrewMember == null) {
                Toast.makeText(this, "Select a crew member first", Toast.LENGTH_SHORT).show();
                return;
            }

            Storage.getInstance().moveCrewMember(selectedCrewMember.getId(), Location.MISSION_CONTROL);
            Toast.makeText(this, selectedCrewMember.getName() + " moved to Mission Control", Toast.LENGTH_SHORT).show();
            selectedCrewMember = null;
            updateList();
        });

        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        Storage storage = Storage.getInstance();
        List<CrewMember> quartersCrew = storage.getCrewByLocation(Location.QUARTERS);

        crewList.clear();
        crewList.addAll(quartersCrew);
        crewAdapter.notifyDataSetChanged();
    }
}