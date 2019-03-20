package org.rspeer.wrappers;

import org.rspeer.config.Config;
import org.rspeer.CombatStore;
import org.rspeer.debug.Logger;
import org.rspeer.models.NpcResult;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.PathingEntity;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

import java.util.HashSet;

public class CombatWrapper {

    private static boolean check(Npc npc, HashSet<String> names, int radius) {
        Npc current = CombatStore.getCurrentTargetNpc();
        boolean index = current == null || npc.getIndex() != current.getIndex();
        if(current != null) {
            Log.fine("Finding npc who is not index: " + current.getIndex());
        }
        return index && npc.containsAction("Attack")
                && names.contains(npc.getName().toLowerCase()) && npc.distance() <= radius && !hasTargetNotMe(npc)
                && !playerHasAsTarget(npc);
    }

    public static Npc[] getBest(HashSet<String> names, int radius) {
        return Npcs.getSorted((npc, t1)
                        -> (int) (npc.distance() - t1.distance()), npc -> check(npc, names, radius));
    }

    public static NpcResult findTarget(boolean isNext) {
        for (Npc npc : CombatStore.getTargetingMe()) {
            if(npc == null || !Movement.isInteractable(npc, false)) {
                continue;
            }
            return new NpcResult(npc, true);
        }
        NpcResult next = CombatStore.getNextTarget();
        if(next != null) {
            return next;
        }
        Npc[] candiates = CombatWrapper.getBest(Config.getNpcs(), Config.getRadius());
        if (candiates.length == 0) {
            return null;
        }
        for (Npc candiate : candiates) {
            if (!Movement.isInteractable(candiate, false))
                continue;
            return new NpcResult(candiate, true);
        }
        try {
            Npc target = Random.nextElement(candiates);
            if (target != null) {
                return new NpcResult(target, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static boolean isTargetingMe(Npc npc) {
        if(npc == null) {
            return false;
        }
        int target = npc.getTargetIndex();
        if(target == 0 || target == 1) {
            return false;
        }
        PathingEntity t = npc.getTarget();
        return t != null && t.equals(Players.getLocal());
    }

    public static boolean playerHasAsTarget(Npc npc) {
        if(npc == null) {
            return false;
        }
        return Players.getLoaded(n -> n.getTargetIndex() == npc.getIndex()).length > 0;
    }

    public static boolean hasTargetNotMe(Npc npc) {
        if(npc == null) {
            return false;
        }
        int target = npc.getTargetIndex();
        if(target == 0 || target == 1) {
            return false;
        }
        PathingEntity t = npc.getTarget();
        return t != null && !t.equals(Players.getLocal()) && t.getTargetIndex() == npc.getIndex();
    }

    public static boolean isDead(Npc npc) {
        return npc == null || Npcs.getAt(npc.getIndex()) == null || npc.getHealthPercent() == 0;
    }

    public static int getHealthPercent() {
        float curr = Skills.getCurrentLevel(Skill.HITPOINTS);
        float total = Skills.getLevel(Skill.HITPOINTS);
        float percent = curr / total;
        return (int) (percent * 100);
    }

}
