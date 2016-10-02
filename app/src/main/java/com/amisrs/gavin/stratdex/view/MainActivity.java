package com.amisrs.gavin.stratdex.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.controller.PokemonSpeciesAdapter;
import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.controller.FetchDexAsyncTask;
import com.amisrs.gavin.stratdex.db.LoadResponse;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;

public class MainActivity extends AppCompatActivity implements LoadResponse {
    private RecyclerView rv;
    private LinearLayoutManager llm;
    public Context context;
    public PokemonSpeciesAdapter psa;
    public RelativeLayout coverLayout;
    public ProgressBar progressBar;
    public TextView loadingMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        llm = new LinearLayoutManager(this);
        rv = (RecyclerView)findViewById(R.id.rv);
        coverLayout = (RelativeLayout)findViewById(R.id.rl_cover);
        progressBar = (ProgressBar)findViewById(R.id.pb_load);
        loadingMsg = (TextView)findViewById(R.id.hi);
        progressBar.setVisibility(View.GONE);
        refreshRecycler();

        Button updateButton = (Button)findViewById(R.id.btn_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                updateButtonClick(v);
                progressBar.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.blank, R.anim.lefttoright);

    }

    public void updateButtonClick(View v) {
        FetchDexAsyncTask fetch = new FetchDexAsyncTask(getApplicationContext());
        fetch.delegate = this;
        fetch.execute();
    }

    @Override
    public void updateLoadingMsg(String msg) {
        loadingMsg.setText(msg);
    }

    public void finishedLoading(){
        coverLayout.setVisibility(View.GONE);
    }


    public void refreshRecycler() {
        SpeciesQueries speciesQueries = new SpeciesQueries(context);
        speciesQueries.open();
        PokemonSpeciesAdapter pokemonSpeciesAdapter = new PokemonSpeciesAdapter(speciesQueries.getBasicSpecies(), context);
        psa = pokemonSpeciesAdapter;
        speciesQueries.close();
        System.out.println("new recylcer");
        rv.setAdapter(pokemonSpeciesAdapter);
        rv.setLayoutManager(llm);
        if (pokemonSpeciesAdapter.getItemCount() == 0) {
            coverLayout.setVisibility(View.VISIBLE);
        } else {
            coverLayout.animate()
                    .alpha(0f)
                    .setDuration(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            coverLayout.setVisibility(View.GONE);

                        }
                    });
        }

    }

    @Override
    public void sayHasLoaded() {
        refreshRecycler();
    }
}
