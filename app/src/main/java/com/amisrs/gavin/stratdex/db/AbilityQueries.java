package com.amisrs.gavin.stratdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amisrs.gavin.stratdex.model.Ability;

import java.util.ArrayList;

/**
 * Created by Gavin on 30/09/2016.
 */
public class AbilityQueries {
    private SQLiteDatabase db;
    private DexSQLHelper dsh;

    public AbilityQueries(Context context) {
        dsh = new DexSQLHelper(context);
    }

    private void open() {
        db = dsh.getWritableDatabase();
    }

    private void close() {
        dsh.close();
        db.close();
    }

    public void addAbility(Ability ability) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.AbilityTable.COLUMN_NAME_URL, ability.getUrl());
        contentValues.put(DexContract.AbilityTable.COLUMN_NAME_NAME, ability.getName());

        long newRowId = db.insert(DexContract.AbilityTable.TABLE_NAME, null, contentValues);
        System.out.println("added ability " + ability.getName() + " to database");
        close();
    }

    public void addDescToAbility(String id, String desc) {
        open();
        String updateString = "UPDATE " + DexContract.AbilityTable.TABLE_NAME + " SET " +
                DexContract.AbilityTable.COLUMN_NAME_DESC + " = \"" + desc + "\" WHERE " +
                DexContract.AbilityTable.COLUMN_NAME_ID  + " = " + id;
        db.execSQL(updateString);
        System.out.println("added desc " + desc + " to ability id " + id);
        close();
    }

    public void linkAbilityToPokemon(int pid, int aid) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.AbilityForPokemonTable.COLUMN_NAME_POKEMON_ID, pid);
        contentValues.put(DexContract.AbilityForPokemonTable.COLUMN_NAME_ABILITY_ID, aid);

        long newRowID = db.insert(DexContract.AbilityForPokemonTable.TABLE_NAME, null, contentValues);
        close();

    }

    public ArrayList<Ability> getAbilitiesForPokemon(int pid) {
        open();
        ArrayList<Ability> retval = new ArrayList<>();
        String query = "SELECT " + "a."+ DexContract.AbilityTable.COLUMN_NAME_ID + ", " +
                                   "a."+ DexContract.AbilityTable.COLUMN_NAME_DESC + ", " +
                                   "a."+ DexContract.AbilityTable.COLUMN_NAME_URL + ", " +
                                   "a."+ DexContract.AbilityTable.COLUMN_NAME_NAME +
                       " FROM " + DexContract.AbilityTable.TABLE_NAME + " a" +
                       " JOIN " + DexContract.AbilityForPokemonTable.TABLE_NAME + " b" +
                       " ON " + "a." + DexContract.AbilityTable.COLUMN_NAME_ID + " = " +
                                "b." + DexContract.AbilityForPokemonTable.COLUMN_NAME_ABILITY_ID +
                       " WHERE " + "b." + DexContract.AbilityForPokemonTable.COLUMN_NAME_POKEMON_ID + " = ?"+
                       ";";
        Cursor c = db.rawQuery(query, new String[]{Integer.toString(pid)});
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Ability ability = new Ability();
            ability.setUrl(c.getString(2));
            ability.setDesc(c.getString(1));
            ability.setName(c.getString(3));
            ability.setIdFromUrl();
            retval.add(ability);
            c.moveToNext();
        }
        c.close();
        close();
        return retval;
    }
}
