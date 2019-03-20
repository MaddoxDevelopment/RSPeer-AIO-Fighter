package org.rspeer.nodes.loot;

import org.rspeer.config.Config;
import org.rspeer.framework.Node;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.wrappers.BankWrapper;

public class DepositLootNode extends Node {

    @Override
    public boolean validate() {
        if(Config.getLoot().size() == 0) {
            return false;
        }
        return Inventory.contains(Config.getLoot().toArray(new String[0])) && Inventory.isFull();
    }

    @Override
    public void execute() {
        if(BankWrapper.openNearest()) {
            Bank.depositAll(Config.getLoot().toArray(new String[0]));
        }
    }

    @Override
    public String status() {
        return "Depositing Loot";
    }
}
