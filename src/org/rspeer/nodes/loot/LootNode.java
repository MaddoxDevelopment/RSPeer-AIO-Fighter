package org.rspeer.nodes.loot;

import org.rspeer.CombatStore;
import org.rspeer.config.Config;
import org.rspeer.framework.Node;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.services.LootService;

public class LootNode extends Node {

    private Pickable[] items;

    @Override
    public boolean validate() {
        if(Config.getLoot().size() == 0) {
            return false;
        }
        final boolean hasTarget = CombatStore.hasTarget();
        if(hasTarget) {
            return false;
        }
        if(Inventory.isFull()) {
            return false;
        }
        items = LootService.getItemsToLoot();
        return items != null && items.length > 0;
    }

    @Override
    public void execute() {
        if(items != null && items.length > 0) {
            for (Pickable item : items) {
                if(item != null) {
                    int count = Inventory.getCount(item.getId());
                    if(!item.interact("Take")) {
                        continue;
                    }
                    Time.sleep(100, 250);
                    if(Players.getLocal().isMoving()) {
                        Time.sleepUntil(() -> !Players.getLocal().isMoving(), 10000);
                    }
                    Time.sleepUntil(() -> Inventory.getCount(item.getId()) != count, 2500);
                }
            }
        }
    }

    @Override
    public void onInvalid() {
        items = null;
        super.onInvalid();
    }

    @Override
    public String status() {
        return "Looting";
    }
}
