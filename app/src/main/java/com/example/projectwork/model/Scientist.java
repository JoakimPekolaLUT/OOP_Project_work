package com.example.projectwork.model;

// This class represents the Scientist crew member.
// The Scientist special ability ignores enemy defense.
public class Scientist extends CrewMember {

    // Creates a Scientist with its starting stats
    public Scientist(int id, String name) {
        super(id, name, 3, 2, 18);
    }

    // Returns the type of this crew member
    @Override
    public String getType() {
        return "Scientist";
    }

    // Scientist special ability: True Damage
    // This attack ignores the enemys defense and directly lowers its energy
    @Override
    public int useSpecialAbility(Enemy enemy) {
        int damage = attack() + 2;

        // Deal damage directly to enemy energy without using normal defense calculation
        enemy.energy -= damage;

        if (enemy.energy < 0) {
            enemy.energy = 0;
        }

        // Track dealt damage for statistics
        addDamageDealt(damage);

        return damage;
    }
}