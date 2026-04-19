package com.example.projectwork.model;

public class Robot extends Enemy {

    public Robot(int difficulty) {
        super("Security Robot", 6 + difficulty, 3 + (difficulty / 2), 20 + difficulty * 2);
    }

    @Override
    public String getType() {
        return "Robot";
    }
}