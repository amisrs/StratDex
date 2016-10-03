package com.amisrs.gavin.stratdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amisrs.gavin.stratdex.model.EvolutionChain;
import com.amisrs.gavin.stratdex.model.EvolutionObject;

import java.util.ArrayList;

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
        contentValues.put(DexContract.EvolutionArrayTable.COLUMN_NAME_GROWN, toId);

        long newRowId = db.insert(DexContract.EvolutionArrayTable.TABLE_NAME, null, contentValues);
        close();
    }

    //TODO: GET THE DATA

    public ArrayList<EvoSlot> getSimpleChain(int pid) {
        int baseTier = 5;
        // get all rows with base = this; means u got all the ones this evolves to
        // get all rows with next = this; all the ones that evolved to this

        // use 2darray? second slot is tier in the hierarchy
        // [bulbasaur] [ivysaur] [venusaur]
        // [1] [2] [3]

        // [eevee] [vaporeon] [flareon] [leafeon] [etc]
        // [1] [2] [2] [2] [2] ...

        //step 1: get base = this;
        open();
        ArrayList<EvoSlot> evoSlots = new ArrayList<>();

        //dont forget to add yourself
        evoSlots.add(new EvoSlot(pid, baseTier));

        String query = "SELECT " + DexContract.EvolutionArrayTable.COLUMN_NAME_GROWN + "," + DexContract.EvolutionArrayTable.COLUMN_NAME_BASE +
                       " FROM " + DexContract.EvolutionArrayTable.TABLE_NAME +
                       " WHERE " + DexContract.EvolutionArrayTable.COLUMN_NAME_BASE + " =  ?;";


        String query2 = "SELECT " + DexContract.EvolutionArrayTable.COLUMN_NAME_BASE + "," + DexContract.EvolutionArrayTable.COLUMN_NAME_GROWN +
                " FROM " + DexContract.EvolutionArrayTable.TABLE_NAME +
                " WHERE " + DexContract.EvolutionArrayTable.COLUMN_NAME_GROWN + " =  ?;";


        Cursor c = db.rawQuery(query, new String[]{Integer.toString(pid)});
        if(c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Log.d(TAG, "Found a pokemon that this evolves to, adding it to tier " + (baseTier + 1));
                evoSlots.add(new EvoSlot(c.getInt(0), baseTier + 1));

                //should put this whole thing in a function so no repeat, but works for now
                Cursor cnest = db.rawQuery(query, new String[]{Integer.toString(c.getInt(0))});
                if(cnest != null && c.getCount() > 0) {
                    cnest.moveToFirst();
                    while(!cnest.isAfterLast()) {
                        evoSlots.add(new EvoSlot(cnest.getInt(0), baseTier + 2));
                        cnest.moveToNext();
                    }
                }
                cnest.close();
                c.moveToNext();
            }
        } else {
            Log.d(TAG, "The first cursor was null?");
        }
        c.close();


        Cursor c2 = db.rawQuery(query2, new String[]{Integer.toString(pid)});

        if(c2 != null && c2.getCount() > 0) {
            c2.moveToFirst();
            while (!c2.isAfterLast()) {
                Log.d(TAG, "results in, we have column numbers " + c2.getColumnCount());
                Log.d(TAG, "Found a pokemon that evolves to this, adding it to tier " + (baseTier - 1));
                int evoID = c2.getInt(0);

                evoSlots.add(new EvoSlot(evoID, baseTier - 1));

                Cursor c2nest = db.rawQuery(query2, new String[]{Integer.toString(c2.getInt(0))});
                if(c2nest != null && c2nest.getCount() > 0) {
                    c2nest.moveToFirst();
                    while(!c2nest.isAfterLast()) {
                        evoSlots.add(new EvoSlot(c2nest.getInt(0), baseTier - 2));
                        c2nest.moveToNext();
                    }
                }
                c2nest.close();

                c2.moveToNext();
            }
        } else {
            Log.d(TAG, "The second cursor was null");
        }
        c2.close();

        close();
        return evoSlots;
    }

    public void open() {
        db = dsh.getWritableDatabase();
    }

    public void close() {
        dsh.close();
        db.close();
    }
}

