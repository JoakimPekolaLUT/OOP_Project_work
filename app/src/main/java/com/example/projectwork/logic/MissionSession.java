package com.example.projectwork.logic;

import com.example.projectwork.data.Storage;
import com.example.projectwork.model.CrewMember;
import com.example.projectwork.model.Enemy;
import com.example.projectwork.model.Location;
import com.example.projectwork.model.MissionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MissionSession {

    private final Storage storage;
    private final Random random;

    private CrewMember crew1;
    private CrewMember crew2;
    private Enemy enemy;
    private MissionType missionType;

    private int currentTurnIndex = 0;
    private boolean missionOver = false;

    private final List<String> log = new ArrayList<>();

    public MissionSession(CrewMember crew1, CrewMember crew2, Enemy enemy, MissionType missionType) {
        this.storage = Storage.getInstance();
        this.random = new Random();
        this.crew1 = crew1;
        this.crew2 = crew2;
        this.enemy = enemy;
        this.missionType = missionType;

        log.add("=== MISSION START ===");
        log.add("Mission Type: " + missionType);
        log.add("Crew 1: " + crew1.toString());
        log.add("Crew 2: " + crew2.toString());
        log.add("Enemy: " + enemy.toString());
    }

    public CrewMember getCurrentCrewMember() {
        if (currentTurnIndex == 0) {
            return crew1;
        } else {
            return crew2;
        }
    }

    public CrewMember getOtherCrewMember() {
        if (currentTurnIndex == 0) {
            return crew2;
        } else {
            return crew1;
        }
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public List<String> getLog() {
        return log;
    }

    public boolean isMissionOver() {
        return missionOver;
    }

    public void normalAttack() {
        if (missionOver) return;

        CrewMember current = getCurrentCrewMember();
        if (current == null || !current.isAlive()) {
            nextTurn();
            return;
        }

        int bonus = current.getMissionBonus(missionType);
        int damage = current.attackWithRandomness() + bonus;
        int dealt = enemy.defend(damage);
        current.addDamageDealt(dealt);

        log.add(current.getName() + " used normal attack and dealt " + dealt + " damage.");
        if (bonus > 0) {
            log.add(current.getName() + " gained +" + bonus + " specialization bonus for " + missionType + ".");
        }

        enemyRetaliateIfAlive(current);
        checkMissionState();
        nextTurn();
    }

    public void specialAttack() {
        if (missionOver) return;

        CrewMember current = getCurrentCrewMember();
        if (current == null || !current.isAlive()) {
            nextTurn();
            return;
        }

        int dealt = current.useSpecialAbility(enemy);

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

        enemyRetaliateIfAlive(current);
        checkMissionState();
        nextTurn();
    }

    private void enemyRetaliateIfAlive(CrewMember target) {
        if (!enemy.isAlive()) return;

        int enemyDamage = enemy.attack();
        int taken = target.defend(enemyDamage);
        log.add(enemy.getName() + " retaliated against " + target.getName() + " for " + taken + " damage.");
        log.add(target.getName() + " energy: " + target.getEnergy() + "/" + target.getMaxEnergy());
    }

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

    private void checkMissionState() {
        if (!enemy.isAlive()) {
            missionOver = true;
            log.add("=== MISSION COMPLETE ===");
            log.add("The enemy has been defeated!");

            rewardCrew(crew1);
            rewardCrew(crew2);

            int crystalReward = 2 + random.nextInt(4);
            storage.addCrystals(crystalReward);
            log.add("Reward: " + crystalReward + " crystals");
            return;
        }

        if (!crew1.isAlive() && !crew2.isAlive()) {
            missionOver = true;
            log.add("=== MISSION FAILED ===");
            log.add("All crew members were defeated.");

            removeIfDead(crew1);
            removeIfDead(crew2);
        }
    }

    private void rewardCrew(CrewMember crewMember) {
        if (crewMember.isAlive()) {
            crewMember.gainXp(1);
            crewMember.addVictory();
            crewMember.addMissionCompleted();
            crewMember.setLocation(Location.QUARTERS);
            crewMember.restoreEnergy();
            log.add(crewMember.getName() + " survived, gained 1 XP, and returned to Quarters.");
        } else {
            storage.removeCrewMember(crewMember.getId());
            log.add(crewMember.getName() + " was defeated and removed from the program.");
        }
    }

    private void removeIfDead(CrewMember crewMember) {
        if (!crewMember.isAlive()) {
            storage.removeCrewMember(crewMember.getId());
            log.add(crewMember.getName() + " was defeated and removed from the program.");
        }
    }
}