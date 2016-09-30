package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 29/09/2016.
 */
public class DetailsFromPokemon {
    private Stat[] stats;
    private TypeContainer[] types;
    private int id;
    private AbilityContainer[] abilities;

    public int getId() {
        return id;
    }

    public Stat[] getStats() {
        return stats;
    }

    public TypeContainer[] getTypeContainers() {
        return types;
    }

    public AbilityContainer[] getAbilities() {
        return abilities;
    }
}
