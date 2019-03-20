package org.rspeer.nodes.idle;

import org.rspeer.config.Config;
import org.rspeer.framework.Node;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.ui.Log;

import java.util.concurrent.TimeUnit;

public class IdleNode extends Node {

    private int max;
    private int kills;
    private long idleTill;

    public void onTargetKill() {
        kills++;
    }

    public int getKills() {
        return kills;
    }

    public int getMax() {
        return max;
    }

    public long getIdleFor() {
        if(idleTill == 0) {
            return 0;
        }
        return TimeUnit.MILLISECONDS.toSeconds(idleTill - System.currentTimeMillis());
    }

    private boolean isIdling() {
       return getIdleFor() > 0;
    }

    @Override
    public boolean validate() {
        if(!Config.getProgressive().isRandomIdle()) {
            return false;
        }
        if(isIdling()) {
            return true;
        }
        if(max == 0) {
            int buffer = Config.getProgressive().getRandomIdleBuffer();
            max = Random.high(buffer - 6, buffer + 8);
            max = buffer;
        }
        return kills >= max;
    }

    @Override
    public void execute() {
        if(idleTill == 0) {
            idleTill = System.currentTimeMillis() + Random.high(20000, 180000);
            return;
        }
        long timeout = getIdleFor();
        if(timeout > 60 && Game.isLoggedIn()) {
            Log.fine("Logging out....");
            Game.logout();
            Time.sleep(200, 500);
        }
        if(timeout > 0) {
            Log.fine("Idling for " + getIdleFor() + " seconds.");
            return;
        }
        max = 0;
        kills = 0;
    }

    @Override
    public void onInvalid() {
        if(!isIdling()) {
            kills = 0;
            max = 0;
            idleTill = 0;
        }
        super.onInvalid();
    }

    @Override
    public String status() {
        return "Idling";
    }
}
