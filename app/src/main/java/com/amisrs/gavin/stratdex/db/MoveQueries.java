package com.amisrs.gavin.stratdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amisrs.gavin.stratdex.model.Move;
import com.amisrs.gavin.stratdex.model.PMove;

import java.util.ArrayList;

/**
 * Created by Gavin on 2/10/2016.
 */
public class MoveQueries {
    private SQLiteDatabase db;
    private DexSQLHelper dsh;

    public MoveQueries(Context context) {
        dsh = new DexSQLHelper(context);
    }

    public void addMove(Move move) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.MoveTable.COLUMN_NAME_URL, move.getUrl());
        contentValues.put(DexContract.MoveTable.COLUMN_NAME_NAME, move.getName());

        long newRowId = db.insert(DexContract.MoveTable.TABLE_NAME, null, contentValues);
        System.out.println("added move " + move.getName() + " to database");
        close();
    }

    public void linkMoveToPokemon(int pid, int mid, int level) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DexContract.MoveForPokemonTable.COLUMN_NAME_POKEMON_ID, pid);
        contentValues.put(DexContract.MoveForPokemonTable.COLUMN_NAME_MOVE_ID, mid);
        contentValues.put(DexContract.MoveForPokemonTable.COLUMN_NAME_LEVEL, level);

        long newRowID = db.insert(DexContract.MoveForPokemonTable.TABLE_NAME, null, contentValues);
        close();
    }

    public ArrayList<PMove> getMovesForPokemon(int pid) {
        open();
        ArrayList<PMove> retval = new ArrayList<>();
        String query = "SELECT " + "a."+ DexContract.MoveTable.COLUMN_NAME_ID + ", " +
                "a."+ DexContract.MoveTable.COLUMN_NAME_URL + ", " +
                "a."+ DexContract.MoveTable.COLUMN_NAME_NAME + ", " +
                "b." + DexContract.MoveForPokemonTable.COLUMN_NAME_LEVEL +
                " FROM " + DexContract.MoveTable.TABLE_NAME + " a" +
                " JOIN " + DexContract.MoveForPokemonTable.TABLE_NAME + " b" +
                " ON " + "a." + DexContract.MoveTable.COLUMN_NAME_ID + " = " +
                "b." + DexContract.MoveForPokemonTable.COLUMN_NAME_MOVE_ID +
                " WHERE " + "b." + DexContract.MoveForPokemonTable.COLUMN_NAME_POKEMON_ID + " = ?"+
                ";";
        Cursor c = db.rawQuery(query, new String[]{Integer.toString(pid)});
        c.moveToFirst();
        while(!c.isAfterLast()) {
            PMove pMove = new PMove();
            pMove.initialize();


            pMove.getMove().setUrl(c.getString(1));
            pMove.getMove().setName(c.getString(2));
            pMove.getVersion_group_details()[0].setLevel_learned_at(c.getInt(3));
            retval.add(pMove);
            c.moveToNext();
        }
        c.close();
        close();
        return retval;
    }




    public void open() {
        db = dsh.getWritableDatabase();
    }

    public void close() {
        dsh.close();
        db.close();
    }


}
