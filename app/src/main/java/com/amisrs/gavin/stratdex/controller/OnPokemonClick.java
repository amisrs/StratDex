package com.amisrs.gavin.stratdex.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.amisrs.gavin.stratdex.view.DetailsActivity;

/**
 * Created by Gavin on 3/10/2016.
 */
public class OnPokemonClick implements View.OnClickListener {
    PokemonSpecies clickedSpecies;
    Context context;
    String LIST_KEY = "LIST";
    String from = "";

    public OnPokemonClick(PokemonSpecies clickedSpecies, Context context, String from) {
        this.from = from;
        this.clickedSpecies = clickedSpecies;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Activity activity = (Activity)v.getContext();

        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
        intent.putExtra(LIST_KEY, clickedSpecies.getId());
        intent.putExtra("FROM", from);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.rightoleft,R.anim.blank);

    }

}
