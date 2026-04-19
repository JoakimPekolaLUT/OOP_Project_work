package com.example.projectwork.model;

// This class represents a Parasite enemy.
//parasite is the weakest enemy.
public class Parasite extends Enemy {

    //creates a parasite with stats scaled by difficulty.
    public Parasite(int difficulty) {
        super("Space Parasite", 4 + difficulty, 1 + (difficulty / 2), 16 + difficulty * 2);
    }

    @Override
    public String getType() {
        return "Parasite";
    }
}