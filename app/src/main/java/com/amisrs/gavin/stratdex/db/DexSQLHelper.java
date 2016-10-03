package com.amisrs.gavin.stratdex.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amisrs.gavin.stratdex.model.Ability;

/**
 * Created by Gavin on 15/09/2016.
 * With the help of https://developer.android.com/training/basics/data-storage/databases.html
 *
 */
public class DexSQLHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "stratdex.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLES =
            "CREATE TABLE " + DexContract.PokemonTable.TABLE_NAME + "(" +
                    DexContract.PokemonTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.PokemonTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_SPRITE + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_TYPE1 + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_TYPE2 + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_BIGSPRITE + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_COLOR + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_STAT1 + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_STAT2 + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_STAT3 + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_STAT4 + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_STAT5 + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_STAT6 + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_DESC + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_GENUS + TEXT_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_EVOCHAIN + INTEGER_TYPE + COMMA_SEP +
                    DexContract.PokemonTable.COLUMN_NAME_GENERATION + TEXT_TYPE + COMMA_SEP +
                    " FOREIGN KEY (" +
                        DexContract.PokemonTable.COLUMN_NAME_EVOCHAIN +
                    ") REFERENCES " +
                        DexContract.EvolutionChainTable.TABLE_NAME + "(" + DexContract.EvolutionChainTable.COLUMN_NAME_ID + ")"
            + ")";

    public static final String SQL_CREATE_ABILITYTABLE =
            "CREATE TABLE " + DexContract.AbilityTable.TABLE_NAME + "(" +
                    DexContract.AbilityTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.AbilityTable.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    DexContract.AbilityTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DexContract.AbilityTable.COLUMN_NAME_DESC + TEXT_TYPE
            + ")";

    public static final String SQL_CREATE_ABILITYPOKEMONTABLE =
            "CREATE TABLE " + DexContract.AbilityForPokemonTable.TABLE_NAME + "(" +
                    DexContract.AbilityForPokemonTable.COLUMN_NAME_POKEMON_ID + INTEGER_TYPE + COMMA_SEP +
                    DexContract.AbilityForPokemonTable.COLUMN_NAME_ABILITY_ID + INTEGER_TYPE + COMMA_SEP +
                    " PRIMARY KEY (" +
                        DexContract.AbilityForPokemonTable.COLUMN_NAME_POKEMON_ID + ", " +
                        DexContract.AbilityForPokemonTable.COLUMN_NAME_ABILITY_ID + ") " + COMMA_SEP +
                    " FOREIGN KEY (" +
                    DexContract.AbilityForPokemonTable.COLUMN_NAME_POKEMON_ID + ") REFERENCES " +
                    DexContract.PokemonTable.TABLE_NAME + "(" + DexContract.PokemonTable.COLUMN_NAME_ID + ")" + COMMA_SEP +
                    " FOREIGN KEY (" +
                    DexContract.AbilityForPokemonTable.COLUMN_NAME_ABILITY_ID + ") REFERENCES " +
                    DexContract.AbilityTable.TABLE_NAME + "(" + DexContract.AbilityTable.COLUMN_NAME_ID + ")"
            + ");";

    public static final String SQL_CREATE_MOVETABLE =
            "CREATE TABLE " + DexContract.MoveTable.TABLE_NAME + "(" +
                    DexContract.MoveTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.MoveTable.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    DexContract.MoveTable.COLUMN_NAME_NAME + TEXT_TYPE
            + ");";

    public static final String SQL_CREATE_MOVEPOKEMONTABLE =
            "CREATE TABLE " + DexContract.MoveForPokemonTable.TABLE_NAME + "(" +
                    DexContract.MoveForPokemonTable.COLUMN_NAME_POKEMON_ID + INTEGER_TYPE + COMMA_SEP +
                    DexContract.MoveForPokemonTable.COLUMN_NAME_MOVE_ID + INTEGER_TYPE + COMMA_SEP +
                    DexContract.MoveForPokemonTable.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP +
                    " PRIMARY KEY (" +
                    DexContract.MoveForPokemonTable.COLUMN_NAME_POKEMON_ID + ", " +
                    DexContract.MoveForPokemonTable.COLUMN_NAME_MOVE_ID + ") " + COMMA_SEP +
                    " FOREIGN KEY (" +
                    DexContract.MoveForPokemonTable.COLUMN_NAME_POKEMON_ID + ") REFERENCES " +
                    DexContract.PokemonTable.TABLE_NAME + "(" + DexContract.PokemonTable.COLUMN_NAME_ID + ")" + COMMA_SEP +
                    " FOREIGN KEY (" +
                    DexContract.MoveForPokemonTable.COLUMN_NAME_MOVE_ID + ") REFERENCES " +
                    DexContract.MoveTable.TABLE_NAME + "(" + DexContract.MoveTable.COLUMN_NAME_ID + ")"
            + ");";

    public static final String SQL_CREATE_EVOLUTIONCHAINTABLE =
            "CREATE TABLE " + DexContract.EvolutionChainTable.TABLE_NAME + "(" +
                    DexContract.EvolutionChainTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.EvolutionChainTable.COLUMN_NAME_OBJECT + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY (" +
                    DexContract.EvolutionChainTable.COLUMN_NAME_OBJECT
                    + ") REFERENCES " + DexContract.EvolutionObjectTable.TABLE_NAME + "(" + DexContract.EvolutionObjectTable.COLUMN_NAME_ID + ")"
            + ");";

    public static final String SQL_CREATE_EVOLUTIONOBJECTTABLE =
            "CREATE TABLE " + DexContract.EvolutionObjectTable.TABLE_NAME + "(" +
                    DexContract.EvolutionObjectTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DexContract.EvolutionObjectTable.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP +
                    DexContract.EvolutionObjectTable.COLUMN_NAME_POKEMON_ID + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY (" +
                    DexContract.EvolutionObjectTable.COLUMN_NAME_POKEMON_ID
                    + ") REFERENCES " + DexContract.PokemonTable.TABLE_NAME + "(" + DexContract.PokemonTable.COLUMN_NAME_ID + ")"
            + ");";

    public static final String SQL_CREATE_EVOLUTIONARRAYTABLE =
            "CREATE TABLE " + DexContract.EvolutionArrayTable.TABLE_NAME + "(" +
                    DexContract.EvolutionArrayTable.COLUMN_NAME_BASE + INTEGER_TYPE + COMMA_SEP +
                    DexContract.EvolutionArrayTable.COLUMN_NAME_GROWN + INTEGER_TYPE + COMMA_SEP +
                    DexContract.EvolutionArrayTable.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP +
                    " PRIMARY KEY (" + DexContract.EvolutionArrayTable.COLUMN_NAME_BASE + COMMA_SEP +
                                        DexContract.EvolutionArrayTable.COLUMN_NAME_GROWN +
                    ")" + COMMA_SEP +
                    " FOREIGN KEY (" + DexContract.EvolutionArrayTable.COLUMN_NAME_BASE + ") REFERENCES " +
                    DexContract.EvolutionObjectTable.TABLE_NAME + "(" + DexContract.EvolutionObjectTable.COLUMN_NAME_POKEMON_ID + ")" + COMMA_SEP +
                    " FOREIGN KEY (" + DexContract.EvolutionArrayTable.COLUMN_NAME_GROWN + ") REFERENCES " +
                    DexContract.EvolutionObjectTable.TABLE_NAME + "(" + DexContract.EvolutionObjectTable.COLUMN_NAME_POKEMON_ID + ")"
            + ");";




    public static final String SQL_DELETE_TABLES =
            "DROP TABLE IF EXISTS " + DexContract.PokemonTable.TABLE_NAME;
    public static final String SQL_DELETE_ABILITYTABLE =
            "DROP TABLE IF EXISTS " + DexContract.AbilityTable.TABLE_NAME;
    public static final String SQL_DELETE_ABILITYPOKEMONTABLE =
            "DROP TABLE IF EXISTS " + DexContract.AbilityForPokemonTable.TABLE_NAME;
    public static final String SQL_DELETE_MOVE =
            "DROP TABLE IF EXISTS " + DexContract.MoveTable.TABLE_NAME;
    public static final String SQL_DELETE_MOVEPOKEMONTABLE =
            "DROP TABLE IF EXISTS " + DexContract.MoveForPokemonTable.TABLE_NAME;
    public static final String SQL_DELETE_EVOLUTIONCHAINTABLE =
            "DROP TABLE IF EXISTS " + DexContract.EvolutionChainTable.TABLE_NAME;
    public static final String SQL_DELETE_EVOLUTIONOBJECTTABLE =
            "DROP TABLE IF EXISTS " + DexContract.EvolutionObjectTable.TABLE_NAME;
    public static final String SQL_DELETE_EVOLUTIONARRAYTABLE =
            "DROP TABLE IF EXISTS " + DexContract.EvolutionArrayTable.TABLE_NAME;




//    public static final String SQL_DELETE_TABLE_NAMES =
//            "DROP TABLE IF EXISTS " + DexContract.PokemonNames.TABLE_NAME;

    public DexSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLES);
        sqLiteDatabase.execSQL(SQL_DELETE_ABILITYTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_ABILITYPOKEMONTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_MOVE);
        sqLiteDatabase.execSQL(SQL_DELETE_MOVEPOKEMONTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_EVOLUTIONCHAINTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_EVOLUTIONOBJECTTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_EVOLUTIONARRAYTABLE);

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLES);
        sqLiteDatabase.execSQL(SQL_CREATE_ABILITYTABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ABILITYPOKEMONTABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVETABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVEPOKEMONTABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EVOLUTIONCHAINTABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EVOLUTIONOBJECTTABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EVOLUTIONARRAYTABLE);

    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLES);
        sqLiteDatabase.execSQL(SQL_DELETE_ABILITYTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_ABILITYPOKEMONTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_MOVE);
        sqLiteDatabase.execSQL(SQL_DELETE_MOVEPOKEMONTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_EVOLUTIONCHAINTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_EVOLUTIONOBJECTTABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_EVOLUTIONARRAYTABLE);

    }

}


