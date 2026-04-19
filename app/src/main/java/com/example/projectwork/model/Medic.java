package com.example.projectwork.model;

public class Medic extends CrewMember {

    public Medic(int id, String name) {
        super(id, name, 7, 2, 18);
    }

    @Override
    public String getType() {
        return "Medic";
    }

    @Override
    public int useSpecialAbility(Enemy enemy) {
        energy = Math.min(maxEnergy, energy + 5);
        return 0;
    }
}