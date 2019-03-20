package org.rspeer.models;

import org.rspeer.runetek.adapter.scene.PathingEntity;

public class Entity {

    private int id;
    private String name;
    private int targetIndex;
    private int index;

    public Entity(int id, String name, int targetIndex, int index) {
        this.id = id;
        this.name = name;
        this.targetIndex = targetIndex;
        this.index = index;
    }

    public static Entity fromPathingEntity(PathingEntity entity) {
        return new Entity(entity.getId(), entity.getName(), entity.getTargetIndex(), entity.getIndex());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entity) {
            Entity cast = (Entity) obj;
            return cast.id == id && cast.index == index && cast.name.equals(name);
        }
        return super.equals(obj);
    }


}
