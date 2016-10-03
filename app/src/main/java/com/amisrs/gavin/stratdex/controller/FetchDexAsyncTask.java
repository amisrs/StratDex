package com.amisrs.gavin.stratdex.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.amisrs.gavin.stratdex.db.AbilityQueries;
import com.amisrs.gavin.stratdex.db.MoveQueries;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;
import com.amisrs.gavin.stratdex.db.DexSQLHelper;
import com.amisrs.gavin.stratdex.model.Ability;
import com.amisrs.gavin.stratdex.model.Move;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gavin on 15/09/2016.
 */
public class FetchDexAsyncTask extends AsyncTask<Void, Integer, ArrayList<PokemonSpecies>> {
    private Context context;
    public LoadResponse delegate = null;
    //public AsyncResponse delegate = null;


    public FetchDexAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<PokemonSpecies> doInBackground(Void[] voids) {
        int prog = 0;
        DexSQLHelper dexSQLHelper = new DexSQLHelper(context);
        SQLiteDatabase db = dexSQLHelper.getWritableDatabase();
        db.execSQL(dexSQLHelper.SQL_DELETE_TABLES);
        db.execSQL(dexSQLHelper.SQL_CREATE_TABLES);
        db.close();
        dexSQLHelper.close();


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofitPokeAPI = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/")
                .client(okHttpClient)
                .build();

        CountFromPokeAPIService countService = retrofitPokeAPI.create(CountFromPokeAPIService.class);
        Call<ResponseBody> getCountCall = countService.getCountFromPokeAPI();
        String count = "";
        retrofit2.Response<ResponseBody> response = null;
        try {
            response = getCountCall.execute();
            String responseString = response.body().string();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            JSONObject jsonObject = new JSONObject(responseString);
            count = jsonObject.getString("count");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AbilityCountFromPokeAPIService abilityCountService = retrofitPokeAPI.create(AbilityCountFromPokeAPIService.class);
        Call<ResponseBody> getAbilityCountCall = abilityCountService.getAbilityCountFromPokeAPI();
        String aCount = "";
        retrofit2.Response<ResponseBody> abilityCountResponse = null;
        try {
            abilityCountResponse = getAbilityCountCall.execute();
            String aResponseString = abilityCountResponse.body().string();

            JSONObject acJsonObject = new JSONObject(aResponseString);
            aCount = acJsonObject.getString("count");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MoveCountFromPokeAPIService moveCountService = retrofitPokeAPI.create(MoveCountFromPokeAPIService.class);
        Call<ResponseBody> getMoveCountCall = moveCountService.getMoveCountFromPokeAPI();
        String mCount = "";
        retrofit2.Response<ResponseBody> moveCountResponse = null;
        try {
            moveCountResponse = getMoveCountCall.execute();
            String mResponseString = moveCountResponse.body().string();

            JSONObject mcJsonObject=  new JSONObject(mResponseString);
            mCount = mcJsonObject.getString("count");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PokemonFromPokeAPIService pokemonService = retrofitPokeAPI.create(PokemonFromPokeAPIService.class);
        AbilitiesFromPokeAPIService abilitiesService = retrofitPokeAPI.create(AbilitiesFromPokeAPIService.class);
        MovesFromPokeAPIService movesService = retrofitPokeAPI.create(MovesFromPokeAPIService.class);

        //!!!!IMPROTANT CHANGE THIS BACK TO TAKE COUNT!!!!!
        Call<ResponseBody> getPokemonCall = pokemonService.getPokemonFromPokeAPI(count);
        Call<ResponseBody> getAbilitiesCall = abilitiesService.getAbilitiesFromPokeAPI(aCount);
        Call<ResponseBody> getMovesCall = movesService.getMoveFromPokeAPI(mCount);

        prog = 1;
        publishProgress(prog);
        retrofit2.Response<ResponseBody> fullListResponse = null;
        String fullListString = "";
        JsonObject jsonObject = null;
        JsonArray resultsArray = null;
        try {
            fullListResponse = getPokemonCall.execute();
            fullListString = fullListResponse.body().string();
            System.out.println(fullListString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        System.out.println(fullListString);
        jsonObject = gson.fromJson(fullListString, JsonObject.class);

        resultsArray = jsonObject.getAsJsonArray("results");
        System.out.println(resultsArray.toString());
        PokemonSpecies[] allPokemon = gson.fromJson(resultsArray, PokemonSpecies[].class);

        ArrayList<PokemonSpecies> pokemonSpecies = new ArrayList<>();

        Retrofit r2 = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .client(okHttpClient)
                .build();

        for(int i=0; i < allPokemon.length; i++) {


            allPokemon[i].setIdFromUrl();
            System.out.println("created pokemon " + allPokemon[i].getName());
            pokemonSpecies.add(allPokemon[i]);
            SpeciesQueries addSpeciesQuery = new SpeciesQueries(context);
            addSpeciesQuery.open();
            addSpeciesQuery.addSpecies(allPokemon[i].getUrl(),allPokemon[i].getName(), allPokemon[i].getType1(), allPokemon[i].getType2(), allPokemon[i].getSpritePath());
            addSpeciesQuery.close();
        }
        prog = 2;
        publishProgress(prog);

        retrofit2.Response<ResponseBody> abilityListResponse = null;
        String abilityList = "";
        JsonObject aJsonObject = null;
        JsonArray aJsonArray = null;
        try {
            abilityListResponse = getAbilitiesCall.execute();
            abilityList = abilityListResponse.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        aJsonObject = gson.fromJson(abilityList, JsonObject.class);
        aJsonArray = aJsonObject.getAsJsonArray("results");
        Ability[] abilityArray = gson.fromJson(aJsonArray, Ability[].class);
        AbilityQueries abilityQueries = new AbilityQueries(context);
        for(int j=0; j < abilityArray.length; j++) {
            abilityQueries.addAbility(abilityArray[j]);
        }

        prog = 3;
        publishProgress(prog);
        retrofit2.Response<ResponseBody> moveListResponse = null;
        String moveList = "";
        JsonObject mJsonObject = null;
        JsonArray mJsonArray = null;
        try {
            moveListResponse = getMovesCall.execute();
            moveList = moveListResponse.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mJsonObject = gson.fromJson(moveList, JsonObject.class);
        mJsonArray = mJsonObject.getAsJsonArray("results");
        Move[] moves = gson.fromJson(mJsonArray, Move[].class);
        MoveQueries moveQueries = new MoveQueries(context);
        for(int j = 0; j < moves.length; j++) {
            moveQueries.addMove(moves[j]);
        }

        return pokemonSpecies;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        String text = "";
        switch(values[0]) {
            case 0 : text = "Starting...";
                        break;
            case 1 : text = "Loading Pokemon...";
                        break;
            case 2 : text = "Loading abilities...";
                        break;
            case 3 : text = "Loading moves...";
                        break;
            default: text = "Loading...";
        }

        delegate.updateLoadingMsg(text);
    }

    @Override
    protected void onPostExecute(ArrayList<PokemonSpecies> pokemonSpecies) {

        //SpeciesQueries addSpeciesQuery = new SpeciesQueries(context);
        //addSpeciesQuery.open();
        System.out.println("inside fetchdexasync");
        delegate.sayHasLoaded();


//        for(PokemonSpecies p : pokemonSpecies) {
//
//            addSpeciesQuery.addSpecies(p.getUrl(),p.getName(), p.getType1(), p.getType2());
//
//            System.out.println(p.getUrl() + "added name = " +p.getName() + "id = " + p.getId());
//
//
//        }
        //addSpeciesQuery.close();

        System.out.println("done");
    }

    public interface CountFromPokeAPIService {
        @GET("api/v2/pokemon-species/")
        Call<ResponseBody> getCountFromPokeAPI();
    }
    public interface PokemonFromPokeAPIService {
        @GET("api/v2/pokemon-species/")
        Call<ResponseBody> getPokemonFromPokeAPI(@Query(value="limit", encoded = true) String count);
    }

    public interface AbilityCountFromPokeAPIService {
        @GET("api/v2/ability/")
        Call<ResponseBody> getAbilityCountFromPokeAPI();
    }

    public interface  AbilitiesFromPokeAPIService {
        @GET("api/v2/ability/")
        Call<ResponseBody> getAbilitiesFromPokeAPI(@Query(value="limit",encoded = true) String count);
    }

    public interface MoveCountFromPokeAPIService {
        @GET("api/v2/move/")
        Call<ResponseBody> getMoveCountFromPokeAPI();
    }

    public interface  MovesFromPokeAPIService {
        @GET("api/v2/move/")
        Call<ResponseBody> getMoveFromPokeAPI(@Query(value="limit",encoded = true) String count);
    }

}
