package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 27/09/2016.
 */
public class DetailsFromSpecies {
    private String id;
    private Color color;
    private EvolutionChain evolution_chain;



    public Color getColor() {
        return color;
    }

    public EvolutionChain getEvolution_chain() {
        return evolution_chain;
    }

    private class EvolutionChain {
        private String url;

        public String getUrl() {
            return url;
        }
    }
}
