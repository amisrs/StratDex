package com.amisrs.gavin.stratdex.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gavin on 2/10/2016.
 */
public class Move {
    private String id;
    private String url;
    private String name;


    public void setIdFromUrl() {
        Pattern idPattern = Pattern.compile(".+?/move/(.+)*/");
        Matcher idMatcher = idPattern.matcher(url);
        idMatcher.find();
        id = idMatcher.group(1);
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

    public String getId() {
        return id;
    }
}
