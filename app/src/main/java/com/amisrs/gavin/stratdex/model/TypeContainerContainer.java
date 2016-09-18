package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 18/09/2016.
 */
public class TypeContainerContainer {
    public TypeContainer[] types;

    public TypeContainerContainer(TypeContainer[] typeContainers) {
        this.types = typeContainers;
    }

    public TypeContainer[] getTypeContainers() {
        return types;
    }
}
