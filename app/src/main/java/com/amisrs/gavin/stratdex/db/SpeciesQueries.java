package com.amisrs.gavin.stratdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amisrs.gavin.stratdex.db.DexContract;
import com.amisrs.gavin.stratdex.db.DexSQLHelper;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;

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

    public void addSpecies(String url, String name, String type1, String type2, String spritePath) {
        open();
        PokemonSpecies dummy = new PokemonSpecies(url, name, type1, type2, spritePath);
        //what a dumb way to do this... just make it accept pokemonspecies as param................
        dummy.setIdFromUrl();

        //InputStream in = null;
        //new DownloadImageAsync().execute(dummy.getSpriteString(), url, name, type1, type2);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_URL, dummy.getUrl());
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_NAME, dummy.getName());
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_TYPE1, dummy.getType1());
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_TYPE2, dummy.getType2());
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_SPRITE, dummy.getSpritePath());

        long newRowId = db.insert(DexContract.PokemonTable.TABLE_NAME, null, contentValues);

        //Bitmap letssee = dummy.fillDetailsWithRequests();
        //System.out.println("smallsprite = " + dummy.getSmallSprite());


        System.out.println("adding to database: url = " + url + " name = " + name);
        close();
    }

    public void addSpriteFilePathForSpecies(String id, String spritePath, String spriteType) {
        open();
        String whichColumn = "";
        if(spriteType.equals("small")) {
            whichColumn = DexContract.PokemonTable.COLUMN_NAME_SPRITE;
        } else {
            whichColumn = DexContract.PokemonTable.COLUMN_NAME_BIGSPRITE;
        }
        String updateString = "UPDATE " + DexContract.PokemonTable.TABLE_NAME + " SET " +
                whichColumn + " = \"" + spritePath + "\" WHERE " +
                DexContract.PokemonTable.COLUMN_NAME_ID + " = " + id;
        db.execSQL(updateString);
        System.out.println(spriteType + " sprite for " + id + " has database entry at " + spritePath);
        close();
    }

    public void addDetailsForSpecies(String id, String color,
                                     int s1, int s2, int s3, int s4, int s5, int s6,
                                     String type1, String type2,
                                     int height, int weight,
                                     String desc, String genus,
                                     int evochain) {
        open();
        String updateString = "UPDATE " + DexContract.PokemonTable.TABLE_NAME + " SET " +
                DexContract.PokemonTable.COLUMN_NAME_COLOR + " = \"" + color + "\"," +
                DexContract.PokemonTable.COLUMN_NAME_STAT1 + " = " + s1 + "," +
                DexContract.PokemonTable.COLUMN_NAME_STAT2 + " = " + s2 + "," +
                DexContract.PokemonTable.COLUMN_NAME_STAT3 + " = " + s3 + "," +
                DexContract.PokemonTable.COLUMN_NAME_STAT4 + " = " + s4 + "," +
                DexContract.PokemonTable.COLUMN_NAME_STAT5 + " = " + s5 + "," +
                DexContract.PokemonTable.COLUMN_NAME_STAT6 + " = " + s6 + "," +
                DexContract.PokemonTable.COLUMN_NAME_TYPE1 + " = \"" + type1 + "\"" + "," +
                DexContract.PokemonTable.COLUMN_NAME_TYPE2 + " = \"" + type2 + "\"" + "," +
                DexContract.PokemonTable.COLUMN_NAME_HEIGHT + " = " + height + "," +
                DexContract.PokemonTable.COLUMN_NAME_WEIGHT + " = " + weight + "," +
                DexContract.PokemonTable.COLUMN_NAME_DESC + " = \"" + desc + "\"" + "," +
                DexContract.PokemonTable.COLUMN_NAME_GENUS + " = \"" + genus + "\"" + "," +
                DexContract.PokemonTable.COLUMN_NAME_EVOCHAIN + " = " + evochain +

                " WHERE " +
                DexContract.PokemonTable.COLUMN_NAME_ID + " = " + id;
        db.execSQL(updateString);

        close();

    }

    public ArrayList<PokemonSpecies> getBasicSpecies() {
        ArrayList<PokemonSpecies> pokemonSpecies = new ArrayList<>();
        String[] projection = {
                DexContract.PokemonTable.COLUMN_NAME_ID,
                DexContract.PokemonTable.COLUMN_NAME_NAME,
                DexContract.PokemonTable.COLUMN_NAME_URL,
                DexContract.PokemonTable.COLUMN_NAME_SPRITE,
                DexContract.PokemonTable.COLUMN_NAME_TYPE1,
                DexContract.PokemonTable.COLUMN_NAME_TYPE2
//                DexContract.PokemonTable.COLUMN_NAME_BIGNAME
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
            //Bitmap smallSpriteBitmap = DbBitmapUtility.getImage(c.getBlob(3));
            PokemonSpecies newPkmn = new PokemonSpecies(c.getString(2), c.getString(1), c.getString(4), c.getString(5), c.getString(3));
            newPkmn.setIdFromUrl();
            //newPkmn.setSmallSprite(smallSpriteBitmap);
            pokemonSpecies.add(newPkmn);
            c.moveToNext();
        }
        c.close();
        return pokemonSpecies;
    }

    public PokemonSpecies getOneSpeciesById(String id) {
        open();

        String[] projection = {
                DexContract.PokemonTable.COLUMN_NAME_ID,
                DexContract.PokemonTable.COLUMN_NAME_NAME,
                DexContract.PokemonTable.COLUMN_NAME_URL,
                DexContract.PokemonTable.COLUMN_NAME_SPRITE,
                DexContract.PokemonTable.COLUMN_NAME_TYPE1,
                DexContract.PokemonTable.COLUMN_NAME_TYPE2,
                DexContract.PokemonTable.COLUMN_NAME_BIGSPRITE,
                DexContract.PokemonTable.COLUMN_NAME_COLOR,
                DexContract.PokemonTable.COLUMN_NAME_STAT1,
                DexContract.PokemonTable.COLUMN_NAME_STAT2,
                DexContract.PokemonTable.COLUMN_NAME_STAT3,
                DexContract.PokemonTable.COLUMN_NAME_STAT4,
                DexContract.PokemonTable.COLUMN_NAME_STAT5,
                DexContract.PokemonTable.COLUMN_NAME_STAT6,
                DexContract.PokemonTable.COLUMN_NAME_HEIGHT,
                DexContract.PokemonTable.COLUMN_NAME_WEIGHT,
                DexContract.PokemonTable.COLUMN_NAME_DESC,
                DexContract.PokemonTable.COLUMN_NAME_GENUS,
                DexContract.PokemonTable.COLUMN_NAME_EVOCHAIN

        };

        String selection = DexContract.PokemonTable.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { id };
        Cursor c = db.query(
                DexContract.PokemonTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        c.moveToFirst();
        PokemonSpecies toGet = new PokemonSpecies(c.getString(2), c.getString(1), c.getString(4), c.getString(5), c.getString(3), c.getString(6), c.getString(7)
                , c.getInt(8), c.getInt(9), c.getInt(10), c.getInt(11), c.getInt(12), c.getInt(13), c.getInt(14), c.getInt(15), c.getString(16), c.getString(17), c.getInt(18));
        toGet.setIdFromUrl();
        c.close();
        close();

        return toGet;
    }


    public void open() {
        db = dsh.getWritableDatabase();
    }

    public void close() {
        dsh.close();
        db.close();
    }

}
