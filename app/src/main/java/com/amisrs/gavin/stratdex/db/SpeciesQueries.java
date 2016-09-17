package com.amisrs.gavin.stratdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.amisrs.gavin.stratdex.model.PokemonSpecies;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gavin on 15/09/2016.
 */
public class SpeciesQueries {
    private SQLiteDatabase db;
    private DexSQLHelper dsh;

    public SpeciesQueries(Context context) {
        dsh = new DexSQLHelper(context);
    }

    public void addSpecies(String url, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_URL, url);
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_NAME, name);

        PokemonSpecies dummy = new PokemonSpecies(url, name);
        dummy.setIdFromUrl();
        Bitmap letssee = dummy.fillDetailsWithRequests();
        System.out.println("smallsprite = " + dummy.getSmallSprite());

        boolean hasPic = false;
        while(!hasPic) {
            if (dummy.getSmallSprite() != null) {
                byte[] spriteBlob = DbBitmapUtility.getBytes(dummy.getSmallSprite());
                contentValues.put(DexContract.PokemonTable.COLUMN_NAME_SPRITE, spriteBlob);
                hasPic = true;
            }
        }
        System.out.println("adding to database: url = " + url + " name = " + name);



        long newRowId = db.insert(DexContract.PokemonTable.TABLE_NAME, null, contentValues);
    }

    public ArrayList<PokemonSpecies> getBasicSpecies() {
        open();

        ArrayList<PokemonSpecies> pokemonSpecies = new ArrayList<>();
        String[] projection = {
                DexContract.PokemonTable.COLUMN_NAME_ID,
                DexContract.PokemonTable.COLUMN_NAME_NAME,
                DexContract.PokemonTable.COLUMN_NAME_URL,
                DexContract.PokemonTable.COLUMN_NAME_SPRITE
        };

        Cursor c = db.query(
                DexContract.PokemonTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            System.out.println("cursorcount = " + c.getCount());
            System.out.println("getting from database: url = " + c.getString(1) + " name = " + c.getString(2));

            Bitmap smallSpriteBitmap = DbBitmapUtility.getImage(c.getBlob(3));
            PokemonSpecies newPkmn = new PokemonSpecies(c.getString(2), c.getString(1));
            newPkmn.setIdFromUrl();
            pokemonSpecies.add(newPkmn);
            c.moveToNext();
        }
        c.close();
        close();
        return pokemonSpecies;
    }

    public void dropSpecies() {

    }

    public void open() {
        db = dsh.getWritableDatabase();
    }

    public void close() {
        dsh.close();
    }
}
