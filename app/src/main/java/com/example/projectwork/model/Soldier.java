package com.example.projectwork.model;

// This class represents the Soldier crew member.
// The Soldiers special ability is big damage.
public class Soldier extends CrewMember {

    // Creates a Soldier with high attack but lower defense
    public Soldier(int id, String name) {
        super(id, name, 5, 1, 18);
    }

    // Returns the type of this crew member
    @Override
    public String getType() {
        return "Soldier";
    }

    // Soldier special ability: Power Strike
    // A strong attack that deals high damage in a single hit
    @Override
    public int useSpecialAbility(Enemy enemy) {

        // Add extra damage to a normal attack
        int damage = attack() + 5;

        int dealt = enemy.defend(damage);

        // Track damage dealt for statistics
        addDamageDealt(dealt);

        return dealt;
    }
}