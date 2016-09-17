package com.amisrs.gavin.stratdex.db;

import android.provider.BaseColumns;

/**
 * Created by Gavin on 15/09/2016.
 */
public final class DexContract     {

    private DexContract() {

    }

    public static class PokemonTable implements BaseColumns {
        public static final String TABLE_NAME = "pokemon";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SPRITE = "sprite";
        public static final String COLUMN_NAME_LEARNSET = "learnset";
        public static final String COLUMN_NAME_STRATEGIES = "strategies";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_ABILITIES = "abilities";
        public static final String COLUMN_NAME_EVOCHAIN = "evolution-chain";
        public static final String COLUMN_NAME_FORMS = "forms";
    }
}
