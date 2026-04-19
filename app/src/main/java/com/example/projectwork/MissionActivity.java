package com.example.projectwork;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectwork.logic.MissionHolder;
import com.example.projectwork.logic.MissionSession;
import com.example.projectwork.model.CrewMember;

public class MissionActivity extends AppCompatActivity {

    private TextView textTurnInfo;
    private TextView textMissionLog;
    private Button buttonAttack;
    private Button buttonSpecial;

    private MissionSession missionSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        textTurnInfo = findViewById(R.id.textTurnInfo);
        textMissionLog = findViewById(R.id.textMissionLog);
        buttonAttack = findViewById(R.id.buttonAttack);
        buttonSpecial = findViewById(R.id.buttonSpecial);

        missionSession = MissionHolder.getCurrentMission();

        if (missionSession == null) {
            finish();
            return;
        }

        buttonAttack.setOnClickListener(v -> {
            missionSession.normalAttack();
            updateUI();
        });

        buttonSpecial.setOnClickListener(v -> {
            missionSession.specialAttack();
            updateUI();
        });

        updateUI();
    }

    private void updateUI() {
        if (missionSession == null) return;

        // update log
        StringBuilder builder = new StringBuilder();
        for (String line : missionSession.getLog()) {
            builder.append(line).append("\n\n");
        }
        textMissionLog.setText(builder.toString());

        // if mission finished
        if (missionSession.isMissionOver()) {
            textTurnInfo.setText("Mission Finished");
            buttonAttack.setEnabled(false);
            buttonSpecial.setEnabled(false);
            return;
        }

        // current turn
        CrewMember current = missionSession.getCurrentCrewMember();

        if (current != null) {
            textTurnInfo.setText("Current Turn: " + current.getType() + " - " + current.getName());

            buttonSpecial.setText(getSpecialAbilityText(current.getType()));
        }
    }

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