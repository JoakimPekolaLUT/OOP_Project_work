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

public class Storage {
    private static Storage instance;

    private final HashMap<Integer, CrewMember> crewMembers = new HashMap<>();
    private int nextId = 1;
    private int crystals = 10;
    private int totalMissions = 0;

    private Storage() {
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

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

    public void removeCrewMember(int id) {
        crewMembers.remove(id);
    }

    public CrewMember getCrewMember(int id) {
        return crewMembers.get(id);
    }

    public List<CrewMember> getAllCrewMembers() {
        return new ArrayList<>(crewMembers.values());
    }

    public List<CrewMember> getCrewByLocation(Location location) {
        List<CrewMember> result = new ArrayList<>();
        for (CrewMember crewMember : crewMembers.values()) {
            if (crewMember.getLocation() == location) {
                result.add(crewMember);
            }
        }
        return result;
    }

    public void moveCrewMember(int id, Location newLocation) {
        CrewMember crewMember = crewMembers.get(id);
        if (crewMember != null) {
            crewMember.setLocation(newLocation);
            if (newLocation == Location.QUARTERS) {
                crewMember.restoreEnergy();
            }
        }
    }

    public boolean trainCrewMember(int id) {
        CrewMember crewMember = crewMembers.get(id);
        if (crewMember != null && crystals > 0 && crewMember.getLocation() == Location.SIMULATOR) {
            crystals--;
            crewMember.train();
            return true;
        }
        return false;
    }

    public int getCrystals() {
        return crystals;
    }

    public void addCrystals(int amount) {
        crystals += amount;
    }

    public int getTotalMissions() {
        return totalMissions;
    }

    public void incrementMissions() {
        totalMissions++;
    }
}