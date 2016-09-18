package com.amisrs.gavin.stratdex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.model.PokemonSpecies;


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

    public PokemonSpeciesAdapter(ArrayList<PokemonSpecies> pokemonSpecies) {
        species = pokemonSpecies;
    }

    @Override
    public int getItemCount() {
        return species.size();
    }

    @Override
    public PokemonSpeciesAdapter.SpeciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_layout, parent, false);
        return new SpeciesViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PokemonSpeciesAdapter.SpeciesViewHolder holder, int position) {
        PokemonSpecies pokemonSpecies = species.get(position);
        holder.bindSpecies(pokemonSpecies);

    }

    public static class SpeciesViewHolder extends RecyclerView.ViewHolder {
        public TextView urlTextView;
        public TextView nameTextView;
        public TextView idTextView;
        public TextView t1TextView;
        public TextView t2TextView;

        public ImageView ssImageView;
        public Bitmap bmp;
        public PokemonSpecies data;

        public SpeciesViewHolder(View v) {
            super(v);
            nameTextView = (TextView)v.findViewById(R.id.tv_name);
            idTextView = (TextView)v.findViewById(R.id.tv_id);
            ssImageView = (ImageView)v.findViewById(R.id.iv_ss);

        }

        public void bindSpecies(PokemonSpecies pokemonSpecies) {
            data = pokemonSpecies;
            nameTextView.setText(data.getFullName());

            System.out.println("try loading image for " + data.getId());

            bmp = data.getSmallSprite();

            idTextView.setText(data.getId());
            ssImageView.setImageBitmap(bmp);
        }
    }
}
