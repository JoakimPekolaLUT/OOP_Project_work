package com.example.projectwork.model;

// This class represents the Pilot crew member.
// The Pilot can use a double attack.
public class Pilot extends CrewMember {

    // Creates a Pilot with predefined starting stats
    public Pilot(int id, String name) {
        super(id, name, 4, 2, 20);
    }

    // Returns the type of this crew member
    @Override
    public String getType() {
        return "Pilot";
    }

    // Pilot special ability: Double Strike
    // The pilot attacks twice with smaller damage each time
    // This is useful against enemies with low defense
    @Override
    public int useSpecialAbility(Enemy enemy) {

        // First hit (half strength + small bonus)
        int firstHit = enemy.defend(Math.max(1, attack() / 2 + 1));

        // Second hit (same as first)
        int secondHit = enemy.defend(Math.max(1, attack() / 2 + 1));

        int totalDamage = firstHit + secondHit;

        // Track total damage dealt for statistics
        addDamageDealt(totalDamage);

        return totalDamage;
    }
}