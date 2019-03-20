package org.rspeer.nodes.food;

import org.rspeer.config.Config;
import org.rspeer.debug.Logger;
import org.rspeer.wrappers.CombatWrapper;
import org.rspeer.framework.Node;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.function.Predicate;

public class EatNode extends Node {

    private Item food;
    private int eatTillPercent;
    private final Predicate<Item> FOOD = i -> Config.getFood().contains(i.getName().toLowerCase());

    @Override
    public boolean validate() {
        int current = CombatWrapper.getHealthPercent();
        if (eatTillPercent != -1 && current < eatTillPercent) {
            food = Inventory.getFirst(FOOD);
            return true;
        }
        boolean lowHealth = current < 40;
        if (!lowHealth) {
            eatTillPercent = -1;
            return false;
        }
        food = Inventory.getFirst(FOOD);
        return food != null;
    }

    @Override
    public void execute() {
        Logger.debug("Attempting to eat.");
        if (food == null) {
            Logger.severe("No food?");
            eatTillPercent = -1;
            return;
        }
        if(eatTillPercent == -1) {
            eatTillPercent = Random.high(55, 75);
        }
        food.interact("Eat");
    }

    @Override
    public void onInvalid() {
        eatTillPercent = -1;
        food = null;
    }

    @Override
    public String status() {
        return null;
    }
}
