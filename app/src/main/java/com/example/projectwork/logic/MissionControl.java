package com.example.projectwork.logic;

import com.example.projectwork.data.Storage;
import com.example.projectwork.model.Alien;
import com.example.projectwork.model.CrewMember;
import com.example.projectwork.model.Enemy;
import com.example.projectwork.model.MissionType;
import com.example.projectwork.model.Parasite;
import com.example.projectwork.model.Robot;

import java.util.Random;

// This class is responsible for creating and starting missions.
// It generates random enemies random mission types and creates
// a MissionSession that handles the actual combat.
public class MissionControl {

    private final Storage storage;
    private final Random random;

    // Initializes storage and random generator
    public MissionControl() {
        storage = Storage.getInstance();
        random = new Random();
    }

    // Creates a random enemy based on mission difficulty
    // Difficulty increases as more missions are completed
    public Enemy createEnemy() {
        int difficulty = Math.max(1, storage.getTotalMissions() + 1);

        int randomEnemy = random.nextInt(3);

        if (randomEnemy == 0) return new Alien(difficulty);
        if (randomEnemy == 1) return new Robot(difficulty);
        return new Parasite(difficulty);
    }

    // Creates a random mission type
    // Used for specialization bonuses and mission variation
    public MissionType createMissionType() {
        MissionType[] missionTypes = MissionType.values();
        return missionTypes[random.nextInt(missionTypes.length)];
    }

    // Creates a new mission session with two crew members
    // This starts the mission but the actual combat is handled in MissionSession
    public MissionSession createMissionSession(CrewMember first, CrewMember second) {

        // Increase total mission count
        storage.incrementMissions();

        // Create a new mission with random enemy and mission type
        return new MissionSession(
                first,
                second,
                createEnemy(),
                createMissionType()
        );
    }
}