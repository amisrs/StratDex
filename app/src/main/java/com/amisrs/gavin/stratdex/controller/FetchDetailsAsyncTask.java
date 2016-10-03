package com.amisrs.gavin.stratdex.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.amisrs.gavin.stratdex.db.AbilityQueries;
import com.amisrs.gavin.stratdex.db.MoveQueries;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;
import com.amisrs.gavin.stratdex.model.Ability;
import com.amisrs.gavin.stratdex.model.AbilityContainer;
import com.amisrs.gavin.stratdex.model.AbilityEffect;
import com.amisrs.gavin.stratdex.model.DetailsFromPokemon;
import com.amisrs.gavin.stratdex.model.DetailsFromSpecies;
import com.amisrs.gavin.stratdex.model.PMove;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.amisrs.gavin.stratdex.model.TypeContainer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
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
        JsonElement flavorElemet = jsonObject.get("flavor_text_entries");
        JsonElement genusElement = jsonObject.get("genera");
        DetailsFromSpecies.FlavorTextEntry[] flavs = gson.fromJson(flavorElemet, DetailsFromSpecies.FlavorTextEntry[].class);
        DetailsFromSpecies.Genus[] genera = gson.fromJson(genusElement, DetailsFromSpecies.Genus[].class);
        System.out.println("HERES JSON \n" + jsonObject.get("flavor_text_entries").toString());
        System.out.println("lmao this better not be empty " + detailsFromSpecies.getFlavor_text_entries().length);

        DetailsFromPokemonService detailsFromPokemonService = retrofitPokeAPI.create(DetailsFromPokemonService.class);
        Call<ResponseBody> detailsCall2 = detailsFromPokemonService.getDetailsFromPokemon(params[0].getId());

        Response r2 = null;
        ResponseBody rb2 = null;
        String responseString2 = "";
        try {
            r2 = detailsCall2.execute();
            rb2 = (ResponseBody)r2.body();
            responseString2 = rb2.string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject2 = gson.fromJson(responseString2, JsonObject.class);
        DetailsFromPokemon detailsFromPokemon = gson.fromJson(jsonObject2, DetailsFromPokemon.class);
        //System.out.printf("HERES JSON: \n " + jsonObject2.get("moves").toString());
        JsonElement moveElement = jsonObject2.get("moves");
        PMove[] pMoves = gson.fromJson(moveElement, PMove[].class);
        System.out.println("hey made pmoves length " + pMoves.length);

        System.out.println("the first stat for " + detailsFromPokemon.getId() + "    is " + detailsFromPokemon.getStats()[1].getBaseStat());
        System.out.println("it has " + detailsFromPokemon.getStats().length + " stats");
        params[0].setColorString(detailsFromSpecies.getColor().getName());


//        params[0].setType1(containers[0].getType().getName());
//        params[0].setType2(containers[1].getType().getName());

        params[0].setStat1(detailsFromPokemon.getStats()[5].getBaseStat());
        params[0].setStat2(detailsFromPokemon.getStats()[4].getBaseStat());
        params[0].setStat3(detailsFromPokemon.getStats()[3].getBaseStat());
        params[0].setStat4(detailsFromPokemon.getStats()[2].getBaseStat());
        params[0].setStat5(detailsFromPokemon.getStats()[1].getBaseStat());
        params[0].setStat6(detailsFromPokemon.getStats()[0].getBaseStat());

        TypeContainer[] containers = detailsFromPokemon.getTypeContainers();

        String type1 = "";
        String type2 = "";

        for(TypeContainer t : containers) {
            if(t.getSlot().equals("1")) {
                type1 = t.getType().getName();
                System.out.println("slot " + t.getSlot() + " is " + t.getType().getName());
            } else if (t.getSlot().equals("2")) {
                type2 = t.getType().getName();
                System.out.println("slot " + t.getSlot() + " is " + t.getType().getName());
            } else {
                System.out.println("theres a type with no slot?");
            }
        }

        System.out.println("HEY TYPE 2 = " + type2);
        params[0].setType2(type2);
        params[0].setType1(type1);

        AbilityContainer[] abilityContainers = detailsFromPokemon.getAbilities();
        DescFromAbilityService descFromAbilityService = retrofitPokeAPI.create(DescFromAbilityService.class);

        for(int k=0; k<abilityContainers.length; k++) {
            abilityContainers[k].getAbility().setIdFromUrl();
            Call<ResponseBody> descCall = descFromAbilityService.getDetailsFromPokemon(abilityContainers[k].getAbility().getId());
            retrofit2.Response<ResponseBody> abilityDescResponse = null;
            String abilityDescResponseString = "";

            try {
                abilityDescResponse = descCall.execute();
                abilityDescResponseString = abilityDescResponse.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonObject descJsonObject = gson.fromJson(abilityDescResponseString, JsonObject.class);
            AbilityEffect abilityEffect = gson.fromJson(descJsonObject, AbilityEffect.class);
            for(int m=0; m < abilityEffect.getEffect_entries().length; m++) {
                abilityEffect.getEffect_entries()[m].cleanText();
            }

            AbilityQueries abilityQueries = new AbilityQueries(context);
            abilityQueries.addDescToAbility(abilityContainers[k].getAbility().getId(), abilityEffect.getEffect_entries()[0].getShort_effect());
        }

        String desc = "";
        boolean foundF = false;

        for(int i=0; i < flavs.length && foundF == false; i++) {
            System.out.println("im looking at the descs number " + i);
            if(flavs[i].getLanguage().getName().equals("en")) {
                desc = flavs[i].getFlavor_text();
                System.out.println("aha found the neglish desc " + desc);
                foundF = true;
            } else {
                System.out.println("its not in english");
            }
        }

        String genus = "";
        boolean foundG = false;
        for(int i=0; i < genera.length && foundG == false; i++) {
            if(genera[i].getLanguage().getName().equals("en")) {
                genus = genera[i].getGenus();
                foundG = true;
            } else {
                System.out.println("its not in english");
            }
        }



        params[0].setGenus(genus);
        params[0].setDesc(desc);
        params[0].setHeight(detailsFromPokemon.getHeight());
        params[0].setWeight(detailsFromPokemon.getWeight());
        params[0].setMoves(pMoves);
        params[0].setAbilityContainers(abilityContainers);


        return params[0];
    }

    @Override
    protected void onPostExecute(PokemonSpecies pokemonSpecies) {
        super.onPostExecute(pokemonSpecies);
        SpeciesQueries speciesQueries = new SpeciesQueries(context);
        speciesQueries.addDetailsForSpecies(pokemonSpecies.getId(), pokemonSpecies.getColorString()
                , pokemonSpecies.getStat1(), pokemonSpecies.getStat2(), pokemonSpecies.getStat3(), pokemonSpecies.getStat4(), pokemonSpecies.getStat5(), pokemonSpecies.getStat6()
                , pokemonSpecies.getType1(), pokemonSpecies.getType2()
                , pokemonSpecies.getHeight(), pokemonSpecies.getWeight()
                , pokemonSpecies.getDesc(), pokemonSpecies.getGenus());

        AbilityQueries abilityQueries = new AbilityQueries(context);

        AbilityContainer[] abilityArray = pokemonSpecies.getAbilityContainers();
        for(int i=0; i<abilityArray.length; i++) {
            abilityQueries.linkAbilityToPokemon(Integer.parseInt(pokemonSpecies.getId()), Integer.parseInt(abilityArray[i].getAbility().getId()));
        }
        ArrayList<Ability> abilityArrayList = new ArrayList<>();
        abilityArrayList = abilityQueries.getAbilitiesForPokemon(Integer.parseInt(pokemonSpecies.getId()));
        System.out.println("hey heres size of ability arraylist " + abilityArrayList.size());
        pokemonSpecies.setAbilities(abilityArrayList);

        MoveQueries moveQueries = new MoveQueries(context);
        PMove[] PMoveArray = pokemonSpecies.getMoves();
        for(int i = 0; i< PMoveArray.length; i++) {
            PMoveArray[i].getMove().setIdFromUrl();
            moveQueries.linkMoveToPokemon(Integer.parseInt(pokemonSpecies.getId()), Integer.parseInt(PMoveArray[i].getMove().getId()), PMoveArray[i].getVersion_group_details()[0].getLevel_learned_at());
        }
        ArrayList<PMove> pMoveArrayList = new ArrayList<>();
        pMoveArrayList = moveQueries.getMovesForPokemon(Integer.parseInt(pokemonSpecies.getId()));
        pokemonSpecies.setMoveArrayList(pMoveArrayList);

        delegate.giveFilledPokemon(pokemonSpecies);
        System.out.println("the color of the pokemon you clicked is " + pokemonSpecies.getColorString());
    }

    public interface DetailsFromSpeciesService {
        @GET("api/v2/pokemon-species/{number}")
        Call<ResponseBody> getDetailsFromSpecies(@Path(value = "number", encoded = true) String id);
    }

    public interface DetailsFromPokemonService {
        @GET("api/v2/pokemon/{number}")
        Call<ResponseBody> getDetailsFromPokemon(@Path(value = "number", encoded = true) String id);
    }

    public interface DescFromAbilityService {
        @GET("api/v2/ability/{number}")
        Call<ResponseBody> getDetailsFromPokemon(@Path(value = "number", encoded = true) String id);
    }
}
