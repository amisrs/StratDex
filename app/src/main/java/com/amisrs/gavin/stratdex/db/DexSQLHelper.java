package com.amisrs.gavin.stratdex.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gavin on 15/09/2016.
 * With the help of https://developer.android.com/training/basics/data-storage/databases.html
 *
 */
public class DexSQLHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "stratdex.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = "INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLES =
            "CREATE TABLE " + DexContract.PokemonTable.TABLE_NAME + "(" +
                    DexContract.PokemonTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.PokemonTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_SPRITE + BLOB_TYPE
//                    DexContract.PokemonTable.COLUMN_NAME_BIGNAME + TEXT_TYPE
//                  DexContract.PokemonTable.COLUMN_NAME_NAMESET + INTEGER_TYPE + COMMA_SEP +
//                    "FOREIGN KEY(" + DexContract.PokemonTable.COLUMN_NAME_NAMESET + ") REFERENCES " +
//                    DexContract.PokemonNames.TABLE_NAME + "(" + DexContract.PokemonNames.COLUMN_NAME_ID + ")"
                    + ")";
    /*public static final String SQL_CREATE_TABLE_NAMES =
            "CREATE TABLE " + DexContract.PokemonNames.TABLE_NAME + "(" +
                    DexContract.PokemonNames.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.PokemonNames.COLUMN_NAME_POKEMONID + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonNames.COLUMN_NAME_LANGUAGE + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonNames.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY(" + DexContract.PokemonNames.COLUMN_NAME_POKEMONID + ") REFERENCES " +
                    DexContract.PokemonTable.TABLE_NAME + "(" + DexContract.PokemonTable.COLUMN_NAME_ID + ")"
                    + ")";
    */
    public static final String SQL_DELETE_TABLES =
            "DROP TABLE IF EXISTS " + DexContract.PokemonTable.TABLE_NAME;

//    public static final String SQL_DELETE_TABLE_NAMES =
//            "DROP TABLE IF EXISTS " + DexContract.PokemonNames.TABLE_NAME;

    public DexSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLES);
        //sqLiteDatabase.execSQL(SQL_DELETE_TABLE_NAMES);

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLES);
       // sqLiteDatabase.execSQL(SQL_CREATE_TABLE_NAMES);

    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLES);
      //  sqLiteDatabase.execSQL(SQL_DELETE_TABLE_NAMES);

    }

}


