package org.rspeer.config;

import org.rspeer.models.Progressive;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Players;

import java.util.HashSet;

public class ProgressiveSet {

    private static HashSet<Progressive> progressives = new HashSet<>();
    private static Progressive current;

    public static void add(Progressive progressive) {
        progressives.add(progressive);
    }

    public static Progressive getCurrent() {
        return current;
    }

    public static void setCurrent(Progressive current) {
        ProgressiveSet.current = current;
    }

    public static Progressive getBest() {
        if(Players.getLocal() == null || !Game.isLoggedIn()) {
            return null;
        }
        Progressive best = null;
        for (Progressive progressive : progressives) {
            int level = Skills.getLevel(progressive.getSkill());
            if(level >= progressive.getMinimumLevel() && level < progressive.getMaximumLevel()) {
                best = progressive;
            }
        }
        return best;
    }
}
