package com.example.projectwork.logic;

import com.example.projectwork.data.Storage;
import com.example.projectwork.model.Alien;
import com.example.projectwork.model.CrewMember;
import com.example.projectwork.model.Enemy;
import com.example.projectwork.model.MissionType;
import com.example.projectwork.model.Parasite;
import com.example.projectwork.model.Robot;

import java.util.Random;

public class MissionControl {
    private final Storage storage;
    private final Random random;

    public MissionControl() {
        storage = Storage.getInstance();
        random = new Random();
    }

    public Enemy createEnemy() {
        int difficulty = Math.max(1, storage.getTotalMissions() + 1);
        int randomEnemy = random.nextInt(3);

        if (randomEnemy == 0) return new Alien(difficulty);
        if (randomEnemy == 1) return new Robot(difficulty);
        return new Parasite(difficulty);
    }

    public MissionType createMissionType() {
        MissionType[] missionTypes = MissionType.values();
        return missionTypes[random.nextInt(missionTypes.length)];
    }

    public MissionSession createMissionSession(CrewMember first, CrewMember second) {
        storage.incrementMissions();
        return new MissionSession(first, second, createEnemy(), createMissionType());
    }
}