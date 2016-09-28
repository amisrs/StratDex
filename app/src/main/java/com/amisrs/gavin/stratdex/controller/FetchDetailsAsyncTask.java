package com.amisrs.gavin.stratdex.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.amisrs.gavin.stratdex.controller.SpeciesQueries;
import com.amisrs.gavin.stratdex.db.AsyncResponse;
import com.amisrs.gavin.stratdex.model.DetailsFromSpecies;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Gavin on 27/09/2016.
 */
public class FetchDetailsAsyncTask extends AsyncTask<PokemonSpecies, Void, PokemonSpecies> {
    private Context context;
    public AsyncResponse delegate = null;

    public FetchDetailsAsyncTask(Context context) {
        super();
        this.context = context;
    }


    @Override
    protected PokemonSpecies doInBackground(PokemonSpecies... params) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofitPokeAPI = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/")
                .client(okHttpClient)
                .build();

        DetailsFromSpeciesService detailsFromSpeciesService = retrofitPokeAPI.create(DetailsFromSpeciesService.class);
        Call<ResponseBody> detailsCall = detailsFromSpeciesService.getDetailsFromSpecies(params[0].getId());

        Response r = null;
        ResponseBody rb = null;
        String responseString = "";
        try {
            r = detailsCall.execute();
            rb = (ResponseBody)r.body();
            responseString = rb.string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
        DetailsFromSpecies detailsFromSpecies = gson.fromJson(jsonObject, DetailsFromSpecies.class);

        params[0].setColorString(detailsFromSpecies.getColor().getName());

        return params[0];
    }

    @Override
    protected void onPostExecute(PokemonSpecies pokemonSpecies) {
        super.onPostExecute(pokemonSpecies);
        SpeciesQueries speciesQueries = new SpeciesQueries(context);
        speciesQueries.addDetailsForSpecies(pokemonSpecies.getId(), pokemonSpecies.getColorString());
        delegate.giveFilledPokemon(pokemonSpecies);
        System.out.println("the color of the pokemon you clicked is " + pokemonSpecies.getColorString());

    }

    public interface DetailsFromSpeciesService {
        @GET("api/v2/pokemon-species/{number}")
        Call<ResponseBody> getDetailsFromSpecies(@Path(value = "number", encoded = true) String id);

    }
}
