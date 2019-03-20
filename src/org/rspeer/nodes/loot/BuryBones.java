package org.rspeer.nodes.loot;

import org.rspeer.config.Config;
import org.rspeer.framework.Node;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;

public class BuryBones extends Node {

    private Item[] bones;

    @Override
    public boolean validate() {
        if(!Config.buryBones()) {
            return false;
        }
        bones = Inventory.getItems(p ->
                p.getName().toLowerCase().contains("bones") && p.containsAction("Bury"));
        return bones.length > 0;
    }

    @Override
    public void execute() {
        if(bones == null) {
            return;
        }
        for (Item bone : bones) {
            bone.click();
            Time.sleep(100, 350);
        }
    }

    @Override
    public String status() {
        return "Burying Bones";
    }
}
