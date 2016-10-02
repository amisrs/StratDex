package com.amisrs.gavin.stratdex.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amisrs.gavin.stratdex.R;
import com.amisrs.gavin.stratdex.model.Move;
import com.amisrs.gavin.stratdex.model.PMove;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Gavin on 2/10/2016.
 */
public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.MoveViewHolder>{
    private ArrayList<PMove> pMoves;

    public MoveAdapter(ArrayList<PMove> pMoves) {
        this.pMoves = pMoves;
        Collections.sort(this.pMoves);
    }

    @Override
    public MoveAdapter.MoveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_layout, parent, false);
        return new MoveViewHolder(inflatedView);
    }

    @Override
    public int getItemCount() {
        return pMoves.size();
    }

    @Override
    public void onBindViewHolder(MoveViewHolder holder, int position) {
        PMove pMove = pMoves.get(position);
        System.out.println("hey im binding " + pMove.getMove().getCleanName());
        holder.bindMove(pMove);
    }

    public static class MoveViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView nameTextView;
        public TextView levelTextView;
        public PMove data;


        public MoveViewHolder(View itemView) {

            super(itemView);
            this.view = itemView;
            nameTextView = (TextView)view.findViewById(R.id.tv_name);
            levelTextView = (TextView)view.findViewById(R.id.tv_level);

        }

        public void bindMove(PMove pMove) {
            data = pMove;
            nameTextView.setText(data.getMove().getCleanName());
            levelTextView.setText(String.valueOf(data.getVersion_group_details()[0].getLevel_learned_at()));
            if(levelTextView.getText().equals("0")) {
                levelTextView.setText("--");
            }
        }
    }
}
