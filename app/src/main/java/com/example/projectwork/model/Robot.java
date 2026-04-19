package com.example.projectwork.model;

// This class represents a Robot enemy.
//Robot is the hardest enemy.
public class Robot extends Enemy {

    //creates a robot with stats scaled by difficulty
    public Robot(int difficulty) {
        super("Security Robot", 6 + difficulty, 3 + (difficulty / 2), 20 + difficulty * 2);
    }

    @Override
    public String getType() {
        return "Robot";
    }
}