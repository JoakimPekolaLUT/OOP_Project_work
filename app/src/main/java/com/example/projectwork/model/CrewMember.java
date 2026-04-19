package com.example.projectwork.model;

import com.example.projectwork.model.MissionType;

// This abstract class is the base class for all crew member types.
// It stores shared stats and behavior such as attacking defending,
// training gaining experience and moving between locations.
public abstract class CrewMember {
    protected int id;
    protected String name;
    protected int baseSkill;
    protected int defense;
    protected int energy;
    protected int maxEnergy;
    protected int level;
    protected int xp;
    protected Location location;

    // Used for statistics
    protected int missionsCompleted;
    protected int victories;
    protected int trainingSessions;
    protected int totalDamageDealt;
    protected int totalDamageTaken;

    // Creates a new crew member with starting stats
    public CrewMember(int id, String name, int baseSkill, int defense, int maxEnergy) {
        this.id = id;
        this.name = name;
        this.baseSkill = baseSkill;
        this.defense = defense;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.level = 1;
        this.xp = 0;
        this.location = Location.QUARTERS;
    }

    // Normal attack value is based on skill and gained experience
    public int attack() {
        return baseSkill + xp;
    }

    // Adds a small random bonus to attacks to make missions more random
    public int attackWithRandomness() {
        int randomBonus = (int) (Math.random() * 3);
        return attack() + randomBonus;
    }

    // Reduces incoming damage by defense and lowers current energy
    public int defend(int incomingDamage) {
        int finalDamage = Math.max(0, incomingDamage - defense);
        energy -= finalDamage;

        if (energy < 0) {
            energy = 0;
        }

        totalDamageTaken += finalDamage;
        return finalDamage;
    }

    // Training increases training statistics and gives experience
    public void train() {
        trainingSessions++;
        gainXp(1);
    }

    // Adds experience points and levels up the crew member if enough XP is reached
    public void gainXp(int amount) {
        xp += amount;

        if (xp >= level * 3) {
            levelUp();
        }
    }

    // Leveling up improves the crew members stats and restores energy
    public void levelUp() {
        level++;
        baseSkill++;
        maxEnergy++;
        energy = maxEnergy;
    }

    // Restores the crew members energy to full
    public void restoreEnergy() {
        energy = maxEnergy;
    }

    // Returns true if the crew member still has energy left
    public boolean isAlive() {
        return energy > 0;
    }

    // Adds dealt damage to the statistics
    public void addDamageDealt(int damage) {
        totalDamageDealt += damage;
    }

    // Adds one victory to the statistics
    public void addVictory() {
        victories++;
    }

    // Adds one completed mission to the statistics
    public void addMissionCompleted() {
        missionsCompleted++;
    }

    // Returns the class type name such as Pilot Medic or Soldier
    public abstract String getType();

    // Each subclass has its own special ability
    // This is an example of polymorphism in the project
    public abstract int useSpecialAbility(Enemy enemy);

    // Gives a class bonus depending on the mission type
    // This supports specialization bonuses for certain mission types
    public int getMissionBonus(MissionType missionType) {
        switch (getType()) {
            case "Soldier":
                return missionType == MissionType.COMBAT ? 2 : 0;
            case "Engineer":
                return missionType == MissionType.REPAIR ? 2 : 0;
            case "Scientist":
                return missionType == MissionType.RESEARCH ? 2 : 0;
            case "Medic":
                return missionType == MissionType.RESCUE ? 2 : 0;
            case "Pilot":
                return missionType == MissionType.NAVIGATION ? 2 : 0;
            default:
                return 0;
        }
    }
    //Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBaseSkill() {
        return baseSkill;
    }

    public int getDefense() {
        return defense;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public Location getLocation() {
        return location;
    }

    // Changes the current location of the crew member
    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMissionsCompleted() {
        return missionsCompleted;
    }

    public int getVictories() {
        return victories;
    }

    public int getTrainingSessions() {
        return trainingSessions;
    }

    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public int getTotalDamageTaken() {
        return totalDamageTaken;
    }

    // Returns a text description of the crew member for lists and logs
    @Override
    public String toString() {
        return getType() + " - " + name +
                " | LVL: " + level +
                " | XP: " + xp +
                " | Skill: " + baseSkill +
                " | Def: " + defense +
                " | Energy: " + energy + "/" + maxEnergy;
    }
}