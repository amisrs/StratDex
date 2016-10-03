package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 3/10/2016.
 */
public class EvolutionObject {
    private EvolutionObject[] evolves_to;
    private PokemonSpecies species;
    private EvolutionDetails[] evolution_details;

    public EvolutionDetails[] getEvolution_details() {
        return evolution_details;
    }

    public EvolutionObject[] getEvolves_to() {
        return evolves_to;
    }

    public PokemonSpecies getSpecies() {
        return species;
    }

    public class EvolutionDetails {
        private int min_level;

        public int getMin_level() {
            return min_level;
        }

        public void setMin_level(int min_level) {
            this.min_level = min_level;
        }
    }
}
