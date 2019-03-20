package org.rspeer.nodes.combat;

import org.rspeer.config.Config;
import org.rspeer.debug.Logger;
import org.rspeer.framework.Node;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;

public class BackToFightZone extends Node {

    private Position startTileRandom;
    private int distRandom;

    @Override
    public boolean validate() {
        return Players.getLocal().distance(Config.getStartingTile()) > Config.getRadius();
    }

    @Override
    public void execute() {
        if(startTileRandom == null) {
            startTileRandom = Config.getStartingTile().randomize(3);
            distRandom = Random.nextInt(1, 4);
        }
        if(Movement.getDestinationDistance() >= distRandom) {
            Logger.debug("Walking to: " + startTileRandom.toString());
            Movement.walkTo(startTileRandom);
            Time.sleep(200, 450);
        }
    }

    @Override
    public void onInvalid() {
        Logger.debug("Disposing back to fight zone.");
        startTileRandom = null;
        distRandom = -1;
    }

    @Override
    public String status() {
        return "Walking back to fight zone.";
    }
}
