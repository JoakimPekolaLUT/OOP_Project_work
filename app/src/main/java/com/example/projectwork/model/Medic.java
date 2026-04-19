package com.example.projectwork.model;

// This class represents the Medic crew member.
// The Medic can heal team members.
public class Medic extends CrewMember {

    // Creates a Medic with balanced stats and slightly higher survivability
    public Medic(int id, String name) {
        super(id, name, 3, 3, 22);
    }

    // Returns the type of this crew member
    @Override
    public String getType() {
        return "Medic";
    }

    // Medic special ability: Heal
    // Restores some of the Medics energy instead of attacking
    // This makes the Medic useful for survival in longer missions
    @Override
    public int useSpecialAbility(Enemy enemy) {

        // Restore energy but does not exceed maximum
        energy = Math.min(maxEnergy, energy + 5);

        // No damage is dealt
        return 0;
    }
}