package com.amisrs.gavin.stratdex.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.amisrs.gavin.stratdex.MainActivity;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.amisrs.gavin.stratdex.model.TypeContainer;
import com.amisrs.gavin.stratdex.model.TypeContainerContainer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Gavin on 15/09/2016.
 */
public class FetchDexAsyncTask extends AsyncTask<Void, Void, ArrayList<PokemonSpecies>> {
    private Context context;
    //public AsyncResponse delegate = null;


    public FetchDexAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<PokemonSpecies> doInBackground(Void[] voids) {
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

        PokemonFromPokeAPIService pokemonService = retrofitPokeAPI.create(PokemonFromPokeAPIService.class);

        //!!!!IMPROTANT CHANGE THIS BACK TO TAKE COUNT!!!!!
        Call<ResponseBody> getPokemonCall = pokemonService.getPokemonFromPokeAPI(count);

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
        }

        return pokemonSpecies;
    }

    @Override
    protected void onPostExecute(ArrayList<PokemonSpecies> pokemonSpecies) {

        SpeciesQueries addSpeciesQuery = new SpeciesQueries(context);
        addSpeciesQuery.open();
        System.out.println("inside fetchdexasync");


        for(PokemonSpecies p : pokemonSpecies) {

            addSpeciesQuery.addSpecies(p.getUrl(),p.getName(), p.getType1(), p.getType2());

            System.out.println(p.getUrl() + "added name = " +p.getName() + "id = " + p.getId());


        }
        addSpeciesQuery.close();

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
}
