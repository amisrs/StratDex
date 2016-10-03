package com.amisrs.gavin.stratdex.controller;

import com.amisrs.gavin.stratdex.model.DetailsFromSpecies;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;

import java.util.ArrayList;

/**
 * Created by Gavin on 15/09/2016.
 */
public interface AsyncResponse {
    void giveFilledPokemon(PokemonSpecies pokemonSpecies);
    void updateLoadingMsg(String msg);
}
