package com.example.projectwork;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectwork.data.Storage;
import com.example.projectwork.model.CrewMember;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private TextView textStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        textStatistics = findViewById(R.id.textStatistics);
        updateStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
    }

    private void updateStatistics() {
        Storage storage = Storage.getInstance();
        List<CrewMember> allCrew = storage.getAllCrewMembers();

        StringBuilder builder = new StringBuilder();
        builder.append("=== COLONY STATISTICS ===\n\n");
        builder.append("Crystals: ").append(storage.getCrystals()).append("\n");
        builder.append("Total Missions: ").append(storage.getTotalMissions()).append("\n");
        builder.append("Total Crew Members: ").append(allCrew.size()).append("\n\n");

        builder.append("=== CREW MEMBER STATS ===\n\n");

        if (allCrew.isEmpty()) {
            builder.append("No crew members available.");
        } else {
            for (CrewMember crewMember : allCrew) {
                builder.append(crewMember.getType()).append(" - ")
                        .append(crewMember.getName()).append("\n");
                builder.append("Level: ").append(crewMember.getLevel()).append("\n");
                builder.append("XP: ").append(crewMember.getXp()).append("\n");
                builder.append("Missions Completed: ").append(crewMember.getMissionsCompleted()).append("\n");
                builder.append("Victories: ").append(crewMember.getVictories()).append("\n");
                builder.append("Training Sessions: ").append(crewMember.getTrainingSessions()).append("\n");
                builder.append("Damage Dealt: ").append(crewMember.getTotalDamageDealt()).append("\n");
                builder.append("Damage Taken: ").append(crewMember.getTotalDamageTaken()).append("\n");
                builder.append("Location: ").append(crewMember.getLocation()).append("\n");
                builder.append("\n----------------------\n\n");
            }
        }

        textStatistics.setText(builder.toString());
    }
}