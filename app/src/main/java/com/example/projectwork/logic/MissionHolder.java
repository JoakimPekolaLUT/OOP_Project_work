package com.example.projectwork.logic;

public class MissionHolder {
    private static MissionSession currentMission;

    public static void setCurrentMission(MissionSession missionSession) {
        currentMission = missionSession;
    }

    public static MissionSession getCurrentMission() {
        return currentMission;
    }

    public static void clear() {
        currentMission = null;
    }
}