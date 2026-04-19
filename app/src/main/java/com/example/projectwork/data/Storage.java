package com.example.projectwork.data;

import com.example.projectwork.model.CrewMember;
import com.example.projectwork.model.Engineer;
import com.example.projectwork.model.Location;
import com.example.projectwork.model.Medic;
import com.example.projectwork.model.Pilot;
import com.example.projectwork.model.Scientist;
import com.example.projectwork.model.Soldier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// This class works as the main storage for the game.
// It stores all crew members crystals mission count, and handles
// creating moving and training crew members.
public class Storage {
    private static Storage instance;

    // HashMap stores crew members by id so they can be accessed quickly
    private final HashMap<Integer, CrewMember> crewMembers = new HashMap<>();

    private int nextId = 1;
    private int crystals = 10;
    private int totalMissions = 0;

    // Private constructor is used so only one Storage object can exist
    private Storage() {
    }

    // Returns the single shared Storage instance
    // This is an example of the Singleton design pattern
    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    // Creates a new crew member based on the selected class type
    // and stores it in the HashMap
    public CrewMember createCrewMember(String type, String name) {
        CrewMember crewMember;

        switch (type) {
            case "Pilot":
                crewMember = new Pilot(nextId, name);
                break;
            case "Engineer":
                crewMember = new Engineer(nextId, name);
                break;
            case "Medic":
                crewMember = new Medic(nextId, name);
                break;
            case "Scientist":
                crewMember = new Scientist(nextId, name);
                break;
            case "Soldier":
                crewMember = new Soldier(nextId, name);
                break;
            default:
                return null;
        }

        crewMembers.put(nextId, crewMember);
        nextId++;
        return crewMember;
    }

    // Removes a crew member from storage for example when defeated in a mission
    public void removeCrewMember(int id) {
        crewMembers.remove(id);
    }

    // Returns one crew member by id
    public CrewMember getCrewMember(int id) {
        return crewMembers.get(id);
    }

    // Returns all crew members as a list
    public List<CrewMember> getAllCrewMembers() {
        return new ArrayList<>(crewMembers.values());
    }

    // Returns only the crew members that are currently in the given location
    public List<CrewMember> getCrewByLocation(Location location) {
        List<CrewMember> result = new ArrayList<>();

        for (CrewMember crewMember : crewMembers.values()) {
            if (crewMember.getLocation() == location) {
                result.add(crewMember);
            }
        }

        return result;
    }

    // Moves a crew member to a new location
    // If they return to Quarters, their energy is restored
    public void moveCrewMember(int id, Location newLocation) {
        CrewMember crewMember = crewMembers.get(id);

        if (crewMember != null) {
            crewMember.setLocation(newLocation);

            if (newLocation == Location.QUARTERS) {
                crewMember.restoreEnergy();
            }
        }
    }

    // Trains a crew member if they are in the simulator and crystals are available
    // Training costs 1 crystal and gives experience
    public boolean trainCrewMember(int id) {
        CrewMember crewMember = crewMembers.get(id);

        if (crewMember != null && crystals > 0 && crewMember.getLocation() == Location.SIMULATOR) {
            crystals--;
            crewMember.train();
            return true;
        }

        return false;
    }

    // Returns the current amount of crystals
    public int getCrystals() {
        return crystals;
    }

    // Adds crystals, for example as mission rewards
    public void addCrystals(int amount) {
        crystals += amount;
    }

    // Returns how many missions have been started so far
    public int getTotalMissions() {
        return totalMissions;
    }

    // Increases the mission counter by one
    public void incrementMissions() {
        totalMissions++;
    }
}