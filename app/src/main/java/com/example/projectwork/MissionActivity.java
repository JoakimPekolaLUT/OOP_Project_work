package com.example.projectwork;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectwork.logic.MissionHolder;
import com.example.projectwork.logic.MissionSession;
import com.example.projectwork.model.CrewMember;

// This activity handles the mission gameplay screen.
// It shows the mission log current turn, and allows the player
// to perform actions like attacking or using special abilities.
public class MissionActivity extends AppCompatActivity {

    private TextView textTurnInfo;
    private TextView textMissionLog;
    private Button buttonAttack;
    private Button buttonSpecial;

    // Stores the current mission session
    private MissionSession missionSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        // Get UI elements
        textTurnInfo = findViewById(R.id.textTurnInfo);
        textMissionLog = findViewById(R.id.textMissionLog);
        buttonAttack = findViewById(R.id.buttonAttack);
        buttonSpecial = findViewById(R.id.buttonSpecial);

        // Get the current mission from MissionHolder
        missionSession = MissionHolder.getCurrentMission();

        // If no mission exists, close this activity
        if (missionSession == null) {
            finish();
            return;
        }

        // Normal attack button
        buttonAttack.setOnClickListener(v -> {
            missionSession.normalAttack();
            updateUI();
        });

        // Special ability button
        buttonSpecial.setOnClickListener(v -> {
            missionSession.specialAttack();
            updateUI();
        });

        // Update UI when activity starts
        updateUI();
    }

    // Updates all UI elements based on the current mission state
    private void updateUI() {
        if (missionSession == null) return;

        // Build and display mission log text
        StringBuilder builder = new StringBuilder();
        for (String line : missionSession.getLog()) {
            builder.append(line).append("\n\n");
        }
        textMissionLog.setText(builder.toString());

        // If mission is finished disable buttons
        if (missionSession.isMissionOver()) {
            textTurnInfo.setText("Mission Finished");
            buttonAttack.setEnabled(false);
            buttonSpecial.setEnabled(false);
            return;
        }

        // Show whose turn it is
        CrewMember current = missionSession.getCurrentCrewMember();
        if (current != null) {
            textTurnInfo.setText("Current Turn: " + current.getType() + " - " + current.getName());

            // Update special ability button text based on crew type
            buttonSpecial.setText(getSpecialAbilityText(current.getType()));
        }
    }

    // Returns the correct button text depending on crew type
    private String getSpecialAbilityText(String type) {
        switch (type) {
            case "Pilot":
                return "Use Double Strike";
            case "Engineer":
                return "Use Armor Break";
            case "Medic":
                return "Use Heal";
            case "Scientist":
                return "Use True Damage";
            case "Soldier":
                return "Use Power Strike";
            default:
                return "Use Special Ability";
        }
    }
}