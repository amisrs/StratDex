package com.amisrs.gavin.stratdex.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gavin on 15/09/2016.
 * Just stores a whole bunc of stuff
 */

//  TODO: clean this forsaken mess
public class PokemonSpecies {
    private static final String TAG = "PokemonSpecies";

    private String url = "blank/pokemon-species/0/";
    private String name;

    private String id;
    private String spritePath;
    private String bigspritePath;
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
    private PMove[] moves;
    private ArrayList<PMove> moveArrayList;
    private int height;
    private int weight;
    private String desc;
    private String genus;
    private int evoChainId;
    private EvolutionChain evoChainTemp;
    private String generation;



    public PokemonSpecies(String url, String name, String type1, String type2, String spritePath) {

        this.url = url;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.spritePath = spritePath;
        //Bitmap bmp = BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.default_sprite);
        //smallSprite = bmp;

    }

    //constructor for details
    public PokemonSpecies(String url, String name, String type1, String type2, String spritePath, String bigspritePath, String colorString,
                          int stat1, int stat2, int stat3, int stat4, int stat5, int stat6,
                          int height, int weight,
                          String desc, String genus,
                          int evoChainId,
                          String generation) {

        Log.d(TAG, "constructing the pokemonspecies that should be passed to details view...  name = " + name);
        this.url = url;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.spritePath = spritePath;
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
        this.height = height;
        this.weight = weight;
        this.desc = desc;
        this.genus = genus;
        this.evoChainId = evoChainId;
        this.generation = generation;

    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public EvolutionChain getEvoChainTemp() {
        return evoChainTemp;
    }

    public void setEvoChainTemp(EvolutionChain evoChainTemp) {
        this.evoChainTemp = evoChainTemp;
    }

    public int getEvoChainId() {
        return evoChainId;
    }

    public void setEvoChainId(int evoChainId) {
        this.evoChainId = evoChainId;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    public void setMoveArrayList(ArrayList<PMove> moveArrayList) {
        this.moveArrayList = moveArrayList;
    }

    public PMove[] getMoves() {
        return moves;
    }

    public void setMoves(PMove[] PMoves) {
        this.moves = PMoves;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<Ability> abilities) {
        this.abilities = abilities;
    }

    public AbilityContainer[] getAbilityContainers() {
        return abilityContainers;
    }

    public void setAbilityContainers(AbilityContainer[] abilityContainers) {
        this.abilityContainers = abilityContainers;
    }

    public int getStat1() {
        return stat1;
    }

    public void setStat1(int stat1) {
        this.stat1 = stat1;
    }

    public int getStat2() {
        return stat2;
    }

    public void setStat2(int stat2) {
        this.stat2 = stat2;
    }

    public int getStat3() {
        return stat3;
    }

    public void setStat3(int stat3) {
        this.stat3 = stat3;
    }

    public int getStat4() {
        return stat4;
    }

    public void setStat4(int stat4) {
        this.stat4 = stat4;
    }

    public int getStat5() {
        return stat5;
    }

    public void setStat5(int stat5) {
        this.stat5 = stat5;
    }

    public int getStat6() {
        return stat6;
    }

    public void setStat6(int stat6) {
        this.stat6 = stat6;
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
        Pattern idPattern = Pattern.compile(".+?/pokemon-species/(.+)*/");
        Matcher idMatcher = idPattern.matcher(url);
        idMatcher.find();
        id = idMatcher.group(1);
        //System.out.println(id);
    }

    public String regionFromGeneration(String gen) {
        switch(gen) {
            case "Generation-I":
                return "Kanto";
            case "Generation-Ii" :
                return "Johto";
            case "Generation-Iii" :
                return "Hoenn";
            case "Generation-Iv" :
                return "Sinnoh";
            case "Generation-V" :
                return "Unova";
            case "Generation-Vi" :
                return "Kalos";
            case "Generation-Vii" :
                return "Alola";
            default:
                return "N/A";
        }
    }



    public String getBigspriteString() {
        String bigSpriteLink = "http://www.smogon.com/dex/media/sprites/xy/"+name+".gif";
        bigspritePath = bigSpriteLink;

        return bigSpriteLink;
    }

    public String getSpriteString() {
        System.out.println("get sprite string... for " + name);
        String spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

        return spriteUrl;
    }


}
