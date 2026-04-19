package com.example.projectwork.model;

// This abstract class represents enemies in missions.
// It defines shared stats and behavior such as attacking defending,
// and checking if the enemy is still alive.
public abstract class Enemy {
    protected String name;
    protected int skill;
    protected int defense;
    protected int energy;
    protected int maxEnergy;

    // Creates a new enemy with stats based on difficulty level
    public Enemy(String name, int skill, int defense, int maxEnergy) {
        this.name = name;
        this.skill = skill;
        this.defense = defense;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    // Calculates attack damage based on skill
    public int attack() {
        return skill + (int) (Math.random() * 3);
    }

    // Reduces incoming damage using defense and lowers energy
    public int defend(int incomingDamage) {
        int finalDamage = Math.max(0, incomingDamage - defense);
        energy -= finalDamage;
        if (energy < 0) {
            energy = 0;
        }
        return finalDamage;
    }

    // Returns true if the enemy still has energy left
    public boolean isAlive() {
        return energy > 0;
    }

    public abstract String getType();

    public String getName() {
        return name;
    }

    public int getSkill() {
        return skill;
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

    // Returns a description used in mission logs
    @Override
    public String toString() {
        return getType() + " - " + name +
                " | Skill: " + skill +
                " | Def: " + defense +
                " | Energy: " + energy + "/" + maxEnergy;
    }

    // Reduces enemy defense (engineers special ability)
    public void reduceDefense(int amount) {
        defense -= amount;
        if (defense < 0) {
            defense = 0;
        }
    }
}