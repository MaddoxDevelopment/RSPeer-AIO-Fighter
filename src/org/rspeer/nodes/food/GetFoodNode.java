package org.rspeer.nodes.food;

import org.rspeer.wrappers.CombatWrapper;
import org.rspeer.framework.Node;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.wrappers.BankWrapper;

public class GetFoodNode extends Node {

    private boolean running;

    @Override
    public boolean validate() {
        return running || CombatWrapper.getHealthPercent() < 40;
    }

    @Override
    public void execute() {
        if(!BankWrapper.openNearest()) {
            return;
        }
        Bank.withdrawAll("Lobster");
    }

    @Override
    public void onInvalid() {
        running = false;
    }

    @Override
    public String status() {
        return null;
    }
}
