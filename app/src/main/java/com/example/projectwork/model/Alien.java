package com.example.projectwork.model;

// This class represents an Alien enemy.
//Alien is a medium difficulty enemy.
public class Alien extends Enemy {

    //creates an Alien with stats scaled by difficulty
    public Alien(int difficulty) {
        super("Alien Raider", 5 + difficulty, 2 + (difficulty / 2), 18 + difficulty * 2);
    }

    @Override
    public String getType() {
        return "Alien";
    }
}