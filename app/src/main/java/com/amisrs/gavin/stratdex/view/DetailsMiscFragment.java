package com.amisrs.gavin.stratdex.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.controller.OnPokemonClick;
import com.amisrs.gavin.stratdex.controller.PokemonSpeciesAdapter;
import com.amisrs.gavin.stratdex.db.DbBitmapUtility;
import com.amisrs.gavin.stratdex.db.EvoSlot;
import com.amisrs.gavin.stratdex.db.EvolutionQueries;
import com.amisrs.gavin.stratdex.db.SpeciesQueries;
import com.amisrs.gavin.stratdex.model.PokemonSpecies;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsMiscFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsMiscFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsMiscFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "DetailsMiscFragment";

    // TODO: Rename and change types of parameters
    private ScrollView scrollView;
    private TextView heightText;
    private TextView weightText;
    private TextView descText;
    private TextView genusText;
    private TextView generationText;
    private TextView regionText;
    private RelativeLayout relativeLayout;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DetailsMiscFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsMiscFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsMiscFragment newInstance(String param1, String param2) {
        DetailsMiscFragment fragment = new DetailsMiscFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_misc, container, false);
        DetailsActivity parentActivity = (DetailsActivity)getActivity();
        int colorToSet = DetailsBaseFragment.getColorToSet(parentActivity.getThePokemon().getColorString());
        final Context context = this.getContext();

        heightText = (TextView)view.findViewById(R.id.tv_height);
        weightText = (TextView)view.findViewById(R.id.tv_weight);
        descText = (TextView)view.findViewById(R.id.tv_desc);
        genusText = (TextView)view.findViewById(R.id.tv_descLabel);
        generationText = (TextView)view.findViewById(R.id.tv_gen);
        regionText = (TextView)view.findViewById(R.id.tv_region);

        float height = (float)parentActivity.getThePokemon().getHeight()/10;
        float weight = (float)parentActivity.getThePokemon().getWeight()/10;
        heightText.setText(height + "m");
        weightText.setText(weight + "kg");
        descText.setText(parentActivity.getThePokemon().getDesc());
        genusText.setText("The " + parentActivity.getThePokemon().getGenus() + " Pokémon");

        String genNumeral = parentActivity.getThePokemon().getGeneration();
        genNumeral = genNumeral.substring(genNumeral.indexOf("-")+1, genNumeral.length()).toUpperCase();

        generationText.setText(genNumeral);
        regionText.setText(parentActivity.getThePokemon().regionFromGeneration(parentActivity.getThePokemon().getGeneration()));

        SpeciesQueries speciesQueries = new SpeciesQueries(this.getContext());
        EvolutionQueries evolutionQueries = new EvolutionQueries(this.getContext());
        ArrayList<EvoSlot> slotArrayList = evolutionQueries.getSimpleChain(Integer.parseInt(parentActivity.getThePokemon().getId()));
        Collections.sort(slotArrayList);
        Log.d(TAG, "sorted the evo list, size is " + slotArrayList.size());
        // get lowest tier and highest tier subtract = number of rows needed
        if(!slotArrayList.isEmpty()) {
            int lowest = slotArrayList.get(0).getTier();
            int highest = slotArrayList.get(slotArrayList.size() - 1).getTier();


            int diff = highest - lowest;
            Log.d(TAG, "The difference in tiers is " + diff);

            TableLayout tableLayout = new TableLayout(this.getContext());
            //http://stackoverflow.com/questions/3277196/can-i-set-androidlayout-below-at-runtime-programmatically
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, R.id.tv_evolutionLabel);
            p.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.rl_main);
            tableLayout.setLayoutParams(p);
            tableLayout.setShrinkAllColumns(true);
            for (int i = lowest; i <= highest; i++) {
                //making new row
                Log.d(TAG, "Making the table, currently on tier " + i);
                TableRow newRow = new TableRow(this.getContext());
                newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                for (EvoSlot e : slotArrayList) {
                    if (e.getTier() == i) {
                        //new text add to the row
                        Log.d(TAG, "Adding pokemon to row " + i + ", currently adding " + e.getPid());
                        final PokemonSpecies newPokemon = speciesQueries.getOneSpeciesById(String.valueOf(e.getPid()));
                        newPokemon.setIdFromUrl();
                        TextView evoName = new TextView(this.getContext());

                        evoName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        evoName.setId(View.generateViewId());
                        evoName.setText(newPokemon.getFullName());

                        //hardcoding font size; but i had to do it just for eevee because there are so many evos
                        evoName.setTextSize(11);
                        evoName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        TextView evoLevel = new TextView(this.getContext());
                        evoLevel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) evoLevel.getLayoutParams();
                        layoutParams.addRule(RelativeLayout.BELOW, evoName.getId());
                        evoLevel.setLayoutParams(layoutParams);
                        evoLevel.setId(View.generateViewId());
                        evoLevel.setText("Lv. " + e.getLevel());
                        evoLevel.setTextSize(11);
                        evoLevel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        ImageView imageView = new ImageView(this.getContext());
                        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                        RelativeLayout.LayoutParams pc2 = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                        pc2.addRule(RelativeLayout.BELOW, evoLevel.getId());
                        imageView.setLayoutParams(pc2);
                        imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.spritecircle));

                        File file = new File(newPokemon.getSpriteString());
                        if (file.exists()) {
                            Glide.with(this.getContext()).load(file).placeholder(R.drawable.placeholder).into(imageView);
                        } else {
                            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(75, 75) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    String spriteFilePath = context.getFilesDir().toString();
                                    OutputStream out = null;

                                    File newSpriteFile = new File(spriteFilePath, newPokemon.getId() + ".png");
                                    try {
                                        out = context.openFileOutput(newPokemon.getId() + ".png", Context.MODE_PRIVATE);
                                        out.write(DbBitmapUtility.getBytes(resource));
                                        out.flush();
                                        out.close();
                                        SpeciesQueries speciesQueries = new SpeciesQueries(context);
                                        speciesQueries.addSpriteFilePathForSpecies(newPokemon.getId(), spriteFilePath + "/" + newPokemon.getId() + ".png", "small");
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };


                            Glide.with(context).load(newPokemon.getSpriteString())
                                    .asBitmap()
                                    .into(simpleTarget);

                            Glide.with(context).load(newPokemon.getSpriteString()).placeholder(R.drawable.placeholder).into(imageView);

                        }

                        final RelativeLayout relativeLayout = new RelativeLayout(this.getContext());
                        relativeLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        relativeLayout.addView(imageView);
                        relativeLayout.addView(evoLevel);
                        relativeLayout.addView(evoName);
                        OnPokemonClick onPokemonClick = new OnPokemonClick(newPokemon, this.getContext(), "DETAILS");
                        relativeLayout.setClickable(true);
                        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.entryDark));
                                } else {
                                    relativeLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                                }
                                return false;
                            }
                        });
                        relativeLayout.setOnClickListener(onPokemonClick);
                        newRow.addView(relativeLayout);
                    }
                }
                tableLayout.addView(newRow);
                if (i != highest) {
                    Log.d(TAG, "Adding the thing between tiers");
                    TableRow betweenRow = new TableRow(this.getContext());
                    newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    ImageView arrow = new ImageView(this.getContext());
                    arrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.downarrow));
                    arrow.getLayoutParams().height = 50;
                    arrow.getLayoutParams().width = 50;

                    betweenRow.addView(arrow);

                    tableLayout.addView(betweenRow);
                }
            }


            relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_main);
            RelativeLayout.LayoutParams what = (RelativeLayout.LayoutParams) tableLayout.getLayoutParams();
            what.addRule(RelativeLayout.CENTER_VERTICAL, 1);

            relativeLayout.addView(tableLayout);

            scrollView = (ScrollView) view.findViewById(R.id.sv_main);
            scrollView.setBackgroundColor(ContextCompat.getColor(this.getContext(), colorToSet));
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
