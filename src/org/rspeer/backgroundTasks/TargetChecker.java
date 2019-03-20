package org.rspeer.backgroundTasks;

import org.rspeer.CombatStore;
import org.rspeer.config.Config;
import org.rspeer.framework.BackgroundTaskExecutor;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.wrappers.CombatWrapper;

public class TargetChecker {

    public TargetChecker() {
        BackgroundTaskExecutor.submit(checkTargetHealth, 100);
    }

    private Runnable checkTargetHealth = () -> {
        Npc npc = CombatStore.getCurrentTargetNpc();
        if(npc == null) {
            CombatStore.setCurrentTarget(Config.isLooting() ? null : CombatWrapper.findTarget(false));
            return;
        }
        if(npc.getHealthPercent() <= 0) {
            CombatStore.setCurrentTarget(Config.isLooting() ? null : CombatWrapper.findTarget(false));
        }
    };

}
