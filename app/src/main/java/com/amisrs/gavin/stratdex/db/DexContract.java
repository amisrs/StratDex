package com.amisrs.gavin.stratdex.db;

import android.provider.BaseColumns;

import com.amisrs.gavin.stratdex.view.SlidingTabLayout;

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
        public static final String COLUMN_NAME_BIGSPRITE = "bigsprite";
        public static final String COLUMN_NAME_COLOR = "color";
//        public static final String COLUMN_NAME_BIGNAME = "bigname";
        public static final String COLUMN_NAME_LEARNSET = "learnset";
        public static final String COLUMN_NAME_STRATEGIES = "strategies";
        public static final String COLUMN_NAME_TYPE1 = "type1";
        public static final String COLUMN_NAME_TYPE2 = "type2";
        public static final String COLUMN_NAME_STAT1 = "stat1";
        public static final String COLUMN_NAME_STAT2 = "stat2";
        public static final String COLUMN_NAME_STAT3 = "stat3";
        public static final String COLUMN_NAME_STAT4 = "stat4";
        public static final String COLUMN_NAME_STAT5 = "stat5";
        public static final String COLUMN_NAME_STAT6 = "stat6";
        public static final String COLUMN_NAME_ABILITIES = "abilities";
        public static final String COLUMN_NAME_EVOCHAIN = "evolution-chain";
        public static final String COLUMN_NAME_FORMS = "forms";
    }

    public static class AbilityTable implements BaseColumns {
        public static final String TABLE_NAME = "ability";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESC = "desc";
    }

    public static class AbilityForPokemonTable implements BaseColumns {
        public static final String TABLE_NAME = "abilitypokemon";
        public static final String COLUMN_NAME_POKEMON_ID = "pid";
        public static final String COLUMN_NAME_ABILITY_ID = "aid";
    }

    public static class MoveTable implements BaseColumns {
        public static final String TABLE_NAME = "move";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static class MoveForPokemonTable implements BaseColumns {
        public static final String TABLE_NAME = "movepokemon";
        public static final String COLUMN_NAME_POKEMON_ID = "pid";
        public static final String COLUMN_NAME_MOVE_ID = "mid";
        public static final String COLUMN_NAME_LEVEL = "level";
    }

//    public static class PokemonNames implements BaseColumns {
//        public static final String TABLE_NAME = "pokemonnames";
//        public static final String COLUMN_NAME_ID = "id";
//        public static final String COLUMN_NAME_POKEMONID = "pokemon";
//        public static final String COLUMN_NAME_LANGUAGE = "language";
//        public static final String COLUMN_NAME_NAME = "name";
//    }
}
