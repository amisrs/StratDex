package com.amisrs.gavin.stratdex.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gavin on 15/09/2016.
 */
public class DexSQLHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "stratdex.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLES =
            "CREATE TABLE " + DexContract.PokemonTable.TABLE_NAME + "(" +
                    DexContract.PokemonTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.PokemonTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_SPRITE + BLOB_TYPE
                    + ")";

    public static final String SQL_DELETE_TABLES =
            "DROP TABLE IF EXISTS " + DexContract.PokemonTable.TABLE_NAME;

    public DexSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLES);
    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLES);
    }

}


