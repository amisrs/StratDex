package com.amisrs.gavin.stratdex.model;

import junit.runner.Version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gavin on 2/10/2016.
 */


public class PMove implements Comparable<PMove> {
    private VersionGroupDetails[] version_group_details;
    private Move move;

    @Override
    public int compareTo(PMove pMove) {
        int compareLevel = pMove.getVersion_group_details()[0].getLevel_learned_at();

        return this.getVersion_group_details()[0].getLevel_learned_at() - compareLevel;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public VersionGroupDetails[] getVersion_group_details() {
        return version_group_details;
    }

    public void setVersion_group_details(VersionGroupDetails[] version_group_details) {
        this.version_group_details = version_group_details;
    }

    public void initialize() {
        move = new Move();
        VersionGroupDetails[] dummy = new VersionGroupDetails[1];
        dummy[0] = new VersionGroupDetails();
        version_group_details = dummy;
    }

    public class VersionGroupDetails {
            private int level_learned_at;
            private VersionGroup version_group;

            public class VersionGroup {
                private String name;
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public int getLevel_learned_at() {
                return level_learned_at;
            }

            public void setLevel_learned_at(int level_learned_at) {
                this.level_learned_at = level_learned_at;
            }

            public VersionGroup getVersion_group() {
                return version_group;
            }

            public void setVersion_group(VersionGroup version_group) {
                this.version_group = version_group;
            }
        }
}
