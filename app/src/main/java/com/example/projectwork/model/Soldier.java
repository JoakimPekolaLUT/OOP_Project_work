package com.example.projectwork.model;

public class Soldier extends CrewMember {

    public Soldier(int id, String name) {
        super(id, name, 9, 0, 16);
    }

    @Override
    public String getType() {
        return "Soldier";
    }

    @Override
    public int useSpecialAbility(Enemy enemy) {
        int damage = attack() + 5;
        int dealt = enemy.defend(damage);
        addDamageDealt(dealt);
        return dealt;
    }
}