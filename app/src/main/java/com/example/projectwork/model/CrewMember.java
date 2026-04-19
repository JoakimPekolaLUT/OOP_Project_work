package com.example.projectwork.model;

import com.example.projectwork.model.MissionType;
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

    protected int missionsCompleted;
    protected int victories;
    protected int trainingSessions;
    protected int totalDamageDealt;
    protected int totalDamageTaken;

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

    public int attack() {
        return baseSkill + xp;
    }

    public int attackWithRandomness() {
        int randomBonus = (int) (Math.random() * 3);
        return attack() + randomBonus;
    }

    public int defend(int incomingDamage) {
        int finalDamage = Math.max(0, incomingDamage - defense);
        energy -= finalDamage;
        if (energy < 0) {
            energy = 0;
        }
        totalDamageTaken += finalDamage;
        return finalDamage;
    }

    public void train() {
        trainingSessions++;
        gainXp(1);
    }

    public void gainXp(int amount) {
        xp += amount;
        if (xp >= level * 3) {
            levelUp();
        }
    }

    public void levelUp() {
        level++;
        baseSkill++;
        maxEnergy++;
        energy = maxEnergy;
    }

    public void restoreEnergy() {
        energy = maxEnergy;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public void addDamageDealt(int damage) {
        totalDamageDealt += damage;
    }

    public void addVictory() {
        victories++;
    }

    public void addMissionCompleted() {
        missionsCompleted++;
    }

    public abstract String getType();

    public abstract int useSpecialAbility(Enemy enemy);

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