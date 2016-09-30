package com.amisrs.gavin.stratdex.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amisrs.gavin.stratdex.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsBaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DetailsBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsBaseFragment newInstance(String param1, String param2) {
        DetailsBaseFragment fragment = new DetailsBaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static int getColorToSet(String color) {
        int colorToSet = 0;
        switch(color) {
            case "green"  : colorToSet = R.color.greenBackground;
                break;
            case "red"    : colorToSet = R.color.redBackground;
                break;
            case "blue"   : colorToSet = R.color.blueBackground;
                break;
            case "yellow" : colorToSet = R.color.yellowBackground;
                break;
            case "purple" : colorToSet = R.color.purpleBackground;
                break;
            case "black"  : colorToSet = R.color.blackBackground;
                break;
            case "pink"   : colorToSet = R.color.pinkBackground;
                break;
            case "brown"  : colorToSet = R.color.brownBackground;
                break;
            case "grey"   : colorToSet = R.color.greyBackground;
                break;
            case "white"  : colorToSet = R.color.whiteBackrgound;
                break;
            default      : colorToSet = R.color.defaultBackground;
                break;
        }

        return colorToSet;
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
        return inflater.inflate(R.layout.fragment_details_base, container, false);
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
