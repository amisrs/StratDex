package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 18/09/2016.
 */
public class TypeContainer {
    private String slot;
    private Type type;

    public TypeContainer(Type type, String slot) {
        this.type = type;
        this.slot = slot;
    }

    public String getSlot() {
        return slot;
    }

    public Type getType() {
        return type;
    }

    public class Type {
        private String name;

        public Type(String name) {
            this.name = name;
        }

        public String getName() {
            String capName = name;
            capName = name.replaceFirst(".", Character.toUpperCase(name.charAt(0))+"");
            if(capName.contains("-")) {
                int hyphenIndex = capName.indexOf("-");
                capName = capName.replaceFirst("-.", "-"+Character.toUpperCase(capName.charAt(hyphenIndex+1))+"");
            }

            return capName;
        }


    }
}
