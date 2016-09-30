package com.amisrs.gavin.stratdex.model;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Gavin on 15/09/2016.
 */
public class PokemonSpecies {
    private String url = "blank/pokemon-species/0/";
    private String name;
    private Color color;


    private String id;
    private String spritePath;
    private String bigspritePath;
    private Bitmap smallSprite;
    private Boolean isDefaultSprite;
    private String type1 = "";
    private String type2 = "";
    private String colorString = "";
    private int stat1 = 0;
    private int stat2 = 0;
    private int stat3 = 0;
    private int stat4 = 0;
    private int stat5 = 0;
    private int stat6 = 0;
    private AbilityContainer[] abilityContainers;
    private ArrayList<Ability> abilities;



    public PokemonSpecies(String url, String name, String type1, String type2, String spritePath) {

        this.url = url;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.spritePath = spritePath;
        //Bitmap bmp = BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.default_sprite);
        //smallSprite = bmp;
        isDefaultSprite = true;

    }

    //constructor for details
    public PokemonSpecies(String url, String name, String type1, String type2, String spritePath, String bigspritePath, String colorString,
                          int stat1, int stat2, int stat3, int stat4, int stat5, int stat6) {

        System.out.println("tihs is the second constructor for " + name + " that takes spritepath " + spritePath + " and bigspritepath " + bigspritePath);
        this.url = url;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.bigspritePath = bigspritePath;
        this.colorString = colorString;
        //Bitmap bmp = BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.default_sprite);
        //smallSprite = bmp;
        this.stat1 = stat1;
        this.stat2 = stat2;
        this.stat3 = stat3;
        this.stat4 = stat4;
        this.stat5 = stat5;
        this.stat6 = stat6;
        isDefaultSprite = true;

    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<Ability> abilities) {
        this.abilities = abilities;
    }

    public void setStat1(int stat1) {
        this.stat1 = stat1;
    }

    public AbilityContainer[] getAbilityContainers() {
        return abilityContainers;
    }

    public void setAbilityContainers(AbilityContainer[] abilityContainers) {
        this.abilityContainers = abilityContainers;
    }

    public void setStat2(int stat2) {
        this.stat2 = stat2;
    }

    public void setStat3(int stat3) {
        this.stat3 = stat3;
    }

    public void setStat4(int stat4) {
        this.stat4 = stat4;
    }

    public void setStat5(int stat5) {
        this.stat5 = stat5;
    }

    public void setStat6(int stat6) {
        this.stat6 = stat6;
    }

    public int getStat1() {
        return stat1;
    }

    public int getStat2() {
        return stat2;
    }

    public int getStat3() {
        return stat3;
    }

    public int getStat4() {
        return stat4;
    }

    public int getStat5() {
        return stat5;
    }

    public int getStat6() {
        return stat6;
    }

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public String getBigspritePath() {
        return bigspritePath;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Bitmap getSmallSprite() {
        return smallSprite;
    }


    public Boolean getDefaultSprite() {
        return isDefaultSprite;
    }

    public void setSmallSprite(Bitmap smallSprite) {
        this.smallSprite = smallSprite;
    }


    public String getFullName() {
        String capName = "";
        capName = name.replaceFirst(".", Character.toUpperCase(name.charAt(0))+"");
        if(capName.contains("-")) {
            int hyphenIndex = capName.indexOf("-");
            capName = capName.replaceFirst("-.", "-"+Character.toUpperCase(capName.charAt(hyphenIndex+1))+"");
        }

        return capName;
    }

    public void setIdFromUrl() {
        System.out.println("setting id from url");
        Pattern idPattern = Pattern.compile(".+?/pokemon-species/(.+)*/");
        Matcher idMatcher = idPattern.matcher(url);
        System.out.println("trying to match for " + url + "  " + idMatcher.find());
        id = idMatcher.group(1);
        System.out.println(id);
    }



    public String getBigspriteString() {
        String bigSpriteLink = "http://www.smogon.com/dex/media/sprites/xy/"+name+".gif";
        bigspritePath = bigSpriteLink;

        return bigSpriteLink;
    }

    public String getSpriteString() {
        System.out.println("get sprite string... for " + name);
        String spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

        /*DownloadImageAsync downloadImageAsync = new DownloadImageAsync();
        Bitmap retval = null;

        try {
            System.out.println("about to execute downloadImageAsync");
            retval = downloadImageAsync.execute(spriteUrl).get();
            System.out.println("got the sprite");
            isDefaultSprite = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return retval;*/
        return spriteUrl;
    }

    public void pullDetailsAfterClick() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofitPokeAPI = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/")
                .client(okHttpClient)
                .build();

        DetailsService detailsService = retrofitPokeAPI.create(DetailsService.class);
        System.out.println("id shold not be null " + getId());
        Call<ResponseBody> detailsServiceCall = detailsService.getDetails(getId());
        Response detResponse = null;
        ResponseBody responseBody = null;
        String detString = "";
        try {
            detResponse = detailsServiceCall.execute();
            responseBody = (ResponseBody) detResponse.body();
            detString = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GsonBuilder gsonBuilder1 = new GsonBuilder();
        Gson gson1 = gsonBuilder1.create();
        TypeContainerContainer typeContainer = gson1.fromJson(detString, TypeContainerContainer.class);
        System.out.println("looking at " + getName());
        System.out.println("got types = " + typeContainer.getTypeContainers()[0].getSlot() + " " + typeContainer.getTypeContainers()[0].getType().getName());

        if(typeContainer.getTypeContainers()[0].getSlot() == "1") {
            setType1(typeContainer.getTypeContainers()[0].getType().getName());
        } else {
            setType2(typeContainer.getTypeContainers()[0].getType().getName());
        }
        if(typeContainer.getTypeContainers().length == 2) {
            if (typeContainer.getTypeContainers()[1].getSlot() == "1") {
                setType1(typeContainer.getTypeContainers()[1].getType().getName());
            } else {
                setType2(typeContainer.getTypeContainers()[1].getType().getName());
            }
        }

    }
    public interface DetailsService {
        @GET("api/v2/pokemon/{id}")
        Call<ResponseBody> getDetails(@Path(value="id", encoded = true) String id);
    }

    public interface getDetailsService {
        @GET("api/v2/pokemon/{id}")
        Call<ResponseBody> getDetails(@Path(value = "id", encoded = true) String id);
    }

    /*class DownloadImageAsync extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap pic = null;

            try {
                System.out.println("opening input stream");
                InputStream in = new URL(strings[0]).openStream();
                pic = BitmapFactory.decodeStream(in);
                in.close();
                System.out.println("inside async...got sprite as bitmap for " + name);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            smallSprite = pic;
            System.out.println("picture gotten for " + name + "smallsprite = " + smallSprite.getByteCount());

            return pic;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
        }

    }*/

}
