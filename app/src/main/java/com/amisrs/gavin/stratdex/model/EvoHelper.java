package com.amisrs.gavin.stratdex.model;

import android.content.Context;
import android.util.Log;

import com.amisrs.gavin.stratdex.db.EvolutionQueries;

/**
 * Created by Gavin on 3/10/2016.
 */
public class EvoHelper {
    private static final String TAG = "EvoHelper";
    EvolutionQueries evolutionQueries;

    public EvoHelper(Context context) {
        evolutionQueries = new EvolutionQueries(context);
    }
    // this  is stupid
    public void readWholeChainIntoDatabase(EvolutionChain evolutionChain) {
        evolutionQueries.addChain(evolutionChain);
        readObject(evolutionChain.getChain());
    }

    public void readObject(EvolutionObject evolutionObject) {

        evolutionQueries.addObject(evolutionObject);

        // i want to get off mr bones wild ride
        if(evolutionObject.getEvolves_to().length <= 0) {
            Log.d(TAG, "ah i am the final evo, " + evolutionObject.getSpecies().getName() + ", gonna stop after this one");
        } else {
            for(int i=0; i < evolutionObject.getEvolves_to().length; i++) {
                evolutionQueries.addFromToAssociation(evolutionObject, evolutionObject.getEvolves_to()[i]);
                readObject(evolutionObject.getEvolves_to()[i]);
            }
        }
    }

}
