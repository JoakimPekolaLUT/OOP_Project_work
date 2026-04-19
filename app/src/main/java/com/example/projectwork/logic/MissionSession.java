package com.example.projectwork.logic;

import com.example.projectwork.data.Storage;
import com.example.projectwork.model.CrewMember;
import com.example.projectwork.model.Enemy;
import com.example.projectwork.model.Location;
import com.example.projectwork.model.MissionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// This class handles one full mission session.
// It controls turn-based combat mission progress rewards and defeat handling.
public class MissionSession {

    private final Storage storage;
    private final Random random;

    private CrewMember crew1;
    private CrewMember crew2;
    private Enemy enemy;
    private MissionType missionType;

    // 0 means crew1 turn 1 means crew2 turn
    private int currentTurnIndex = 0;

    // This becomes true when the mission is won or lost
    private boolean missionOver = false;

    // Stores all mission events so they can be shown in the UI
    private final List<String> log = new ArrayList<>();

    // Creates a new mission session with two crew members one enemy and one random mission type
    public MissionSession(CrewMember crew1, CrewMember crew2, Enemy enemy, MissionType missionType) {
        this.storage = Storage.getInstance();
        this.random = new Random();
        this.crew1 = crew1;
        this.crew2 = crew2;
        this.enemy = enemy;
        this.missionType = missionType;

        // Add the starting information to the mission log
        log.add("=== MISSION START ===");
        log.add("Mission Type: " + missionType);
        log.add("Crew 1: " + crew1.toString());
        log.add("Crew 2: " + crew2.toString());
        log.add("Enemy: " + enemy.toString());
    }

    // Returns the crew member whose turn it currently is
    public CrewMember getCurrentCrewMember() {
        if (currentTurnIndex == 0) {
            return crew1;
        } else {
            return crew2;
        }
    }

    // Returns the other crew member meaning the one who is not taking the current turn
    public CrewMember getOtherCrewMember() {
        if (currentTurnIndex == 0) {
            return crew2;
        } else {
            return crew1;
        }
    }

    // Returns the current enemy
    public Enemy getEnemy() {
        return enemy;
    }

    // Returns the mission type
    public MissionType getMissionType() {
        return missionType;
    }

    // Returns the full mission log for displaying in the UI
    public List<String> getLog() {
        return log;
    }

    // Tells the UI whether the mission has already ended
    public boolean isMissionOver() {
        return missionOver;
    }

    // Handles a normal attack for the current crew member
    public void normalAttack() {
        if (missionOver) return;

        CrewMember current = getCurrentCrewMember();

        // If the current crew member is dead skip the turn
        if (current == null || !current.isAlive()) {
            nextTurn();
            return;
        }

        // Add specialization bonus if the mission type matches the crew member class
        int bonus = current.getMissionBonus(missionType);

        // Attack uses base damage + randomness + possible mission bonus
        int damage = current.attackWithRandomness() + bonus;
        int dealt = enemy.defend(damage);
        current.addDamageDealt(dealt);

        log.add(current.getName() + " used normal attack and dealt " + dealt + " damage.");

        if (bonus > 0) {
            log.add(current.getName() + " gained +" + bonus + " specialization bonus for " + missionType + ".");
        }

        // Enemy attacks back if it survived
        enemyRetaliateIfAlive(current);

        // Check if the mission ended after this action
        checkMissionState();

        // Move to the next turn
        nextTurn();
    }

    // Handles the special ability of the current crew member
    // Polymorphism is used here because each class has its own version of useSpecialAbility()
    public void specialAttack() {
        if (missionOver) return;

        CrewMember current = getCurrentCrewMember();

        // If the current crew member is dead skip the turn
        if (current == null || !current.isAlive()) {
            nextTurn();
            return;
        }

        // Calls the correct special ability depending on the crew member class
        int dealt = current.useSpecialAbility(enemy);

        // Different log messages make the abilities clearer during gameplay
        if (current.getType().equals("Medic")) {
            log.add(current.getName() + " used Heal and restored energy.");
        } else if (current.getType().equals("Engineer")) {
            log.add(current.getName() + " used Armor Break and dealt " + dealt + " damage.");
        } else if (current.getType().equals("Scientist")) {
            log.add(current.getName() + " used True Damage and dealt " + dealt + " damage.");
        } else if (current.getType().equals("Pilot")) {
            log.add(current.getName() + " used Double Strike and dealt " + dealt + " total damage.");
        } else if (current.getType().equals("Soldier")) {
            log.add(current.getName() + " used Power Strike and dealt " + dealt + " damage.");
        } else {
            log.add(current.getName() + " used special ability.");
        }

        // Enemy attacks back if it survived
        enemyRetaliateIfAlive(current);

        // Check if the mission ended after this action
        checkMissionState();

        // Move to the next turn
        nextTurn();
    }

    // Makes the enemy retaliate against the acting crew member if the enemy is still alive
    private void enemyRetaliateIfAlive(CrewMember target) {
        if (!enemy.isAlive()) return;

        int enemyDamage = enemy.attack();
        int taken = target.defend(enemyDamage);

        log.add(enemy.getName() + " retaliated against " + target.getName() + " for " + taken + " damage.");
        log.add(target.getName() + " energy: " + target.getEnergy() + "/" + target.getMaxEnergy());
    }

    // Changes whose turn it is
    // If both crew members are alive turns alternate normally
    // If only one is alive that one keeps taking turns
    private void nextTurn() {
        if (missionOver) return;

        if (crew1.isAlive() && crew2.isAlive()) {
            currentTurnIndex = 1 - currentTurnIndex;
        } else if (crew1.isAlive()) {
            currentTurnIndex = 0;
        } else if (crew2.isAlive()) {
            currentTurnIndex = 1;
        }

        checkMissionState();
    }

    // Checks whether the mission is won or lost
    private void checkMissionState() {
        // Mission win condition: enemy defeated
        if (!enemy.isAlive()) {
            missionOver = true;
            log.add("=== MISSION COMPLETE ===");
            log.add("The enemy has been defeated!");

            // Reward both crew members if they survived
            rewardCrew(crew1);
            rewardCrew(crew2);

            // Random crystal reward for winning
            int crystalReward = 2 + random.nextInt(4);
            storage.addCrystals(crystalReward);
            log.add("Reward: " + crystalReward + " crystals");
            return;
        }

        // Mission lose condition: both crew members defeated
        if (!crew1.isAlive() && !crew2.isAlive()) {
            missionOver = true;
            log.add("=== MISSION FAILED ===");
            log.add("All crew members were defeated.");

            removeIfDead(crew1);
            removeIfDead(crew2);
        }
    }

    // Gives rewards to a surviving crew member or removes them if dead
    private void rewardCrew(CrewMember crewMember) {
        if (crewMember.isAlive()) {
            crewMember.gainXp(1);
            crewMember.addVictory();
            crewMember.addMissionCompleted();

            // Surviving crew members return home and recover energy
            crewMember.setLocation(Location.QUARTERS);
            crewMember.restoreEnergy();

            log.add(crewMember.getName() + " survived, gained 1 XP, and returned to Quarters.");
        } else {
            storage.removeCrewMember(crewMember.getId());
            log.add(crewMember.getName() + " was defeated and removed from the program.");
        }
    }

    // Removes a crew member from storage if they died in the mission
    private void removeIfDead(CrewMember crewMember) {
        if (!crewMember.isAlive()) {
            storage.removeCrewMember(crewMember.getId());
            log.add(crewMember.getName() + " was defeated and removed from the program.");
        }
    }
}