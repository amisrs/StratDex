package com.amisrs.gavin.stratdex.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.controller.MoveAdapter;
import com.amisrs.gavin.stratdex.db.MoveQueries;
import com.amisrs.gavin.stratdex.model.PMove;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsMovesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsMovesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsMovesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MoveAdapter moveAdapter;

    private OnFragmentInteractionListener mListener;

    public DetailsMovesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsMovesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsMovesFragment newInstance(String param1, String param2) {
        DetailsMovesFragment fragment = new DetailsMovesFragment();
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

        View view = inflater.inflate(R.layout.fragment_details_moves, container, false);
////        System.out.println("theme is : " + view.getContext().getTheme());

        DetailsActivity parentActivity = (DetailsActivity) getActivity();
        int colorToSet = DetailsBaseFragment.getColorToSet(parentActivity.getThePokemon().getColorString());

        MoveQueries moveQueries = new MoveQueries(this.getContext());
        ArrayList<PMove> pMoveArrayList = moveQueries.getMovesForPokemon(Integer.parseInt(parentActivity.getThePokemon().getId()));
        System.out.println("move list size in fragment " + pMoveArrayList.size());
        MoveAdapter moveAdapter = new MoveAdapter(moveQueries.getMovesForPokemon(Integer.parseInt(parentActivity.getThePokemon().getId())));

        // Inflate the layout for this fragment
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_moves);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(moveAdapter);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.fl_main);
        relativeLayout.setBackgroundColor(ContextCompat.getColor(this.getContext(),colorToSet));
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
