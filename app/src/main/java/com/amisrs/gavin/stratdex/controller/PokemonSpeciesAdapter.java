package com.amisrs.gavin.stratdex.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.db.DbBitmapUtility;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.amisrs.gavin.stratdex.view.DetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Gavin on 15/09/2016.
 */
public class PokemonSpeciesAdapter extends RecyclerView.Adapter<PokemonSpeciesAdapter.SpeciesViewHolder> {
    private static final String TAG = "PokemonSpeciesAdapter";
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
            final String id = data.getId();


            Log.d(TAG, "try loading image for " + data.getId());
            bmp = data.getSmallSprite();


            if(data.getSpritePath() == null) {
                SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(75,75) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String spriteFilePath = context.getFilesDir().toString();
                        OutputStream out = null;

                        File newSpriteFile = new File(spriteFilePath, id+".png");
                        try {
                            //out = new FileOutputStream(newSpriteFile);
                            out = context.openFileOutput(id+".png", Context.MODE_PRIVATE);

                            //resource.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.write(DbBitmapUtility.getBytes(resource));
                            //System.out.println("compressing image");
                            out.flush();
                            out.close();
                            //MediaStore.Images.Media.insertImage(context.getContentResolver(), newSpriteFile.getAbsolutePath(), newSpriteFile.getName(), newSpriteFile.getName());
                           // System.out.println("image is stored");
                            SpeciesQueries speciesQueries = new SpeciesQueries(context);
                            speciesQueries.addSpriteFilePathForSpecies(id, spriteFilePath+"/"+id+".png", "small");
                           // System.out.println("image path is stored in database");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };



                Glide.with(context).load(data.getSpriteString())
                        .asBitmap()
                        .into(simpleTarget);

                Glide.with(context).load(data.getSpriteString()).placeholder(R.drawable.placeholder).into(ssImageView);

            } else {
                Log.d(TAG, "could find spritepath so loading from file.. this pokemon is " + data.getName());
                Glide.with(context).load(new File(data.getSpritePath())).placeholder(R.drawable.placeholder).into(ssImageView);
            }

            idTextView.setText(data.getId());


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
