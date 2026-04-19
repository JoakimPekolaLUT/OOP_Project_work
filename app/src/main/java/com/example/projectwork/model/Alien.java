package com.example.projectwork.model;

public class Alien extends Enemy {

    public Alien(int difficulty) {
        super("Alien Raider", 5 + difficulty, 2 + (difficulty / 2), 18 + difficulty * 2);
    }

    @Override
    public String getType() {
        return "Alien";
    }
}