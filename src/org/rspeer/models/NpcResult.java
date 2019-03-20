package org.rspeer.models;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.scene.Npcs;

public class NpcResult {

    private int npc;
    private boolean isInteractable;

    public NpcResult(Npc npc, boolean isInteractable) {
        this.npc = npc.getIndex();
        this.isInteractable = isInteractable;
    }

    public Npc getNpc() {
        return Npcs.getAt(npc);
    }

    public boolean isInteractable() {
        return isInteractable;
    }
}
