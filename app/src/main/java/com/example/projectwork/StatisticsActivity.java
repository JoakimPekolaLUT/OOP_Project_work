package com.example.projectwork;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectwork.data.Storage;
import com.example.projectwork.model.CrewMember;

import java.util.List;

// This activity displays overall game statistics.
// It shows colony information such as crystals, total missions,
// total crew members, and detailed statistics for each crew member.
public class StatisticsActivity extends AppCompatActivity {

    private TextView textStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Button buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> finish());

        // Find the TextView that shows all statistics
        textStatistics = findViewById(R.id.textStatistics);

        // Load statistics when the activity starts
        updateStatistics();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Refresh statistics when returning to this screen
        updateStatistics();
    }

    // Builds and displays the full statistics text
    private void updateStatistics() {
        Storage storage = Storage.getInstance();
        List<CrewMember> allCrew = storage.getAllCrewMembers();

        StringBuilder builder = new StringBuilder();

        // General colony statistics
        builder.append("=== COLONY STATISTICS ===\n\n");
        builder.append("Crystals: ").append(storage.getCrystals()).append("\n");
        builder.append("Total Missions: ").append(storage.getTotalMissions()).append("\n");
        builder.append("Total Crew Members: ").append(allCrew.size()).append("\n\n");

        // Crew member specific statistics
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