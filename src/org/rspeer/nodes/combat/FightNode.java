package org.rspeer.nodes.combat;

import org.rspeer.CombatStore;
import org.rspeer.config.Config;
import org.rspeer.debug.Logger;
import org.rspeer.framework.BackgroundTaskExecutor;
import org.rspeer.framework.Node;
import org.rspeer.models.NpcResult;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.PathingEntity;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.services.LootService;
import org.rspeer.wrappers.CombatWrapper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FightNode extends Node {

    private NpcResult result;
    private String status;
    private boolean running;

    public FightNode() {
        BackgroundTaskExecutor.submit(this::findNextTarget, 1000);
    }

    @Override
    public boolean validate() {
        NpcResult target = CombatStore.getCurrentTarget();
        if(target != null) {
            return true;
        }
        if(Config.getProgressive().isPrioritizeLooting()) {
            //Item to loot, return.
            System.out.println("Prioritizing looting");
            if(LootService.getItemsToLoot().length > 0) {
                System.out.println("Found item to loot");
                return false;
            }
        }
        status = "Looking for target.";
        NpcResult res = CombatWrapper.findTarget(false);
        if(res == null) {
            status = "No targets around me, waiting...";
            return false;
        }
        Logger.fine("New Target Index: " + res.getNpc().getIndex());
        result = res;
        doAttack(result.getNpc());
        return true;
    }

    @Override
    public void execute() {
        running = true;
        if(result != null && !CombatStore.hasTarget()) {
            doAttack(result.getNpc());
            return;
        }
        NpcResult target = CombatStore.getCurrentTarget();
        if(CombatWrapper.isDead(target.getNpc())) {
            status = "Target has died.";
            CombatStore.setCurrentTarget(null);
            return;
        }
        if(!CombatWrapper.isTargetingMe(target.getNpc())) {
            Logger.debug("Our current target is not targeting me.");
            if(CombatStore.getTargetingMe().size() > 0) {
               status = "Switching to target that is targeting me.";
               Npc first = CombatStore.getTargetingMe().stream().filter(n -> {
                   PathingEntity npcsTarget = n.getTarget();
                   return npcsTarget != null && npcsTarget.equals(Players.getLocal()) && n.getIndex() != target.getNpc().getIndex();
               }).findFirst().orElse(null);
               if(first == null) {
                   Logger.debug("Targeting me first is null, grabbing next.");
                   Npc next = CombatStore.getTargetingMe().iterator().next();
                   CombatStore.setCurrentTarget(new NpcResult(next, true));
                   doAttack(next);
               } else {
                   Logger.debug("Changing target to: " + first.getIndex());
                   CombatStore.setCurrentTarget(new NpcResult(first, true));
                   doAttack(first);
               }
               return;
            }
            doAttack(target.getNpc());
        }
    }

    @Override
    public void onInvalid() {
        running = false;
        super.onInvalid();
    }

    @Override
    public void onScriptStop() {
        super.onScriptStop();
    }

    @Override
    public String status() {
        return status;
    }

    private void findNextTarget() {
        if(!running)
            return;
        CombatStore.setNextTarget(CombatWrapper.findTarget(true));
    }

    private void doAttack(Npc npc) {
        Player p = Players.getLocal();
        PathingEntity target = p.getTarget();
        PathingEntity targetsTarget = target == null ? null : target.getTarget();
        if(p.getTargetIndex() != -1 && target != null && targetsTarget != null && targetsTarget.equals(p)) {
            System.out.println("In combat.");
            return;
        }
        if(Movement.isInteractable(npc, false)) {
            status = "Attacking " + npc.getName() + " (" + npc.getIndex() + ").";
            Logger.debug("Attacking target: " + npc.getIndex());
            npc.interact("Attack");
            Time.sleepUntil(() -> Players.getLocal().getTargetIndex() > 0, 1500);
            return;
        }
        status = "Walking to target.";
        Movement.walkTo(npc);
    }
}
