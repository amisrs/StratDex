package com.amisrs.gavin.stratdex;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Gavin on 15/09/2016.
 */
public class PokemonSpeciesAdapter extends RecyclerView.Adapter<PokemonSpeciesAdapter.SpeciesViewHolder> {
    private ArrayList<PokemonSpecies> species;
    private Context context;
    public static String LIST_KEY = "LIST";

    public PokemonSpeciesAdapter(ArrayList<PokemonSpecies> pokemonSpecies, Context context) {
        species = pokemonSpecies;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return species.size();
    }

    @Override
    public PokemonSpeciesAdapter.SpeciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_layout, parent, false);
        return new SpeciesViewHolder(inflatedView, context);
    }

    @Override
    public void onBindViewHolder(PokemonSpeciesAdapter.SpeciesViewHolder holder, int position) {
        PokemonSpecies pokemonSpecies = species.get(position);
        holder.bindSpecies(pokemonSpecies);
    }



    public static class SpeciesViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView urlTextView;
        public TextView nameTextView;
        public TextView idTextView;
        public TextView t1TextView;
        public TextView t2TextView;
        public Context context;

        public ImageView ssImageView;
        public Bitmap bmp;
        public PokemonSpecies data;

        public SpeciesViewHolder(View v, Context context) {
            super(v);
            this.context = context;
            this.view = v;
            nameTextView = (TextView)v.findViewById(R.id.tv_name);
            idTextView = (TextView)v.findViewById(R.id.tv_id);
            ssImageView = (ImageView)v.findViewById(R.id.iv_ss);

        }

        public void bindSpecies(PokemonSpecies pokemonSpecies) {
            data = pokemonSpecies;

            //get/set sprite
            nameTextView.setText(data.getFullName());

            System.out.println("try loading image for " + data.getId());

            bmp = data.getSmallSprite();

            idTextView.setText(data.getId());

            Glide.with(context).load(data.getSpriteString()).placeholder(R.drawable.sprite_spinner).into(ssImageView);

            Drawable d = ssImageView.getDrawable();

            OnPokemonClick onPokemonClick = new OnPokemonClick(pokemonSpecies, context);
            getView().setOnClickListener(onPokemonClick);


        }

        public View getView() {
            return view;
        }

        public class OnPokemonClick implements View.OnClickListener {
            PokemonSpecies clickedSpecies;
            Context context;

            public OnPokemonClick(PokemonSpecies clickedSpecies, Context context) {
                this.clickedSpecies = clickedSpecies;
                this.context = context;
            }

            @Override
            public void onClick(View v) {
                Activity activity = (Activity)v.getContext();

                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra(LIST_KEY, clickedSpecies.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.rightoleft,R.anim.blank);

            }

        }

    }
}
