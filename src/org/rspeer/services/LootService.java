package org.rspeer.services;

import org.rspeer.config.Config;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.scene.Pickables;

public class LootService {

    public static Pickable[] getItemsToLoot() {
        if(Config.getLoot().size() == 0)
            return new Pickable[0];
        return Pickables.getLoaded(s -> Config.getLoot().contains(s.getName().toLowerCase())
                && s.distance() <= Config.getRadius());
    }

}
