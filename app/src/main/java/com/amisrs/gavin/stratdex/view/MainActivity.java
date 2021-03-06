package com.amisrs.gavin.stratdex.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.controller.AsyncResponse;
import com.amisrs.gavin.stratdex.controller.FetchDetailsAsyncTask;
import com.amisrs.gavin.stratdex.controller.PokemonSpeciesAdapter;
import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.controller.FetchDexAsyncTask;
import com.amisrs.gavin.stratdex.db.AbilityQueries;
import com.amisrs.gavin.stratdex.db.DexSQLHelper;
import com.amisrs.gavin.stratdex.controller.LoadResponse;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoadResponse, AsyncResponse {
    public static String LIST_KEY = "LIST";

    private static final String TAG = "MainActivity";

    private RecyclerView rv;
    private LinearLayoutManager llm;
    public Context context;
    public PokemonSpeciesAdapter psa;
    public RelativeLayout coverLayout;
    public ProgressBar progressBar;
    public TextView loadingMsg;
    public TextView searchMsg;
    public EditText editText;
    public String searchString = "";
    public Button updateButton;

//    public RelativeLayout longCoverLayout;
//    public TextView longLoadMsg;
//    public ProgressBar longProgressBar;
//    public Button stopLongLoad;
    public boolean keepLoading = true;
    public ArrayList<FetchDetailsAsyncTask> asyncTasks = new ArrayList<>();

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
        editText = (EditText)findViewById(R.id.et_search);
        searchMsg = (TextView)findViewById(R.id.tv_searchno);

//        longCoverLayout = (RelativeLayout)findViewById(R.id.rl_longloadcover);
//        longProgressBar = (ProgressBar)findViewById(R.id.pb_longload);
//        longLoadMsg = (TextView)findViewById(R.id.hi2);
//        stopLongLoad = (Button)findViewById(R.id.btn_stoplongload);
//        stopLongLoad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                longCoverLayout.setVisibility(View.GONE);
//                keepLoading = false;
//                Log.i(TAG, "I have set KEEPLOADING to " + keepLoading);
//                for(FetchDetailsAsyncTask f : asyncTasks) {
//                        f.cancel(true);
//
//                }
//                Intent intent = new Intent(context, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        searchMsg.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);
        refreshRecycler();

        updateButton = (Button)findViewById(R.id.btn_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                updateButtonClick();
                progressBar.setVisibility(View.VISIBLE);
                //editText.setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);
            }
        });

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("LONGLOAD")) {
            if(intent.getStringExtra("LONGLOAD").equals("ye")) {
//                longCoverLayout.setVisibility(View.VISIBLE);
            }
        } else {
//            longCoverLayout.setVisibility(View.INVISIBLE);
        }
        ImageButton imageButton = (ImageButton)findViewById(R.id.btn_setting);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsButtonClick(v);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")) {
                    searchString = editText.getText().toString();
                    refreshRecycler();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.blank, R.anim.lefttoright);

    }

    public void settingsButtonClick(View v) {
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.inflate(R.menu.settings);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleSettingsMenuClick(item);
                return false;
            }
        });
        popupMenu.show();
    }

    public void handleSettingsMenuClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_reinitialize :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.reinitialise)).setTitle("Hey, listen!").setPositiveButton("PUNCH IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reinitialise();
                    }
                }).setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.item_load         :
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage(getResources().getString(R.string.load)).setTitle(getResources().getString(R.string.loadTitle)).setPositiveButton("PUNCH IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadAll();
                    }
                }).setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();

                break;
            case R.id.item_about        :
                showAbout();
                break;
            default                     :
                Log.w(TAG, "Non-existent MenuItem clicked in Settings Menu.");
        }

    }

    private void reinitialise() {


        DexSQLHelper dsh = new DexSQLHelper(this);
        SQLiteDatabase db = dsh.getWritableDatabase();
        dsh.dropTables(db);
        dsh.onCreate(db);
        db.close();
        dsh.close();
        Log.d(TAG,"***** REINITIALISE *****");
        Intent reIntent = new Intent(this, MainActivity.class);
        startActivity(reIntent);
        this.finish();
    }


    private void loadAll() {
        //updateButtonClick();
        SpeciesQueries speciesQueries = new SpeciesQueries(context);
        ArrayList<PokemonSpecies> pokemonSpecies = speciesQueries.getBasicSpecies();

        Intent reIntent = new Intent(this, MainActivity.class);
        reIntent.putExtra("LONGLOAD", "ye");
        startActivity(reIntent);

//        longProgressBar.setVisibility(View.VISIBLE);
//        longLoadMsg.setVisibility(View.VISIBLE);
//        stopLongLoad.setVisibility(View.VISIBLE);
        for(int i=0; i < pokemonSpecies.size() && keepLoading; i++) {
            Log.i(TAG, "KEEPLOADING = " + keepLoading);
            Log.d(TAG, "LOAD pokemon " + pokemonSpecies.get(i).getId());
            AbilityQueries abilityQueries = new AbilityQueries(this);
            final PokemonSpecies theOne = speciesQueries.getOneSpeciesById(pokemonSpecies.get(i).getId());
            theOne.setAbilities(abilityQueries.getAbilitiesForPokemon(Integer.parseInt(theOne.getId())));

            FetchDetailsAsyncTask fetchDetailsAsyncTask = new FetchDetailsAsyncTask(this);
            fetchDetailsAsyncTask.delegate = this;
            fetchDetailsAsyncTask.execute(theOne);
            asyncTasks.add(fetchDetailsAsyncTask);
        }
//        longCoverLayout.setVisibility(View.INVISIBLE);
        Log.d(TAG, "Finished loading all.");
    }

    public void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.about)).setTitle("About").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateButtonClick() {
        FetchDexAsyncTask fetch = new FetchDexAsyncTask(getApplicationContext());
        fetch.delegate = this;
        fetch.execute();
    }

    @Override
    public void updateLoadingMsg(String msg) {
        loadingMsg.setText(msg);
    }


    public void refreshRecycler() {
        SpeciesQueries speciesQueries = new SpeciesQueries(context);
        speciesQueries.open();

        ArrayList<PokemonSpecies> adapterList = speciesQueries.getBasicSpecies();
        ArrayList<PokemonSpecies> searchList = new ArrayList<>();

        for(PokemonSpecies p : adapterList) {
            if(p.getFullName().toUpperCase().contains(searchString.toUpperCase())) {
                searchList.add(p);
            }
        }
        if(searchList.size() == 0) {
            searchMsg.setVisibility(View.VISIBLE);
        } else {
            searchMsg.setVisibility(View.GONE);
        }

        PokemonSpeciesAdapter pokemonSpeciesAdapter = new PokemonSpeciesAdapter(searchList, context);
        psa = pokemonSpeciesAdapter;
        speciesQueries.close();
        Log.d(TAG, "new recylcer");
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

    @Override
    public void giveFilledPokemon(PokemonSpecies pokemonSpecies) {
        Log.i(TAG, "Hey a pokmeon has laoded");
//        longLoadMsg.setText("Loading " + pokemonSpecies.getFullName());
    }
}

// TODO:
// Optimisations
// 1. Probably make a superclass for model classes instead of always calling setIdFromUrl(); 10000 times for each one
// 2. Same for ____Queries classes.
// 3. Make sure every displayed string is actually a string resource.