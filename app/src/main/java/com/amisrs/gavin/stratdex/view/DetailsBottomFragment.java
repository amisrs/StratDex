package com.amisrs.gavin.stratdex.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.db.EvoSlot;
import com.amisrs.gavin.stratdex.db.EvolutionQueries;
import com.amisrs.gavin.stratdex.model.Ability;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsBottomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsBottomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsBottomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RelativeLayout relativeLayout;
    private ScrollView scrollView;
    private LinearLayout linearLayout;

    private TextView type1text;
    private TextView type2text;

    private ProgressBar stat1Bar;
    private ProgressBar stat2Bar;
    private ProgressBar stat3Bar;
    private ProgressBar stat4Bar;
    private ProgressBar stat5Bar;
    private ProgressBar stat6Bar;

    private TextView stat1amt;
    private TextView stat2amt;
    private TextView stat3amt;
    private TextView stat4amt;
    private TextView stat5amt;
    private TextView stat6amt;


    public DetailsBottomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsBottomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsBottomFragment newInstance(String param1, String param2) {
        DetailsBottomFragment fragment = new DetailsBottomFragment();
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

        //http://stackoverflow.com/questions/9469174/set-theme-for-a-fragment
//        System.out.println("Hey APPLY THE THEME!!!!");
//
//        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.detailGreen);
//        contextThemeWrapper.getTheme().applyStyle(R.style.detailGreen, true);
//        LayoutInflater newInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = inflater.inflate(R.layout.fragment_details_bottom, container, false);
//
//        System.out.println("theme is : " + view.getContext().getTheme());
        DetailsActivity parentActivity = (DetailsActivity) getActivity();
        int colorToSet = DetailsBaseFragment.getColorToSet(parentActivity.getThePokemon().getColorString());

        onCreate(savedInstanceState);

        type1text = (TextView)view.findViewById(R.id.tv_type1);
        type2text = (TextView)view.findViewById(R.id.tv_type2);

        stat1Bar = (ProgressBar)view.findViewById(R.id.pb_stat1);
        stat2Bar = (ProgressBar)view.findViewById(R.id.pb_stat2);
        stat3Bar = (ProgressBar)view.findViewById(R.id.pb_stat3);
        stat4Bar = (ProgressBar)view.findViewById(R.id.pb_stat4);
        stat5Bar = (ProgressBar)view.findViewById(R.id.pb_stat5);
        stat6Bar = (ProgressBar)view.findViewById(R.id.pb_stat6);

        stat1amt = (TextView)view.findViewById(R.id.tv_stat1amt);
        stat2amt = (TextView)view.findViewById(R.id.tv_stat2amt);
        stat3amt = (TextView)view.findViewById(R.id.tv_stat3amt);
        stat4amt = (TextView)view.findViewById(R.id.tv_stat4amt);
        stat5amt = (TextView)view.findViewById(R.id.tv_stat5amt);
        stat6amt = (TextView)view.findViewById(R.id.tv_stat6amt);

        type1text.setText(parentActivity.getThePokemon().getType1());
        type2text.setText(parentActivity.getThePokemon().getType2());

        stat1Bar.setProgress(parentActivity.getThePokemon().getStat1());
        stat2Bar.setProgress(parentActivity.getThePokemon().getStat2());
        stat3Bar.setProgress(parentActivity.getThePokemon().getStat3());
        stat4Bar.setProgress(parentActivity.getThePokemon().getStat4());
        stat5Bar.setProgress(parentActivity.getThePokemon().getStat5());
        stat6Bar.setProgress(parentActivity.getThePokemon().getStat6());

        ArrayList<ProgressBar> progressBars = new ArrayList<>();
        progressBars.add(stat1Bar);
        progressBars.add(stat2Bar);
        progressBars.add(stat3Bar);
        progressBars.add(stat4Bar);
        progressBars.add(stat5Bar);
        progressBars.add(stat6Bar);

        //http://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android
        //user mieszk3
        int barColor = ContextCompat.getColor(parentActivity, DetailsBaseFragment.getTabColorToSet(parentActivity.getThePokemon().getColorString()));
        for(ProgressBar pb : progressBars) {
            LayerDrawable layerDrawable = (LayerDrawable) pb.getProgressDrawable();
            Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
            Drawable bgDrawable = layerDrawable.findDrawableByLayerId(android.R.id.background);
            bgDrawable.setAlpha(0);
            progressDrawable.setColorFilter(barColor, PorterDuff.Mode.SRC_IN);
        }

        stat1amt.setText(String.valueOf(parentActivity.getThePokemon().getStat1()));
        stat2amt.setText(String.valueOf(parentActivity.getThePokemon().getStat2()));
        stat3amt.setText(String.valueOf(parentActivity.getThePokemon().getStat3()));
        stat4amt.setText(String.valueOf(parentActivity.getThePokemon().getStat4()));
        stat5amt.setText(String.valueOf(parentActivity.getThePokemon().getStat5()));
        stat6amt.setText(String.valueOf(parentActivity.getThePokemon().getStat6()));



        linearLayout = (LinearLayout)view.findViewById(R.id.ll_abilities);
        ArrayList<Ability> abilities = parentActivity.getThePokemon().getAbilities();
        for(Ability a : abilities) {
            TextView abilityName = new TextView(this.getContext());
            TextView abilityDesc = new TextView(this.getContext());

            abilityName.setTextSize(getResources().getDimension(R.dimen.heading3forprogram));
            abilityName.setText(a.getCleanName());
            abilityDesc.setText(a.getDesc() + "\n");

            linearLayout.addView(abilityName);
            linearLayout.addView(abilityDesc);
        }


        relativeLayout = (RelativeLayout)view.findViewById(R.id.fl_main);
        scrollView = (ScrollView)view.findViewById(R.id.sv_main);
        scrollView.setBackgroundColor(ContextCompat.getColor(this.getContext(),colorToSet));
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        System.out.println("THIS IS ONACTIVITYCREATE");
        //System.out.println("activity: "+ getView().getContext().getTheme().applyStyle(R.style.detailGreen,true));



        super.onActivityCreated(savedInstanceState);
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
