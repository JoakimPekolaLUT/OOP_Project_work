package com.example.projectwork.model;

public abstract class Enemy {
    protected String name;
    protected int skill;
    protected int defense;
    protected int energy;
    protected int maxEnergy;

    public Enemy(String name, int skill, int defense, int maxEnergy) {
        this.name = name;
        this.skill = skill;
        this.defense = defense;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public int attack() {
        return skill + (int) (Math.random() * 3);
    }

    public int defend(int incomingDamage) {
        int finalDamage = Math.max(0, incomingDamage - defense);
        energy -= finalDamage;
        if (energy < 0) {
            energy = 0;
        }
        return finalDamage;
    }

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

    @Override
    public String toString() {
        return getType() + " - " + name +
                " | Skill: " + skill +
                " | Def: " + defense +
                " | Energy: " + energy + "/" + maxEnergy;
    }

    public void reduceDefense(int amount) {
        defense -= amount;
        if (defense < 0) {
            defense = 0;
        }
    }
}