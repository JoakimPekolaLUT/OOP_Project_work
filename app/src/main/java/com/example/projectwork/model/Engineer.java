package com.example.projectwork.model;

// This class represents the Engineer crew member.
// The Engineer can reduce enemy defense making future attacks stronger.
public class Engineer extends CrewMember {

    // Creates an Engineer with balanced stats
    // Slightly lower attack but useful support ability
    public Engineer(int id, String name) {
        super(id, name, 3, 4, 20);
    }

    // Returns the type of this crew member
    @Override
    public String getType() {
        return "Engineer";
    }

    // Engineer special ability: Armor Break
    // Reduces the enemys defense and deals normal damage
    // This makes all future attacks more effective
    @Override
    public int useSpecialAbility(Enemy enemy) {

        // Reduce enemy defense permanently for the mission
        enemy.reduceDefense(1);

        // Perform a normal attack after weakening the enemy
        int dealt = enemy.defend(attack());

        // Track damage dealt for statistics
        addDamageDealt(dealt);

        return dealt;
    }
}