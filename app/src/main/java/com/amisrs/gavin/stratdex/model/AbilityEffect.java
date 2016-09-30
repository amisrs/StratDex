package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 30/09/2016.
 */
public class AbilityEffect {
    private InnerEffect[] effect_entries;

    public InnerEffect[] getEffect_entries() {
        return effect_entries;
    }

    public class InnerEffect {
        private String short_effect;
        private String effect;

        public String getEffect() {
            return effect;
        }

        public String getShort_effect() {
            return short_effect;
        }

        public void cleanText() {
            effect = effect.replace("\"", "\\\"");
        }
    }
}
