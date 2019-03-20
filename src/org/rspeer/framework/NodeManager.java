package org.rspeer.framework;

import org.rspeer.debug.Logger;

import java.util.concurrent.CopyOnWriteArraySet;

public class NodeManager {

    private CopyOnWriteArraySet<Node> nodes;
    private Node active;

    public NodeManager() {
        this.nodes = new CopyOnWriteArraySet<>();
    }

    public NodeManager submit(Node node) {
        nodes.add(node);
        return this;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public Node getActive() {
        return active;
    }

    public void onScriptStop() {
        for (Node node : nodes) {
            node.onScriptStop();
        }
    }

    public int execute(int loop) {
        for (Node node : nodes) {
            if(!node.validate())
                continue;
            if(active != null && !node.equals(active)) {
                Logger.debug("Node has changed.");
                active.onInvalid();
            }
            active = node;
            node.execute();
            return loop;
        }
        active = null;
        return loop;
    }
}
