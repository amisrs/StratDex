package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 30/09/2016.
 */
public class AbilityContainer {
    private int slot;
    private Ability ability;

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
