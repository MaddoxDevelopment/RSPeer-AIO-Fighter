package org.rspeer;

import org.rspeer.backgroundTasks.TargetChecker;
import org.rspeer.config.Config;
import org.rspeer.config.ProgressiveSet;
import org.rspeer.debug.LogLevel;
import org.rspeer.framework.BackgroundTaskExecutor;
import org.rspeer.framework.NodeManager;
import org.rspeer.models.Progressive;
import org.rspeer.nodes.combat.CombatListener;
import org.rspeer.paint.CombatPaintRenderer;
import org.rspeer.paint.ScriptPaint;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.EquipmentSlot;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Projection;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.listeners.DeathListener;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.listeners.TargetListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.DeathEvent;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.TargetEvent;
import org.rspeer.runetek.providers.subclass.GameCanvas;
import org.rspeer.script.GameAccount;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

@ScriptMeta(name = "Pro Fighter", desc = "RSPeer Official AIO Fighter", developer = "MadDev", category = ScriptCategory.COMBAT)
public class Fighter extends Script implements RenderListener, TargetListener, DeathListener, ChatMessageListener {

    private NodeManager manager;
    private ScriptPaint paint;
    private StopWatch runtime;

    public static int getLoopReturn() {
        return Random.high(200, 450);
    }

    public StopWatch getRuntime() {
        return runtime;
    }

    public NodeManager getManager() {
        return manager;
    }

    public NodeSupplier supplier;

    public NodeSupplier getSupplier() {
        return supplier;
    }

    @Override
    public void onStart() {
        try {
            super.onStart();
            Progressive progressive = new Progressive();
            progressive.setName("Chickens");
            HashMap<EquipmentSlot, String> map = new HashMap<>();
            map.put(EquipmentSlot.MAINHAND, "Mithril scimitar");
            map.put(EquipmentSlot.OFFHAND, "Iron kiteshield");
            map.put(EquipmentSlot.LEGS, "Iron platelegs");
            map.put(EquipmentSlot.CHEST, "Iron platebody");
            map.put(EquipmentSlot.HEAD, "Iron full helm");
            map.put(EquipmentSlot.NECK, "Amulet of strength");
            map.put(EquipmentSlot.CAPE, "Team-17 cape");


            progressive.setEquipmentMap(map);
            progressive.setStyle(Combat.AttackStyle.ACCURATE);
            progressive.setSkill(Skill.ATTACK);
            progressive.setMinimumLevel(1);
            HashSet<String> enemies = new HashSet<>();
            enemies.add("cow");
            enemies.add("cow calf");
            progressive.setEnemies(enemies);
            HashSet<String> loot = new HashSet<>();
            loot.add("cowhide");
            //loot.add("bones");
            progressive.setLoot(loot);
            progressive.setRadius(15);
            progressive.setBuryBones(true);
            progressive.setPrioritizeLooting(true);
            progressive.setPosition(new Position(3032, 3305));
            progressive.setRandomIdle(true);
            progressive.setRandomIdleBuffer(52);
            ProgressiveSet.add(progressive);
            Config.setLogLevel(LogLevel.All);
            supplier = new NodeSupplier();
            manager = new NodeManager();
            setupNodes();
            runtime = StopWatch.start();
            paint = new ScriptPaint(this);
            setBackgroundTasks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupNodes() {
        manager
                .submit(supplier.EAT)
                .submit(supplier.GET_FOOD)
                .submit(supplier.DEPOSIT_LOOT)
                .submit(supplier.LOOT)
                .submit(supplier.PROGRESSION_CHECKER)
                .submit(supplier.BURY_BONES)
                .submit(supplier.IDLE)
                .submit(supplier.FIGHT)
                .submit(supplier.BACK_TO_FIGHT);
    }

    private void setBackgroundTasks() {
        new TargetChecker();
    }

    @Override
    public int loop() {
        if(!GameCanvas.isInputEnabled()) {
            GameCanvas.setInputEnabled(true);
        }
        try {
            return manager.execute(getLoopReturn());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getLoopReturn();
    }

    @Override
    public void onStop() {
        try {
            BackgroundTaskExecutor.shutdown();
            manager.onScriptStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void notify(RenderEvent e) {
        Graphics g = e.getSource();
        try {
            if(manager != null) {
                paint.notify(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        g.setColor(Color.GREEN);

        Position p = Config.getStartingTile();
        if(p != null && Game.isLoggedIn()) {
            Point start = Projection.toMinimap(p);
            if (start != null) {
                int size = Config.getRadius() * 4;
                g.drawOval(start.x - (size / 2), start.y - (size / 2), size, size);
            }
            CombatPaintRenderer.onRenderEvent(g);
        }
    }

    @Override
    public void notify(TargetEvent e) {
        CombatListener.onTargetEvent(e);
    }

    @Override
    public void notify(DeathEvent e) {
        CombatListener.onDeathEvent(e, supplier);
    }

    @Override
    public void notify(ChatMessageEvent e) {
        CombatListener.onChatMessage(e);
    }

}
