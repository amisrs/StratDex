package com.amisrs.gavin.stratdex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.amisrs.gavin.stratdex.db.AsyncResponse;
import com.amisrs.gavin.stratdex.db.DexSQLHelper;
import com.amisrs.gavin.stratdex.db.FetchDexAsyncTask;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView rv;
    private static LinearLayoutManager llm;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        llm = new LinearLayoutManager(this);
        rv = (RecyclerView)findViewById(R.id.rv);
        refreshRecycler();

        Button updateButton = (Button)findViewById(R.id.btn_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                updateButtonClick(v);
            }
        });
    }

    public void updateButtonClick(View v) {
        FetchDexAsyncTask fetch = new FetchDexAsyncTask(getApplicationContext());
        fetch.execute();

    }

    public static void refreshRecycler() {
        SpeciesQueries speciesQueries = new SpeciesQueries(context);
        PokemonSpeciesAdapter pokemonSpeciesAdapter = new PokemonSpeciesAdapter(speciesQueries.getBasicSpecies());
        System.out.println("new recylcer");
        rv.setAdapter(pokemonSpeciesAdapter);
        rv.setLayoutManager(llm);

    }
}
