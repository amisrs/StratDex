package com.amisrs.gavin.stratdex.view;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.controller.PokemonSpeciesAdapter;
import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.db.AbilityQueries;
import com.amisrs.gavin.stratdex.controller.AsyncResponse;
import com.amisrs.gavin.stratdex.controller.FetchDetailsAsyncTask;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class DetailsActivity extends AppCompatActivity implements AsyncResponse,
        DetailsBottomFragment.OnFragmentInteractionListener,
        DetailsMovesFragment.OnFragmentInteractionListener,
        DetailsMiscFragment.OnFragmentInteractionListener {
    private TextView nameTextView;
    private TextView idTextView;
    private ImageView bigspriteImageView;
    private TextView loadingMsg;
    private ProgressBar bottomProgressBar;
    private PokemonSpecies thePokemon;
    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;
    private SlidingTabLayout slidingTabLayout;
    private ImageButton refreshButton;

    @Override
    public void updateLoadingMsg(String msg) {
        loadingMsg.setText(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);

        setContentView(R.layout.activity_details);
        idTextView = (TextView) findViewById(R.id.tv_id);
        nameTextView = (TextView) findViewById(R.id.tv_name);
        bigspriteImageView = (ImageView) findViewById(R.id.iv_bigsprite);
        bottomProgressBar = (ProgressBar) findViewById(R.id.pb_spinner);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pts_tabs);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        loadingMsg = (TextView) findViewById(R.id.tv_load);
        refreshButton = (ImageButton) findViewById(R.id.btn_refresh);



        bottomProgressBar.setVisibility(View.GONE);
        pagerTabStrip.setVisibility(View.GONE);

        Intent intent = getIntent();
        String id = intent.getStringExtra(PokemonSpeciesAdapter.LIST_KEY);
        SpeciesQueries speciesQueries = new SpeciesQueries(this);
        AbilityQueries abilityQueries = new AbilityQueries(this);
        final PokemonSpecies theOne = speciesQueries.getOneSpeciesById(id);
        theOne.setAbilities(abilityQueries.getAbilitiesForPokemon(Integer.parseInt(theOne.getId())));
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(theOne);
            }
        });


        nameTextView.setText(theOne.getFullName());
        idTextView.setText("#"+theOne.getId());

        SimpleTarget<GifDrawable> simpleTarget = new SimpleTarget<GifDrawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

            @Override
            public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                System.out.println("onresourceready simpletarget");
                String spriteFilePath = getFilesDir().toString();
                OutputStream out = null;

                File newSpriteFile = new File(spriteFilePath, "bigsprite" + theOne.getId() + ".png");
                try {
                    //out = new FileOutputStream(newSpriteFile);
                    out = openFileOutput("bigsprite"+theOne.getId()+".png", MODE_PRIVATE);
                    byte[] gifData = resource.getData();
                    out.write(gifData);
                    out.flush();
                    out.close();
                    //MediaStore.Images.Media.insertImage(getContentResolver(), newSpriteFile.getAbsolutePath(), newSpriteFile.getName(), newSpriteFile.getName());
                    System.out.println("image is stored");
                    SpeciesQueries speciesQueries = new SpeciesQueries(getApplicationContext());
                    speciesQueries.addSpriteFilePathForSpecies(theOne.getId(), spriteFilePath+"/bigsprite"+theOne.getId()+".png", "big");
                    System.out.println("image path is stored in database");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        if (theOne.getBigspritePath() == null) {

            System.out.println("the sprite is null for " + theOne.getName());

            Glide.with(this).load(theOne.getBigspriteString())
                    .asGif()
                    .into(simpleTarget);

            Glide.with(this).load(theOne.getBigspriteString()).asGif().dontTransform().fitCenter().placeholder(R.drawable.placeholder).into(bigspriteImageView);

        } else {
            System.out.println("could find bigspritepath at " + theOne.getBigspritePath() + " so loading from file.. this pokemon is " + theOne.getName());
            Glide.with(this).load(new File(theOne.getBigspritePath())).asGif().dontTransform().fitCenter().placeholder(R.drawable.placeholder).into(bigspriteImageView);
        }

        //get details
        if (theOne.getColorString() == null) {
            sendRequest(theOne);
        } else {
            System.out.println("you got the details for this one already");

            giveFilledPokemon(theOne);
        }

        //show progress spinner

//        while(fetchDetailsAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
//            //
//        }
        //


    }
    public void sendRequest(PokemonSpecies theOne) {
        FetchDetailsAsyncTask fetchDetailsAsyncTask = new FetchDetailsAsyncTask(this);
        fetchDetailsAsyncTask.delegate = this;
        fetchDetailsAsyncTask.execute(theOne);
        bottomProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void giveFilledPokemon(PokemonSpecies pokemonSpecies) {
        bottomProgressBar.setVisibility(View.GONE);
        System.out.println("hey i filled the details for " + pokemonSpecies.getName() + " the color is " + pokemonSpecies.getColorString()
                + " and it has " + pokemonSpecies.getAbilities().size() + " abilities");
        thePokemon = pokemonSpecies;
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        DetailsBottomFragment detailsBottom = new DetailsBottomFragment();
//        fragmentTransaction.add(R.id.rl_fragmentcontainer, detailsBottom);
//        fragmentTransaction.commit();

        viewPager = (ViewPager) findViewById(R.id.vp_pager);
        FragmentPagerAdapter vpAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpAdapter);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(DetailsBaseFragment.getTabColorToSet(thePokemon.getColorString()));
            }
        });

        slidingTabLayout.setViewPager(viewPager);
        //pagerTabStrip.setVisibility(View.VISIBLE);

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

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getStringExtra("FROM").equals("DETAILS")) {
            overridePendingTransition(R.anim.blank, R.anim.lefttoright);
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }





    public PokemonSpecies getThePokemon() {
        return thePokemon;
    }

    //https://guides.codepath.com/android/ViewPager-with-FragmentPagerAdapter
    public static class PagerAdapter extends FragmentPagerAdapter {

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return DetailsBottomFragment.newInstance("hi","Page 1");
                case 1:
                    return DetailsMovesFragment.newInstance("hi2", "Page 2");
                case 2:
                    return DetailsMiscFragment.newInstance("hi3", "Page 3");
                default:
                    return DetailsBottomFragment.newInstance("hi", "Page 1");
            }
        }

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "Attributes";
                case 1:
                    return "Moves";
                case 2:
                    return "Misc";
                default:
                    return "default";
            }
        }


    }
}
