package com.amisrs.gavin.stratdex.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gavin on 27/09/2016.
 */
public class DetailsFromSpecies {
    private String id;
    private Color color;
    private EvolutionChain evolution_chain;
    private Generation generation;
    private FlavorTextEntry[] flavor_text_entries;
    private Genus[] genera;

    public FlavorTextEntry[] getFlavor_text_entries() {
        return flavor_text_entries;
    }

    public Generation getGeneration() {
        return generation;
    }

    public Color getColor() {
        return color;
    }

    public EvolutionChain getEvolution_chain() {
        return evolution_chain;
    }

    public class Generation {
        private String url;
        private String name;

        public String getName() {
            String capName = name;
            capName = name.replaceFirst(".", Character.toUpperCase(name.charAt(0))+"");
            if(capName.contains("-")) {
                int hyphenIndex = capName.indexOf("-");
                capName = capName.replaceFirst("-.", "-"+Character.toUpperCase(capName.charAt(hyphenIndex+1))+"");
            }

            return capName;
        }

        public String getUrl() {
            return url;
        }
    }

    public class FlavorTextEntry {
        private GameVersion version;
        private String flavor_text;
        private Language language;

        public Language getLanguage() {
            return language;
        }

        public String getFlavor_text() {
            return flavor_text;
        }

        public GameVersion getVersion() {
            return version;
        }

        public class Language {
            private String name;

            public String getName() {
                return name;
            }
        }

        public class GameVersion {
            private String url;


            public int getVersionNumber() {
                int v = 0;
                Pattern idPattern = Pattern.compile(".+?/version/(.+)*/");
                Matcher idMatcher = idPattern.matcher(url);
                idMatcher.find();
                v = Integer.parseInt(idMatcher.group(1));
                return v;
            }
        }
    }

    public class Genus {
        private String genus;
        private FlavorTextEntry.Language language;

        public String getGenus() {
            return genus;
        }

        public FlavorTextEntry.Language getLanguage() {
            return language;
        }
    }
}
