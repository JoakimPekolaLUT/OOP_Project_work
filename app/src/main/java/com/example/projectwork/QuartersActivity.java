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

// This activity is used to manage crew members in Quarters.
// The player can recruit new crew members and move selected ones
// to the Simulator or Mission Control.
public class QuartersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCrew;
    private CrewAdapter crewAdapter;
    private final List<CrewMember> crewList = new ArrayList<>();

    // Used to create simple default names
    private int recruitCounter = 1;

    // Stores the currently selected crew member
    private CrewMember selectedCrewMember = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        // Find UI elements from the layout
        recyclerViewCrew = findViewById(R.id.recyclerViewCrew);
        Button buttonAddCrew = findViewById(R.id.buttonAddCrew);
        Button buttonMoveToSimulator = findViewById(R.id.buttonMoveToSimulator);
        Button buttonMoveToMission = findViewById(R.id.buttonMoveToMission);

        // Set up RecyclerView with a vertical layout
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter and handle crew member selection
        crewAdapter = new CrewAdapter(crewList, crewMember -> {
            selectedCrewMember = crewMember;
            Toast.makeText(this, crewMember.getName() + " selected", Toast.LENGTH_SHORT).show();
        });

        recyclerViewCrew.setAdapter(crewAdapter);

        //Back to main menu.
        Button buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> finish());

        // Recruit a new crew member with a rotating class type
        buttonAddCrew.setOnClickListener(v -> {
            Storage storage = Storage.getInstance();

            String[] types = {"Pilot", "Engineer", "Medic", "Scientist", "Soldier"};
            String type = types[(recruitCounter - 1) % types.length];
            String name = "Crew" + recruitCounter;

            storage.createCrewMember(type, name);
            recruitCounter++;

            updateList();
        });

        // Move the selected crew member to the Simulator
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

        // Move the selected crew member to Mission Control
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

        // Refresh the crew list when returning to this screen
        updateList();
    }

    // Updates the RecyclerView with crew members currently in Quarters
    private void updateList() {
        Storage storage = Storage.getInstance();
        List<CrewMember> quartersCrew = storage.getCrewByLocation(Location.QUARTERS);

        crewList.clear();
        crewList.addAll(quartersCrew);
        crewAdapter.notifyDataSetChanged();
    }
}