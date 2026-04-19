package com.example.projectwork.model;

public class Pilot extends CrewMember {

    public Pilot(int id, String name) {
        super(id, name, 5, 4, 20);
    }

    @Override
    public String getType() {
        return "Pilot";
    }

    @Override
    public int useSpecialAbility(Enemy enemy) {
        int firstHit = enemy.defend(Math.max(1, attack() / 2 + 1));
        int secondHit = enemy.defend(Math.max(1, attack() / 2 + 1));
        int total = firstHit + secondHit;
        addDamageDealt(total);
        return total;
    }
}