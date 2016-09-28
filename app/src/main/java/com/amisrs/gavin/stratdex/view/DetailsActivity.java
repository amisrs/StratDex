package com.amisrs.gavin.stratdex.view;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.controller.PokemonSpeciesAdapter;
import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.db.AsyncResponse;
import com.amisrs.gavin.stratdex.controller.FetchDetailsAsyncTask;
import com.amisrs.gavin.stratdex.controller.SpeciesQueries;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DetailsActivity extends AppCompatActivity implements AsyncResponse {
    private TextView nameTextView;
    private ImageView bigspriteImageView;
    private ProgressBar bottomProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);

        setContentView(R.layout.activity_details);
        nameTextView = (TextView)findViewById(R.id.tv_name);
        bigspriteImageView = (ImageView)findViewById(R.id.iv_bigsprite);
        bottomProgressBar = (ProgressBar)findViewById(R.id.pb_spinner);
        bottomProgressBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        String id = intent.getStringExtra(PokemonSpeciesAdapter.LIST_KEY);
        SpeciesQueries speciesQueries = new SpeciesQueries(MainActivity.context);
        final PokemonSpecies theOne = speciesQueries.getOneSpeciesById(id);
        nameTextView.setText(theOne.getFullName());

        SimpleTarget<GifDrawable> simpleTarget = new SimpleTarget<GifDrawable>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {

            @Override
            public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                System.out.println("onresourceready simpletarget");
                String spriteFilePath = Environment.getExternalStorageDirectory().toString();
                OutputStream out = null;

                File newSpriteFile = new File(spriteFilePath, "bigsprite"+theOne.getId()+".png");
                try {
                    out = new FileOutputStream(newSpriteFile);
                    byte[] gifData = resource.getData();
                    out.write(gifData);
                    System.out.println("compressing image");
                    out.flush();
                    out.close();
                    MediaStore.Images.Media.insertImage(getContentResolver(), newSpriteFile.getAbsolutePath(), newSpriteFile.getName(), newSpriteFile.getName());
                    System.out.println("image is stored");
                    SpeciesQueries speciesQueries = new SpeciesQueries(getApplicationContext());
                    speciesQueries.addSpriteFilePathForSpecies(theOne.getId(), newSpriteFile.getAbsolutePath(), "big");
                    System.out.println("image path is stored in database");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        if(theOne.getBigspritePath() == null) {

                System.out.println("the sprite is null for " + theOne.getName());

                Glide.with(this).load(theOne.getBigspriteString())
                        .asGif()
                        .into(simpleTarget);

                Glide.with(this).load(theOne.getBigspriteString()).asGif().dontTransform().fitCenter().placeholder(R.drawable.placeholder).into(bigspriteImageView);

        } else {
            System.out.println("could find bigspritepath at "+theOne.getBigspritePath()+" so loading from file.. this pokemon is " + theOne.getName());
            Glide.with(this).load(new File(theOne.getBigspritePath())).asGif().dontTransform().fitCenter().placeholder(R.drawable.placeholder).into(bigspriteImageView);
        }

        //get details
        if(theOne.getColorString() == null) {
            FetchDetailsAsyncTask fetchDetailsAsyncTask = new FetchDetailsAsyncTask(this);
            fetchDetailsAsyncTask.delegate = this;
            fetchDetailsAsyncTask.execute(theOne);
            bottomProgressBar.setVisibility(View.VISIBLE);

        } else {
            System.out.println("you got the details for this one already");
        }
        //show progress spinner

//        while(fetchDetailsAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
//            //
//        }
        //


    }

    @Override
    public void giveFilledPokemon(PokemonSpecies pokemonSpecies) {
        bottomProgressBar.setVisibility(View.GONE);
        System.out.println("hey i filled the details for " + pokemonSpecies.getName() + " the color is " + pokemonSpecies.getColorString());
        int themeToSet = 0;

        switch(pokemonSpecies.getColorString()) {
            case "green" : themeToSet = R.style.detailGreen;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.lefttoright, R.anim.lefttoright);

    }
}
