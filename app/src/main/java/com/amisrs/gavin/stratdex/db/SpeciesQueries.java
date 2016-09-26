package com.amisrs.gavin.stratdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.amisrs.gavin.stratdex.MainActivity;
import com.amisrs.gavin.stratdex.model.Name;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Gavin on 15/09/2016.
 */
public class SpeciesQueries {
    private SQLiteDatabase db;
    private DexSQLHelper dsh;


    public SpeciesQueries(Context context) {
        dsh = new DexSQLHelper(context);
    }

    public void addSpecies(String url, String name, String type1, String type2) {
        open();
        PokemonSpecies dummy = new PokemonSpecies(url, name, type1, type2);

        dummy.setIdFromUrl();

        //InputStream in = null;
        //new DownloadImageAsync().execute(dummy.getSpriteString(), url, name, type1, type2);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_URL, dummy.getUrl());
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_NAME, dummy.getName());
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_TYPE1, dummy.getType1());
        contentValues.put(DexContract.PokemonTable.COLUMN_NAME_TYPE2, dummy.getType2());
        long newRowId = db.insert(DexContract.PokemonTable.TABLE_NAME, null, contentValues);

        //Bitmap letssee = dummy.fillDetailsWithRequests();
        //System.out.println("smallsprite = " + dummy.getSmallSprite());


        System.out.println("adding to database: url = " + url + " name = " + name);
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
            System.out.println("cursorcount = " + c.getCount());
            System.out.println("getting from database: url = " + c.getString(1) + " name = " + c.getString(2));

            //Bitmap smallSpriteBitmap = DbBitmapUtility.getImage(c.getBlob(3));
            PokemonSpecies newPkmn = new PokemonSpecies(c.getString(2), c.getString(1), c.getString(4), c.getString(5));
            newPkmn.setIdFromUrl();
            //newPkmn.setSmallSprite(smallSpriteBitmap);
            pokemonSpecies.add(newPkmn);
            c.moveToNext();
        }
        c.close();
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


    class DownloadImageAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            Bitmap pic = null;

            //try {
//                System.out.println("opening input stream to get sprite for " + strings[2]);
//                InputStream in = new URL(strings[0]).openStream();
//                pic = BitmapFactory.decodeStream(in);
//                in.close();


                ContentValues contentValues = new ContentValues();
                contentValues.put(DexContract.PokemonTable.COLUMN_NAME_URL, strings[1]);
                contentValues.put(DexContract.PokemonTable.COLUMN_NAME_NAME, strings[2]);
                contentValues.put(DexContract.PokemonTable.COLUMN_NAME_TYPE1, strings[3]);
                contentValues.put(DexContract.PokemonTable.COLUMN_NAME_TYPE2, strings[4]);



//                byte[] spriteBlob = DbBitmapUtility.getBytes(pic);
//                contentValues.put(DexContract.PokemonTable.COLUMN_NAME_SPRITE, spriteBlob);
//                open();
//                long newRowId = db.insert(DexContract.PokemonTable.TABLE_NAME, null, contentValues);
//                close();
                //System.out.println("inside async...got sprite as bitmap for " + name);

//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
           // smallSprite = pic;
            //System.out.println("picture gotten for " + name + "smallsprite = " + smallSprite.getByteCount());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity.dataSetChange();

        }
    }
}
