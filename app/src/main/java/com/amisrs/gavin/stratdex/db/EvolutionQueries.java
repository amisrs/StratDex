package com.amisrs.gavin.stratdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amisrs.gavin.stratdex.model.EvolutionChain;
import com.amisrs.gavin.stratdex.model.EvolutionObject;

/**
 * Created by Gavin on 3/10/2016.
 */
public class EvolutionQueries {
    private SQLiteDatabase db;
    private DexSQLHelper dsh;
    private static final String TAG = "EvolutionQueries";

    public EvolutionQueries(Context context) {
        dsh = new DexSQLHelper(context);
    }

    public void addChain(EvolutionChain evolutionChain) {
        int id = evolutionChain.getId();
        evolutionChain.getChain().getSpecies().setIdFromUrl();
        Log.d(TAG, "This chain starts with " + evolutionChain.getChain().getSpecies().getName() + " ID: " + evolutionChain.getChain().getSpecies().getId());

        int objectId = Integer.parseInt(evolutionChain.getChain().getSpecies().getId());

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.EvolutionChainTable.COLUMN_NAME_ID, id);
        contentValues.put(DexContract.EvolutionChainTable.COLUMN_NAME_OBJECT, objectId);

        long newRowId = db.insert(DexContract.EvolutionChainTable.TABLE_NAME, null, contentValues);
        close();
    }

    public void addObject(EvolutionObject evolutionObject) {
        int id = Integer.parseInt(evolutionObject.getSpecies().getId());
        int level = 0;
        if(evolutionObject.getEvolution_details().length <= 0) {
            Log.i(TAG, "evolutionObject has no level; it is probably the base then.");
        } else {
            level = evolutionObject.getEvolution_details()[0].getMin_level();
        }
        int pid = Integer.parseInt(evolutionObject.getSpecies().getId());
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.EvolutionObjectTable.COLUMN_NAME_ID, id);
        contentValues.put(DexContract.EvolutionObjectTable.COLUMN_NAME_LEVEL, level);
        contentValues.put(DexContract.EvolutionObjectTable.COLUMN_NAME_POKEMON_ID, pid);

        long newRowId = db.insert(DexContract.EvolutionObjectTable.TABLE_NAME, null, contentValues);
        close();
    }

    public void addFromToAssociation(EvolutionObject from, EvolutionObject to) {
        from.getSpecies().setIdFromUrl();
        to.getSpecies().setIdFromUrl();
        int fromId = Integer.parseInt(from.getSpecies().getId());
        int toId = Integer.parseInt(to.getSpecies().getId());
        Log.d(TAG, "Associating FROM: " + fromId + " and NEXT: " + toId);

        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.EvolutionArrayTable.COLUMN_NAME_BASE, fromId);
        contentValues.put(DexContract.EvolutionArrayTable.COLUMN_NAME_NEXT, toId);

        long newRowId = db.insert(DexContract.EvolutionArrayTable.TABLE_NAME, null, contentValues);
        close();
    }

    //TODO: GET THE DATA

    public void open() {
        db = dsh.getWritableDatabase();
    }

    public void close() {
        dsh.close();
        db.close();
    }
}

