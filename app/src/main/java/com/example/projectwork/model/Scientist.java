package com.example.projectwork.model;

public class Scientist extends CrewMember {

    public Scientist(int id, String name) {
        super(id, name, 8, 1, 17);
    }

    @Override
    public String getType() {
        return "Scientist";
    }

    @Override
    public int useSpecialAbility(Enemy enemy) {
        int damage = attack() + 2;
        enemy.energy -= damage;
        if (enemy.energy < 0) {
            enemy.energy = 0;
        }
        addDamageDealt(damage);
        return damage;
    }
}