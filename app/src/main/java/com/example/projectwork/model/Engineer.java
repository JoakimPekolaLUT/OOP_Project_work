package com.example.projectwork.model;

public class Engineer extends CrewMember {

    public Engineer(int id, String name) {
        super(id, name, 6, 3, 19);
    }

    @Override
    public String getType() {
        return "Engineer";
    }

    @Override
    public int useSpecialAbility(Enemy enemy) {
        enemy.reduceDefense(1);
        int dealt = enemy.defend(attack());
        addDamageDealt(dealt);
        return dealt;
    }
}