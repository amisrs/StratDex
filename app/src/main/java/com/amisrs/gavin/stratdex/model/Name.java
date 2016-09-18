package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 18/09/2016.
 */
public class Name {
    private String name;
    private Language language;

    public String getName() {
        return name;
    }

    public Language getLanguage() {
        return language;
    }

     public static class Language {
        private String name;

        public String getName() {
            return name;
        }
    }
}
