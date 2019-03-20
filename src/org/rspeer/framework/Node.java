package org.rspeer.framework;

public abstract class Node {

    public abstract boolean validate();

    public abstract void execute();

    public void onInvalid() {

    }

    public void onScriptStop() {

    }

    public abstract String status();
}
