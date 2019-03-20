package org.rspeer.nodes.progressive;

import org.rspeer.config.ProgressiveSet;
import org.rspeer.framework.BackgroundTaskExecutor;
import org.rspeer.framework.Node;
import org.rspeer.models.Progressive;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.EquipmentSlot;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressionChecker extends Node {

    private Progressive progressive;

    public ProgressionChecker() {
        BackgroundTaskExecutor.submit(() -> ProgressiveSet.setCurrent(ProgressiveSet.getBest()), 1000);
    }

    private List<Item> toSwitch;

    private List<Item> checkEquipmentToSwitch() {
        List<Item> indexes = new ArrayList<>();
        HashMap<EquipmentSlot, String> map = progressive.getEquipmentMap();
        for (Map.Entry<EquipmentSlot, String> entry : map.entrySet()) {
            String equipment = entry.getValue();
            if (equipment == null) {
                continue;
            }
            String name = entry.getKey().getItemName();
            if (name == null || !name.equals(equipment)) {
                Item inv = Inventory.getFirst(equipment);
                if (inv == null) {
                    continue;
                }
                indexes.add(inv);
            }
        }
        return indexes;
    }


    @Override
    public boolean validate() {
        progressive = ProgressiveSet.getCurrent();
        if (progressive == null) {
            return false;
        }
        Combat.AttackStyle style = Combat.getAttackStyle();
        if (style != null && !progressive.getStyle().equals(style)) {
            return true;
        }
        toSwitch = checkEquipmentToSwitch();
        return toSwitch.size() > 0;
    }

    @Override
    public void execute() {
        Combat.AttackStyle style = Combat.getAttackStyle();
        if (!progressive.getStyle().equals(style)) {
            Combat.WeaponType type = Combat.getWeaponType();
            Combat.AttackStyle[] possibleStyles = type.getAttackStyles();
            for (int i = 0; i < type.getAttackStyles().length; i++) {
                if(possibleStyles[i].equals(progressive.getStyle())) {
                    Combat.select(i);
                    break;
                }
            }
        }
        if (toSwitch == null) {
            return;
        }
        for (Item item : toSwitch) {
            System.out.println("Equipping: " + item.getName());
            item.click();
            Time.sleep(100, 350);
        }
    }

    @Override
    public String status() {
        return "Equipping items.";
    }

    @Override
    public void onScriptStop() {
        super.onScriptStop();
    }
}
