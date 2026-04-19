package com.example.projectwork.model;

public class Parasite extends Enemy {

    public Parasite(int difficulty) {
        super("Space Parasite", 4 + difficulty, 1 + (difficulty / 2), 16 + difficulty * 2);
    }

    @Override
    public String getType() {
        return "Parasite";
    }
}