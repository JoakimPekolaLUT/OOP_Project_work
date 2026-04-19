package com.example.projectwork.logic;

// This class is used to temporarily store the current mission session.
public class MissionHolder {

    // Stores the current active mission
    private static MissionSession currentMission;

    // Sets the current mission session
    public static void setCurrentMission(MissionSession missionSession) {
        currentMission = missionSession;
    }

    // Returns the current mission session
    public static MissionSession getCurrentMission() {
        return currentMission;
    }

    // Clears the stored mission when it is no longer needed
    public static void clear() {
        currentMission = null;
    }
}