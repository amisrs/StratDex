package com.amisrs.gavin.stratdex.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gavin on 30/09/2016.
 */
public class Ability {
    private String id;
    private String url;
    private String name;
    private String desc;


    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIdFromUrl() {
        System.out.println("setting id from url");
        Pattern idPattern = Pattern.compile(".+?/ability/(.+)*/");
        Matcher idMatcher = idPattern.matcher(url);
        System.out.println("trying to match for " + url + "  " + idMatcher.find());
        id = idMatcher.group(1);
        System.out.println(id);
    }

    public String getCleanName() {
        String capName = name;
        capName = name.replaceFirst(".", Character.toUpperCase(name.charAt(0))+"");
        if(capName.contains("-")) {
            int hyphenIndex = capName.indexOf("-");
            capName = capName.replaceFirst("-.", " "+Character.toUpperCase(capName.charAt(hyphenIndex+1))+"");
        }


        return capName;
    }

}
