package org.rspeer.models;

import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.EquipmentSlot;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.movement.position.Position;

import java.util.HashMap;
import java.util.HashSet;

public class Progressive {

    private String name;
    private int minimumLevel = 0;
    private int maximumLevel = 99;
    private Skill skill;
    private Combat.AttackStyle style;
    private HashSet<String> enemies = new HashSet<>();
    private HashSet<String> food = new HashSet<>();
    private HashSet<String> loot = new HashSet<>();
    private HashMap<EquipmentSlot, String> equipmentMap;
    private Position position;
    private int radius;
    private boolean prioritizeLooting;
    private boolean buryBones;
    private boolean randomIdle;
    private int randomIdleBuffer;

    public void setRandomIdle(boolean randomIdle) {
        this.randomIdle = randomIdle;
    }

    public int getRandomIdleBuffer() {
        return randomIdleBuffer;
    }

    public void setRandomIdleBuffer(int randomIdleBuffer) {
        this.randomIdleBuffer = randomIdleBuffer;
    }

    public boolean isRandomIdle() {
        return randomIdle;
    }

    public void setBuryBones(boolean buryBones) {
        this.buryBones = buryBones;
    }

    public boolean isBuryBones() {
        return buryBones;
    }

    public void setPrioritizeLooting(boolean prioritizeLooting) {
        this.prioritizeLooting = prioritizeLooting;
    }

    public boolean isPrioritizeLooting() {
        return prioritizeLooting;
    }

    public void setEnemies(HashSet<String> enemies) {
        this.enemies = enemies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEquipmentMap(HashMap<EquipmentSlot, String> equipmentMap) {
        this.equipmentMap = equipmentMap;
    }

    public HashMap<EquipmentSlot, String> getEquipmentMap() {
        return equipmentMap;
    }

    public HashSet<String> getEnemies() {
        return enemies;
    }

    public void setFood(HashSet<String> food) {
        this.food = food;
    }

    public HashSet<String> getFood() {
        return food;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getMinimumLevel() {
        return minimumLevel;
    }

    public void setMinimumLevel(int minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    public int getMaximumLevel() {
        return maximumLevel;
    }

    public void setMaximumLevel(int maximumLevel) {
        this.maximumLevel = maximumLevel;
    }

    public Combat.AttackStyle getStyle() {
        return style;
    }

    public HashSet<String> getLoot() {
        return loot;
    }

    public void setLoot(HashSet<String> loot) {
        this.loot = loot;
    }

    public void setStyle(Combat.AttackStyle style) {
        this.style = style;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
}
