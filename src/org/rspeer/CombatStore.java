package org.rspeer;

import org.rspeer.debug.Logger;
import org.rspeer.models.NpcResult;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.wrappers.CombatWrapper;

import java.util.HashSet;

public class CombatStore {

    private static NpcResult currentTarget;
    private static NpcResult nextTarget;
    private static HashSet<Npc> targetingMe = new HashSet<>();

    public static Npc getCurrentTargetNpc() {
        NpcResult n = currentTarget;
        if(n == null) {
            return null;
        }
        Npc npc = n.getNpc();
        if(npc == null) {
            currentTarget = null;
        }
        return npc;
    }

    public static NpcResult getCurrentTarget() {
        NpcResult n = currentTarget;
        if(n != null && n.getNpc() == null) {
            currentTarget = null;
        }
        return currentTarget;
    }

    public static boolean hasTarget() {
        return getCurrentTarget() != null;
    }


    public static boolean hasNextTaget() {
        return getNextTarget() != null;
    }

    public static void setCurrentTarget(NpcResult currentTarget) {
        CombatStore.currentTarget = currentTarget;
    }

    public static HashSet<Npc> getTargetingMe() {
        return targetingMe;
    }

    public static NpcResult getNextTarget() {
        NpcResult n = nextTarget;
        if(n != null && n.getNpc() == null) {
            nextTarget = null;
        }
        if(n != null && n.getNpc() != null && CombatWrapper.hasTargetNotMe(n.getNpc())) {
            Logger.debug("Next target has another target.");
            nextTarget = null;
        }
        return nextTarget;
    }

    public static void setNextTarget(NpcResult nextTarget) {
        CombatStore.nextTarget = nextTarget;
    }

    public static void addTargetingMe(Npc source) {
        targetingMe.add(source);
    }

    public static void removeTargetingMe(Npc source) {
        targetingMe.remove(source);
    }

}
